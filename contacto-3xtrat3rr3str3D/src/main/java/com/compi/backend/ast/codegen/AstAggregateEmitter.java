package com.compi.backend.ast.codegen;

import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.expr.*;
import com.compi.backend.ast.stmt.DeclarationNodeAST;
import com.compi.backend.c3d.*;
import com.compi.backend.symbols.*;
import java.util.List;

/** Reserva y rellena agregados; cada campo guarda una referencia o un escalar. */
final class AstAggregateEmitter {
    private final AstCodeGenerator g;
    AstAggregateEmitter(AstCodeGenerator generator) { g = generator; }

    void declare(DeclarationNodeAST n) {
        Symbol symbol = g.symbol(n);
        Type type = symbol.getType();
        String value;
        if (n.initializer() != null) value = initializer(n.initializer(), type);
        else if (type.isArray()) value = ArrayRuntime.allocateArray(g.c3d, type.getSizes());
        else if (type.isStruct()) value = HeapRuntime.allocateObject(g.c3d, HeapRuntime.definition(g.table, type));
        else return;
        g.memory.store(symbol, value);
    }

    String initializer(AbstractExpressionNodeAST node, Type expected) {
        if (node instanceof ReadExpressionNodeAST) return g.expressions.read(expected);
        if (node instanceof InstanceInitNodeAST instance) return instance(instance, expected);
        if (node instanceof ArrayLiteralNodeAST array) {
            if (expected.isStruct() || expected.isClass()) return objectLiteral(array, expected);
            return arrayLiteral(array, expected.isArray() ? expected : g.type(node));
        }
        return g.emit(node);
    }

    private String arrayLiteral(ArrayLiteralNodeAST n, Type type) {
        type = ArrayLiteral.completeSizes(type, g.type(n));
        String base = ArrayRuntime.allocateArray(g.c3d, type.getSizes());
        fillArray(base, n, type, new int[type.getDimensions()], 0);
        return base;
    }

    private void fillArray(String base, ArrayLiteralNodeAST n, Type type, int[] indices, int dimension) {
        for (int i = 0; i < n.elements().size(); i++) {
            indices[dimension] = i;
            AbstractExpressionNodeAST value = n.elements().get(i);
            if (dimension + 1 < indices.length && value instanceof ArrayLiteralNodeAST row) {
                fillArray(base, row, type, indices, dimension + 1);
            } else {
                String data = initializer(value, type.getElementType());
                ArrayRuntime.writeConstant(g.c3d, base, indices, type.getSizes(), data);
            }
        }
    }

    private String objectLiteral(ArrayLiteralNodeAST n, Type type) {
        Symbol owner = HeapRuntime.definition(g.table, type);
        String base = HeapRuntime.allocateObject(g.c3d, owner);
        fillPositional(base, owner, n.elements());
        return base;
    }

    String instance(InstanceInitNodeAST n, Type expected) {
        if (n.arrayRef() != null) return g.emit(n.arrayRef());
        Symbol owner = g.model.symbolOf(n);
        if (owner == null) owner = HeapRuntime.definition(g.table, expected);
        String base = HeapRuntime.allocateObject(g.c3d, owner);
        fillPositional(base, owner, n.positional());
        for (var field : n.named().entrySet()) {
            Symbol member = owner.getMember(field.getKey());
            String value = initializer(field.getValue(), member.getType());
            g.c3d.emit(QuadrupleOp.HEAP_SET, ArrayRuntime.add(g.c3d, base, member.getOffset()), value, null);
        }
        return base;
    }

    private void fillPositional(String base, Symbol owner, List<AbstractExpressionNodeAST> values) {
        List<Symbol> fields = owner.getMembers().stream().filter(field -> field.getCategory() == SymbolCategory.FIELD).toList();
        for (int i = 0; i < values.size(); i++) {
            Symbol field = fields.get(i);
            String value = initializer(values.get(i), field.getType());
            g.c3d.emit(QuadrupleOp.HEAP_SET, ArrayRuntime.add(g.c3d, base, field.getOffset()), value, null);
        }
    }

    String newObject(NewObjectNodeAST n) {
        Symbol owner = g.table.getClass(n.typeName());
        String base = HeapRuntime.allocateObject(g.c3d, owner);
        g.c3d.emit(QuadrupleOp.PARAM, base, null, null);
        for (AbstractExpressionNodeAST arg : n.arguments()) g.c3d.emit(QuadrupleOp.PARAM, g.memory.capture(g.emit(arg)), null, null);
        g.c3d.emit(QuadrupleOp.CALL, FunctionRuntime.name(n.typeName(), g.symbol(n).getName(), n.arguments().size()),
                String.valueOf(n.arguments().size() + 1), null);
        return base;
    }
}
