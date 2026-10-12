package com.compi.backend.ast.codegen;

import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.exprressions.*;
import com.compi.backend.c3d.FunctionRuntime;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.c3d.ValueRuntime;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolCategory;
import com.compi.backend.symbols.Type;

/**
 * Emisión de valores, operaciones, llamadas e incrementos.
 */
final class AstExpressionEmitter {
    private final AstCodeGenerator g;

    AstExpressionEmitter(AstCodeGenerator generator) {
        g = generator;
    }

    String literal(LiteralNodeAST n) {
        return switch (n.kind()) {
            case BOOLEAN -> Boolean.TRUE.equals(n.value()) ? "1" : "0";
            case NULL -> "-1";
            case CHARACTER -> String.valueOf((int) n.value().toString().charAt(0));
            case INTEGER, DECIMAL -> n.value().toString();
            case STRING -> text(n.value().toString());
        };
    }

    private String text(String value) {
        String temp = g.c3d.newTemp();
        String literal = "\"" + value.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + "\"";
        g.c3d.emitAssign(temp, literal);
        return temp;
    }

    String binary(BinaryOperationNodeAST n) {
        if (n.kind() == OperationKind.LOGICAL || n.kind() == OperationKind.RELATIONAL) return g.control.booleanValue(n);
        String left = g.memory.capture(g.emit(n.left()));
        String right = g.emit(n.right());
        if (n.operator().equals("+") && Type.STRING.equals(g.type(n))) {
            left = ValueRuntime.asText(g.c3d, left, g.type(n.left()));
            right = ValueRuntime.asText(g.c3d, right, g.type(n.right()));
            return operation(QuadrupleOp.CONCAT, left, right);
        }
        return operation(arithmetic(n.operator(), g.type(n.left()), g.type(n.right())), left, right);
    }

    private QuadrupleOp arithmetic(String operator, Type left, Type right) {
        return switch (operator) {
            case "+" -> QuadrupleOp.ADD;
            case "-" -> QuadrupleOp.SUB;
            case "*" -> QuadrupleOp.MUL;
            case "/" -> Type.INT.equals(left) && Type.INT.equals(right) ? QuadrupleOp.INT_DIV : QuadrupleOp.DIV;
            case "%" -> QuadrupleOp.MOD;
            default -> throw new IllegalStateException("Operador aritmético desconocido: " + operator);
        };
    }

    private String operation(QuadrupleOp op, String left, String right) {
        String result = g.c3d.newTemp();
        g.c3d.emit(op, left, right, result);
        return result;
    }

    String unary(UnaryOperationNodeAST n) {
        return operation(n.operator().equals("!") ? QuadrupleOp.NOT : QuadrupleOp.NEG, g.emit(n.operand()), null);
    }

    String assign(AbstractExpressionNodeAST target, String operator, AbstractExpressionNodeAST expression) {
        var place = g.memory.place(target);
        String old = operator.equals("=") ? null : g.memory.capture(g.memory.read(place));
        String value = g.aggregates.initializer(expression, g.type(target));
        if (!operator.equals("=")) {
            value = operation(arithmetic(operator.substring(0, 1), g.type(target), g.type(expression)), old, value);
        }
        g.memory.write(place, value);
        return value;
    }

    String increment(IncrementNodeAST n) {
        var place = g.memory.place(n.target());
        String old = g.memory.capture(g.memory.read(place));
        String updated = operation(n.operator().equals("++") ? QuadrupleOp.ADD : QuadrupleOp.SUB, old, "1");
        g.memory.write(place, updated);
        return n.prefix() ? updated : old;
    }

    String ternary(TernaryNodeAST n) {
        String result = g.c3d.newTemp();
        String yes = g.c3d.newLabel(), no = g.c3d.newLabel(), end = g.c3d.newLabel();
        g.control.jump(n.condition(), yes, no);
        g.c3d.emitLabel(yes);
        g.c3d.emitAssign(result, g.emit(n.thenExpr()));
        g.c3d.emitGoto(end);
        g.c3d.emitLabel(no);
        g.c3d.emitAssign(result, g.emit(n.elseExpr()));
        g.c3d.emitLabel(end);
        return result;
    }

    String call(CallNodeAST n) {
        Symbol callable = g.model.symbolOf(n);
        if (callable == null && (n.name().equals("print") || n.name().equals("println"))) {
            for (AbstractExpressionNodeAST arg : n.arguments())
                g.c3d.emit(printOp(g.type(arg)), g.emit(arg), null, null);
            if (n.name().equals("println")) g.c3d.emit(QuadrupleOp.PRINTLN, null, null, null);
            return "0";
        }
        if (callable == null) throw new IllegalStateException("Llamada sin símbolo semántico");
        String name = callable.getName();
        int count = n.arguments().size();
        if (callable.getCategory() == SymbolCategory.METHOD) {
            String owner;
            String receiver;
            if (n.callee() instanceof MemberAccessNodeAST member) {
                owner = g.type(member.target()).getCustomTypeName();
                receiver = g.emit(member.target());
            } else {
                owner = g.currentClass.name();
                receiver = g.table.resolve("this").getStorageName();
            }
            name = FunctionRuntime.name(owner, name, count);
            g.c3d.emit(QuadrupleOp.PARAM, g.memory.capture(receiver), null, null);
            count++;
        }
        for (AbstractExpressionNodeAST arg : n.arguments())
            g.c3d.emit(QuadrupleOp.PARAM, g.memory.capture(g.emit(arg)), null, null);
        String result = g.c3d.newTemp();
        g.c3d.emit(QuadrupleOp.CALL, name, String.valueOf(count), result);
        return result;
    }

    QuadrupleOp printOp(Type type) {
        if (Type.STRING.equals(type)) return QuadrupleOp.PRINT_STR;
        if (Type.DOUBLE.equals(type)) return QuadrupleOp.PRINT_FLOAT;
        if (Type.CHAR.equals(type)) return QuadrupleOp.PRINT_CHAR;
        return QuadrupleOp.PRINT_INT;
    }

    String read(Type type) {
        String result = g.c3d.newTemp();
        QuadrupleOp op = Type.STRING.equals(type) ? QuadrupleOp.READ_STR : Type.CHAR.equals(type) ? QuadrupleOp.READ_CHAR : QuadrupleOp.READ;
        g.c3d.emit(op, result, null, null);
        return result;
    }
}
