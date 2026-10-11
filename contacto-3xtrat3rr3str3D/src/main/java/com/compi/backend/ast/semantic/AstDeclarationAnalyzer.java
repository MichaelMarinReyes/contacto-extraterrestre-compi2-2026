package com.compi.backend.ast.semantic;

import com.compi.backend.ast.*;
import com.compi.backend.ast.decl.AttributeDefinitionNodeAST;
import com.compi.backend.ast.expr.*;
import com.compi.backend.ast.stmt.*;
import com.compi.backend.symbols.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/** Declaraciones e inicializadores con comprobación de formas y límites. */
final class AstDeclarationAnalyzer {
    private final AstSemanticAnalyzer a;
    AstDeclarationAnalyzer(AstSemanticAnalyzer analyzer) { a = analyzer; }

    void global(AbstractStatementNodeAST node) {
        if (node instanceof DeclarationNodeAST declaration) {
            a.model.scope(node, a.table.getGlobalScope());
            a.result(node, declare(declaration, true));
        } else a.evaluate(node);
    }

    Type declared(Type element, List<AbstractExpressionNodeAST> dimensions, ASTNode node) {
        Type type = a.resolveType(element, node);
        if (dimensions.isEmpty()) return type;
        int[] sizes = new int[dimensions.size()];
        for (int i = 0; i < sizes.length; i++) {
            AbstractExpressionNodeAST dimension = dimensions.get(i);
            sizes[i] = size(dimension);
        }
        return Type.arrayOf(type, sizes);
    }

    private int size(AbstractExpressionNodeAST dimension) {
        if (dimension == null) return Type.UNKNOWN_SIZE;
        Type type = a.evaluate(dimension);
        if (Type.isKnown(type) && !Type.INT.equals(type)) a.report("La dimensión debe ser entera", dimension);
        BigInteger size = AstExpressionAnalyzer.constant(dimension);
        if (size == null) return Type.UNKNOWN_SIZE;
        if (size.signum() <= 0 || size.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
            a.report("Las dimensiones del arreglo deben ser positivas y representables", dimension);
            return Type.UNKNOWN_SIZE;
        }
        return size.intValue();
    }

    Type declare(DeclarationNodeAST n, boolean global) {
        Type expected = declared(n.type(), n.dimensions(), n);
        Type actual = initializer(n.initializer(), expected);
        if (!Type.isKnown(expected) && n.type().equals(Type.UNKNOWN)) expected = actual;
        if (n.initializer() instanceof ArrayLiteralNodeAST && expected.isArray()) {
            expected = ArrayLiteral.completeSizes(expected, actual);
        }
        if (expected.isArray() && !expected.hasSizes() && n.initializer() == null) {
            a.report("El arreglo «" + n.name() + "» no tiene tamaño ni inicializador", n);
        }
        if (n.initializer() != null) a.compatible(expected, actual, n);
        Scope scope = global ? a.table.getGlobalScope() : a.table.getCurrentScope();
        Symbol symbol = a.symbol(n.name(), expected, SymbolCategory.VARIABLE, scope.allocateOffset(1), global, n);
        boolean defined = global ? a.table.defineGlobal(symbol) : a.table.define(symbol);
        if (!defined) a.report("La variable «" + n.name() + "» ya está declarada en este ámbito", n);
        return expected;
    }

    Type attribute(AttributeDefinitionNodeAST n) {
        Symbol field = a.currentClass.getMember(n.name());
        Type expected = field.getType();
        a.model.symbol(n, field);
        if (n.initializer() != null) a.compatible(expected, initializer(n.initializer(), expected), n);
        return expected;
    }

    Type initializer(AbstractExpressionNodeAST node, Type expected) {
        if (node == null) return Type.UNKNOWN;
        Type type;
        a.model.scope(node, a.table.getCurrentScope());
        if (node instanceof InstanceInitNodeAST instance) type = instance(instance, expected);
        else if (node instanceof ArrayLiteralNodeAST array) {
            if (expected.isStruct() || expected.isClass()) {
                Symbol owner = expected.isStruct() ? a.table.getStruct(expected.getCustomTypeName()) : a.table.getClass(expected.getCustomTypeName());
                positionalFields(array.elements(), owner, array);
                type = expected;
            } else type = arrayLiteral(array, expected);
        }
        else return a.evaluate(node);
        return a.result(node, type);
    }

    Type newArray(NewArrayNodeAST n) {
        int[] sizes = n.sizes().stream().mapToInt(this::size).toArray();
        return Type.arrayOf(a.resolveType(n.elementType(), n), sizes);
    }

    Type arrayLiteral(ArrayLiteralNodeAST n, Type expected) {
        Type rowType = Type.UNKNOWN;
        int[] rowShape = new int[0];
        Type expectedRow = expected.isArray() ? expected.desindexar(1) : Type.UNKNOWN;
        for (int i = 0; i < n.elements().size(); i++) {
            AbstractExpressionNodeAST element = n.elements().get(i);
            Type type = initializer(element, expectedRow);
            Type scalar = type.isArray() ? type.getElementType() : type;
            int[] shape = type.isArray() ? type.getSizes() : new int[0];
            if (i == 0) { rowType = scalar; rowShape = shape; }
            else {
                if (!Arrays.equals(rowShape, shape)) a.report("El arreglo contiene filas de tamaños desiguales", element);
                if (rowType.isNumeric() && scalar.isNumeric()) {
                    if (Type.DOUBLE.equals(scalar)) rowType = Type.DOUBLE;
                } else if (Type.isKnown(rowType) && Type.isKnown(scalar) && !rowType.equals(scalar)) {
                    a.report("El arreglo contiene elementos de tipos incompatibles", element);
                }
            }
        }
        int[] sizes = new int[rowShape.length + 1];
        sizes[0] = n.elements().size();
        System.arraycopy(rowShape, 0, sizes, 1, rowShape.length);
        Type actual = Type.arrayOf(rowType, sizes);
        validateShape(expected, actual, n);
        return actual;
    }

    private void validateShape(Type expected, Type actual, ASTNode n) {
        if (!expected.isArray()) return;
        if (expected.getDimensions() != actual.getDimensions()) {
            a.report("La forma del inicializador no coincide con las dimensiones declaradas", n);
            return;
        }
        int[] sizes = expected.getSizes(), found = actual.getSizes();
        for (int i = 0; i < sizes.length; i++) {
            if (sizes[i] > 0 && sizes[i] != found[i]) a.report("Tamaño incompatible en la dimensión " + (i + 1)
                    + ": se esperaba " + sizes[i] + " y se obtuvo " + found[i], n);
        }
        a.compatible(expected.getElementType(), actual.getElementType(), n);
    }

    Type instance(InstanceInitNodeAST n, Type expected) {
        for (String name : n.duplicateFields()) a.report("Campo repetido «" + name + "» en el inicializador", n);
        if (n.arrayRef() != null) {
            Type actual = a.evaluate(n.arrayRef());
            a.compatible(expected, actual, n);
            return actual;
        }
        Type target = n.typeName().isEmpty() ? expected : a.resolveType(Type.structType(n.typeName()), n);
        Symbol owner = target.isStruct() ? a.table.getStruct(target.getCustomTypeName())
                : target.isClass() ? a.table.getClass(target.getCustomTypeName()) : null;
        if (owner == null) { a.report("El inicializador requiere una estructura o clase definida", n); return Type.UNKNOWN; }
        a.model.symbol(n, owner);
        if (!n.positional().isEmpty()) {
            positionalFields(n.positional(), owner, n);
        }
        n.named().forEach((name, value) -> {
            Symbol field = owner.getMember(name);
            if (field == null || field.getCategory() != SymbolCategory.FIELD) { a.report("El campo «" + name + "» no existe en «" + owner.getName() + "»", n); initializer(value, Type.UNKNOWN); }
            else a.compatible(field.getType(), initializer(value, field.getType()), value);
        });
        return target;
    }

    private void positionalFields(List<AbstractExpressionNodeAST> values, Symbol owner, ASTNode node) {
        if (owner == null) { a.report("El inicializador requiere un tipo definido", node); return; }
        List<Symbol> fields = owner.getMembers().stream().filter(field -> field.getCategory() == SymbolCategory.FIELD).toList();
        if (values.size() != fields.size()) a.report("Cantidad de campos incompatible para «" + owner.getName() + "»", node);
        for (int i = 0; i < values.size(); i++) {
            Type field = i < fields.size() ? fields.get(i).getType() : Type.UNKNOWN;
            a.compatible(field, initializer(values.get(i), field), values.get(i));
        }
    }
}
