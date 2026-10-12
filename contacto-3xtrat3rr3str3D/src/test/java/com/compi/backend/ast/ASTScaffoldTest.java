package com.compi.backend.ast;

import com.compi.backend.ast.exprressions.IdentifierNodeAST;
import com.compi.backend.ast.exprressions.LiteralKind;
import com.compi.backend.ast.exprressions.LiteralNodeAST;
import com.compi.backend.ast.statements.AssignmentNodeAST;
import com.compi.backend.ast.statements.ProgramNodeAST;
import com.compi.backend.utils.ASTDotGenerator;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Andamiaje del AST propio (Fase 0): nodos, doble despacho y generador DOT. */
class ASTScaffoldTest {

    @Test
    void nodosConDobleDespachoYChildren() {
        LiteralNodeAST literal = new LiteralNodeAST(1, 8, 42, LiteralKind.INTEGER);
        IdentifierNodeAST objetivo = new IdentifierNodeAST(1, 0, "x", null);
        AssignmentNodeAST asignacion = new AssignmentNodeAST(1, 0, objetivo, "=", literal);

        assertEquals(2, asignacion.children().size());
        assertEquals(objetivo, asignacion.children().get(0));
        assertEquals(literal, asignacion.children().get(1));
        assertEquals(1, asignacion.line());

        // El visitante por defecto (vacío) no debe lanzar.
        ASTVisitor<Void> visitante = new AbstractASTVisitor<>() {
        };
        asignacion.accept(visitante);
    }

    @Test
    void dotGeneratorEmiteGrafoConNodosYAristas() {
        LiteralNodeAST literal = new LiteralNodeAST(1, 10, 42, LiteralKind.INTEGER);
        IdentifierNodeAST objetivo = new IdentifierNodeAST(1, 0, "x", null);
        AssignmentNodeAST asignacion = new AssignmentNodeAST(1, 0, objetivo, "=", literal);
        ProgramNodeAST programa = new ProgramNodeAST(1, 0,
                List.of(), List.of(), List.of(), List.of(asignacion));

        String dot = new ASTDotGenerator().build(programa, "demo");

        assertNotNull(dot);
        assertTrue(dot.contains("digraph demo"));
        assertTrue(dot.contains("Program"));
        assertTrue(dot.contains("Assignment"));
        assertTrue(dot.contains("Identifier"));
        assertTrue(dot.contains("Literal"));
        assertTrue(dot.contains("->"));
    }
}