package com.compi.backend.ast.codegen;

import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.expr.*;
import com.compi.backend.c3d.*;
import com.compi.backend.symbols.*;
import java.util.ArrayList;
import java.util.List;

/** Direcciones de destinos: cada acceso se evalúa una vez antes de escribirlo. */
final class AstMemoryEmitter {
    record Place(String address, boolean heap) { }
    private final AstCodeGenerator g;
    AstMemoryEmitter(AstCodeGenerator generator) { g = generator; }

    Place place(AbstractExpressionNodeAST n) {
        if (n instanceof IdentifierNodeAST) return symbolPlace(g.symbol(n));
        if (n instanceof MemberAccessNodeAST member) {
            String receiver = g.emit(member.target());
            return new Place(ArrayRuntime.add(g.c3d, receiver, g.symbol(member).getOffset()), true);
        }
        if (n instanceof IndexNodeAST index) return indexPlace(index);
        throw new IllegalStateException("El nodo no representa un destino de escritura: " + n);
    }

    private Place indexPlace(IndexNodeAST n) {
        List<AbstractExpressionNodeAST> indices = new ArrayList<>();
        AbstractExpressionNodeAST root = collectIndices(n, indices);
        String base = capture(g.emit(root));
        List<String> values = indices.stream().map(index -> capture(g.emit(index))).toList();
        return new Place(ArrayRuntime.address(g.c3d, base, values, g.type(root).getSizes()), true);
    }

    private AbstractExpressionNodeAST collectIndices(IndexNodeAST n, List<AbstractExpressionNodeAST> indices) {
        AbstractExpressionNodeAST root = n.target() instanceof IndexNodeAST parent ? collectIndices(parent, indices) : n.target();
        indices.addAll(n.indices());
        return root;
    }

    private Place symbolPlace(Symbol symbol) {
        if (symbol.getCategory() != SymbolCategory.FIELD) return new Place(symbol.getStorageName(), false);
        Symbol receiver = g.table.resolve("this");
        if (receiver == null) throw new IllegalStateException("El atributo requiere un receptor comprobado");
        return new Place(ArrayRuntime.add(g.c3d, receiver.getStorageName(), symbol.getOffset()), true);
    }

    String load(Symbol symbol) { return read(symbolPlace(symbol)); }
    void store(Symbol symbol, String value) { write(symbolPlace(symbol), value); }
    String read(Place place) { return place.heap() ? HeapRuntime.read(g.c3d, place.address()) : place.address(); }
    void write(Place place, String value) {
        if (place.heap()) g.c3d.emit(QuadrupleOp.HEAP_SET, place.address(), value, null);
        else g.c3d.emitAssign(place.address(), value);
    }

    /** Congela valores mutables antes de evaluar otro operando con efectos laterales. */
    String capture(String value) {
        if (!value.startsWith("v_") && !value.startsWith("g_")) return value;
        String temp = g.c3d.newTemp();
        g.c3d.emitAssign(temp, value);
        return temp;
    }
}
