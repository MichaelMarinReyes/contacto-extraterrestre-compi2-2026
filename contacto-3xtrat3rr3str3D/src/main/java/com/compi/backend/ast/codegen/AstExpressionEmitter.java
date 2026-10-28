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
        if (callable.getCategory() == SymbolCategory.CONSTRUCTOR) {
            // Una llamada super(...) reutiliza el objeto que ya está construyendo
            // la subclase. A diferencia de `new`, no reserva otro objeto, pero sí
            // debe invocar la etiqueta completa del constructor padre y pasar this.
            Symbol owner = callable.getOwnerClass();
            name = FunctionRuntime.name(owner.getName(), callable.getName(), count);
            Symbol receiver = g.table.resolve("this");
            if (receiver == null) throw new IllegalStateException("super(...) fuera de un constructor");
            g.c3d.emit(QuadrupleOp.PARAM, g.memory.capture(receiver.getStorageName()), null, null);
            count++;
        } else if (callable.getCategory() == SymbolCategory.METHOD) {
            Symbol owner;
            String receiver;
            if (n.callee() instanceof MemberAccessNodeAST member) {
                owner = g.table.getClass(g.type(member.target()).getCustomTypeName());
                receiver = g.emit(member.target());
            } else {
                owner = g.table.getClass(g.currentClass.name());
                receiver = g.table.resolve("this").getStorageName();
            }
            java.util.Map<Integer, String> targets = virtualTargets(owner, callable, count);
            if (targets.size() > 1) return dispatch(n, receiver, targets, count);
            name = label(callable);
            g.c3d.emit(QuadrupleOp.PARAM, g.memory.capture(receiver), null, null);
            count++;
        }
        for (AbstractExpressionNodeAST arg : n.arguments())
            g.c3d.emit(QuadrupleOp.PARAM, g.memory.capture(g.emit(arg)), null, null);
        String result = g.c3d.newTemp();
        g.c3d.emit(QuadrupleOp.CALL, name, String.valueOf(count), result);
        return result;
    }

    /** Etiqueta C3D de un método según la clase que realmente lo declara. */
    private String label(Symbol method) {
        Symbol owner = method.getOwnerClass() == null ? g.table.getClass(g.currentClass.name()) : method.getOwnerClass();
        return FunctionRuntime.name(owner.getName(), method.getName(), method.getParameters().size());
    }

    /** Etiqueta concreta para cada tipo de la jerarquía; más de una exige despacho virtual. */
    private java.util.Map<Integer, String> virtualTargets(Symbol owner, Symbol callable, int count) {
        java.util.Map<Integer, String> targets = new java.util.LinkedHashMap<>();
        if (owner == null) return targets;
        for (Symbol clazz : g.table.getClasses()) {
            if (!isDescendant(clazz, owner)) continue;
            Symbol method = clazz.resolveMethod(callable.getName(), count);
            if (method != null && method.getOwnerClass() != null) targets.put(clazz.getTypeTag(), label(method));
        }
        return targets;
    }

    private boolean isDescendant(Symbol clazz, Symbol root) {
        for (Symbol current = clazz; current != null; current = current.getParentClass())
            if (current == root) return true;
        return false;
    }

    /** Selecciona en ejecución el método según la marca de tipo del receptor. */
    private String dispatch(CallNodeAST n, String receiver, java.util.Map<Integer, String> targets, int count) {
        String recv = g.memory.capture(receiver);
        java.util.List<String> arguments = new java.util.ArrayList<>();
        for (AbstractExpressionNodeAST argument : n.arguments())
            arguments.add(g.memory.capture(g.emit(argument)));
        String tag = g.c3d.newTemp();
        g.c3d.emit(QuadrupleOp.TYPE_GET, recv, null, tag);
        String result = g.c3d.newTemp();
        String end = g.c3d.newLabel();
        java.util.List<String> branches = new java.util.ArrayList<>();
        for (Integer value : targets.keySet()) {
            String branch = g.c3d.newLabel();
            branches.add(branch);
            g.c3d.emit(QuadrupleOp.IF_EQ, tag, String.valueOf(value), branch);
        }
        g.c3d.emitGoto(branches.get(0));
        int index = 0;
        for (String target : targets.values()) {
            g.c3d.emitLabel(branches.get(index++));
            g.c3d.emit(QuadrupleOp.PARAM, recv, null, null);
            for (String argument : arguments) g.c3d.emit(QuadrupleOp.PARAM, argument, null, null);
            g.c3d.emit(QuadrupleOp.CALL, target, String.valueOf(count + 1), result);
            g.c3d.emitGoto(end);
        }
        g.c3d.emitLabel(end);
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
