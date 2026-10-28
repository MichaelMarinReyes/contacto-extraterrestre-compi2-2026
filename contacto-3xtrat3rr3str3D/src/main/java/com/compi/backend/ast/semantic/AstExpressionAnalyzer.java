package com.compi.backend.ast.semantic;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.exprressions.*;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.Type;
import com.compi.backend.symbols.TypeCompatibility;

import java.math.BigInteger;
import java.util.List;

/**
 * Tipos y resolución de expresiones, llamadas y destinos de escritura.
 */
final class AstExpressionAnalyzer {
    private final AstSemanticAnalyzer a;

    AstExpressionAnalyzer(AstSemanticAnalyzer analyzer) {
        a = analyzer;
    }

    static BigInteger constant(AbstractExpressionNodeAST n) {
        if (n instanceof LiteralNodeAST literal && literal.kind() == LiteralKind.INTEGER) {
            return new BigInteger(literal.value().toString());
        }
        if (n instanceof UnaryOperationNodeAST unary && unary.operator().equals("-")) {
            BigInteger value = constant(unary.operand());
            return value == null ? null : value.negate();
        }
        return null;
    }

    Type literal(LiteralNodeAST n) {
        return switch (n.kind()) {
            case INTEGER -> Type.INT;
            case DECIMAL -> Type.DOUBLE;
            case STRING -> Type.STRING;
            case CHARACTER -> Type.CHAR;
            case BOOLEAN -> Type.BOOLEAN;
            case NULL -> Type.NULL;
        };
    }

    Type identifier(IdentifierNodeAST n) {
        Symbol symbol = a.table.resolve(n.name());
        if (symbol == null && a.currentClass != null) symbol = a.inheritedField(a.currentClass, n.name());
        if (symbol == null) {
            a.report("Símbolo «" + n.name() + "» no declarado", n);
            return Type.UNKNOWN;
        }
        a.checkAccess(symbol, n);
        a.model.symbol(n, symbol);
        return symbol.getType();
    }

    Type binary(BinaryOperationNodeAST n) {
        Type left = a.evaluate(n.left()), right = a.evaluate(n.right());
        Type result = switch (n.kind()) {
            case LOGICAL -> TypeCompatibility.checkLogical(left, right);
            case RELATIONAL -> n.operator().equals("==") || n.operator().equals("!=")
                    ? TypeCompatibility.checkEquality(left, right) : TypeCompatibility.checkRelational(left, right, n.operator());
            default -> TypeCompatibility.checkArithmetic(left, right, n.operator());
        };
        if (Type.isKnown(left) && Type.isKnown(right) && !Type.isKnown(result)) {
            a.report("Operandos incompatibles para «" + n.operator() + "»: «" + left.label() + "», «" + right.label() + "»", n);
        }
        return result;
    }

    Type unary(UnaryOperationNodeAST n) {
        Type operand = a.evaluate(n.operand());
        if (n.operator().equals("!")) {
            if (Type.isKnown(operand) && !Type.BOOLEAN.equals(operand)) a.report("La negación requiere un booleano", n);
            return Type.BOOLEAN;
        }
        if (Type.isKnown(operand) && !operand.isNumeric()) {
            a.report("El signo menos requiere un número", n);
            return Type.UNKNOWN;
        }
        return operand;
    }

    Type member(MemberAccessNodeAST n) {
        Type target = a.evaluate(n.target());
        if (!Type.isKnown(target)) return Type.UNKNOWN;
        Symbol owner = owner(target);
        Symbol member = owner == null ? null : owner.getMember(n.member());
        if (member == null) {
            a.report("El miembro «" + n.member() + "» no existe en «" + target.label() + "»", n);
            return Type.UNKNOWN;
        }
        a.checkAccess(member, n);
        a.model.symbol(n, member);
        return member.getType();
    }

    private Symbol owner(Type type) {
        if (type.isClass()) return a.table.getClass(type.getCustomTypeName());
        if (type.isStruct()) return a.table.getStruct(type.getCustomTypeName());
        return null;
    }

    Type index(IndexNodeAST n) {
        Type target = a.evaluate(n.target());
        for (AbstractExpressionNodeAST index : n.indices()) {
            Type type = a.evaluate(index);
            if (Type.isKnown(type) && !Type.INT.equals(type)) a.report("El índice debe ser entero", index);
            if (!Type.isKnown(target)) continue;
            if (!target.isArray()) {
                a.report("El destino no es un arreglo o tiene demasiados índices", n);
                return Type.UNKNOWN;
            }
            BigInteger value = constant(index);
            int[] sizes = target.getSizes();
            if (value != null && sizes.length > 0 && sizes[0] > 0
                    && (value.signum() < 0 || value.compareTo(BigInteger.valueOf(sizes[0])) >= 0)) {
                a.report("Índice " + value + " fuera de rango [0.." + (sizes[0] - 1) + "]", index);
            }
            target = target.desindexar(1);
        }
        return target;
    }

    void requireTarget(AbstractExpressionNodeAST n) {
        if (!(n instanceof IdentifierNodeAST || n instanceof MemberAccessNodeAST || n instanceof IndexNodeAST)) {
            a.report("El destino debe ser una variable, miembro o elemento de arreglo", n);
        }
    }

    Type assign(AbstractExpressionNodeAST target, String operator, AbstractExpressionNodeAST value, ASTNode n) {
        requireTarget(target);
        Type expected = a.evaluate(target);
        Type actual = a.declarations.initializer(value, expected);
        if (!operator.equals("=")) {
            Type operation = TypeCompatibility.checkArithmetic(expected, actual, operator.substring(0, 1));
            if (Type.isKnown(expected) && Type.isKnown(actual) && !Type.isKnown(operation)) {
                a.report("Operandos incompatibles para «" + operator + "»", n);
            }
            actual = operation;
        }
        a.compatible(expected, actual, n);
        return expected;
    }

    Type increment(IncrementNodeAST n) {
        requireTarget(n.target());
        Type type = a.evaluate(n.target());
        if (Type.isKnown(type) && !type.isNumeric()) a.report("Incrementar o decrementar requiere un número", n);
        return type;
    }

    Type ternary(TernaryNodeAST n) {
        a.condition(n.condition());
        Type yes = a.evaluate(n.thenExpr()), no = a.evaluate(n.elseExpr());
        if (!Type.isKnown(yes) || !Type.isKnown(no)) return Type.UNKNOWN;
        if (yes.equals(no)) return yes;
        if (yes.isNumeric() && no.isNumeric()) return Type.DOUBLE;
        if (a.assignable(no, yes)) return no;
        if (a.assignable(yes, no)) return yes;
        a.report("Las ramas del ternario tienen tipos incompatibles", n);
        return Type.UNKNOWN;
    }

    Type call(CallNodeAST n) {
        List<Type> args = n.arguments().stream().map(a::evaluate).toList();
        Symbol method = null;
        if (n.callee() instanceof MemberAccessNodeAST member) {
            Symbol owner = owner(a.evaluate(member.target()));
            if (owner != null) method = owner.resolveMethod(member.member(), args.size());
            if (method != null) a.checkAccess(method, n.callee());
        } else if (n.callee() instanceof IdentifierNodeAST id) {
            if (List.of("print", "println").contains(id.name()) && a.language.equals("zet")) return Type.VOID;
            if ("super".equals(id.name()) && a.currentClass != null && a.currentClass.getParentClass() != null) {
                Symbol parent = a.currentClass.getParentClass();
                method = parent.getCallable(parent.getName(), args.size());
            } else if (a.currentClass != null) {
                method = a.currentClass.resolveMethod(id.name(), args.size());
            }
            if (method == null) method = a.table.getFunction(id.name());
        }
        if (method == null) {
            a.report("Función o método «" + n.name() + "» no definido con " + args.size() + " argumentos", n);
            return Type.UNKNOWN;
        }
        validateCall(method, args, n.arguments(), n);
        a.model.symbol(n, method);
        a.model.symbol(n.callee(), method);
        a.model.scope(n.callee(), a.table.getCurrentScope());
        a.model.type(n.callee(), method.getType());
        return method.getReturnType() == null ? method.getType() : method.getReturnType();
    }

    Type newObject(NewObjectNodeAST n) {
        List<Type> args = n.arguments().stream().map(a::evaluate).toList();
        Symbol owner = a.table.getClass(n.typeName());
        if (owner == null) {
            a.report("Tipo «" + n.typeName() + "» no definido", n);
            return Type.UNKNOWN;
        }
        Symbol constructor = owner.getCallable(n.typeName(), args.size());
        if (constructor == null)
            a.report("No existe un constructor de «" + n.typeName() + "» con " + args.size() + " argumentos", n);
        else validateCall(constructor, args, n.arguments(), n);
        a.model.symbol(n, constructor);
        return owner.getType();
    }

    private void validateCall(Symbol method, List<Type> args, List<AbstractExpressionNodeAST> nodes, ASTNode n) {
        if (method.getParameters().size() != args.size()) {
            a.report("«" + method.getName() + "» espera " + method.getParameters().size() + " argumentos y recibió " + args.size(), n);
            return;
        }
        for (int i = 0; i < args.size(); i++) {
            Symbol param = method.getParameters().get(i);
            a.compatible(param.getType(), args.get(i), nodes.get(i));
            if (param.isByReference()) requireTarget(nodes.get(i));
        }
    }
}
