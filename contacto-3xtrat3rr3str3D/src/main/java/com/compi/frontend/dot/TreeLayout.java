package com.compi.frontend.dot;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;

/**
 * Calculo de posiciones de un arbol DOT y dibujo del mismo.
 *
 * Usa un reparto clasico de subarboles: las hojas se colocan a la misma altura,
 * el padre se centra sobre el intervalo que ocupan sus hijos y despues se
 * separa cada nivel. Ese reparto deja las hojas arriba y la raiz en el fondo, asi
 * que al terminar se refleja en vertical ({@link #flipAndNormalize}) y el arbol
 * se lee como es habitual: raiz arriba y hijos hacia abajo. El resultado es un
 * arbol legible sin depender de Graphviz.
 */
public class TreeLayout {

    private static final int H_GAP = 14;
    private static final int V_GAP = 44;
    private static final int PADDING = 12;
    private static final int MAX_LABEL_WIDTH = 190;

    private final Map<String, int[]> positions = new HashMap<>();
    private final Map<String, Dimension> sizes = new HashMap<>();

    private int maxWidth;
    private int maxHeight;
    private int leafCursor;

    /**
     * Calcula las coordenadas de cada nodo.
     *
     * @param model grafo ya parseado
     * @param font  fuente con la que se mediran las etiquetas
     */
    public void compute(DotModel model, Font font) {
        positions.clear();
        sizes.clear();
        leafCursor = 0;
        if (model == null || model.isEmpty()) {
            maxWidth = 0;
            maxHeight = 0;
            return;
        }
        JPanel measure = new JPanel();
        FontMetrics fm = measure.getFontMetrics(font);

        String root = model.root();
        place(model, root, fm);
        flipAndNormalize(fm);
    }

    /**
     * Da la vuelta al reparto y calcula el tamaño total.
     *
     * <p>El reparto de {@link #place} deja las hojas arriba y a la raiz en el
     * fondo, que se lee al reves. Como todas las cajas miden lo mismo, basta
     * con reflejar el eje vertical (y' = maxY - y) para dejar la raiz arriba y
     * los hijos colgando hacia abajo, y despues se separa del margen izquierdo.
     * El ancho se mide con el borde derecho de la caja mas a la derecha, que es
     * lo que necesita el lienzo para poder desplazarse en horizontal.</p>
     */
    private void flipAndNormalize(FontMetrics fm) {
        int maxY = 0;
        int minX = Integer.MAX_VALUE;
        int rightX = 0;
        for (Map.Entry<String, int[]> entry : positions.entrySet()) {
            int[] pos = entry.getValue();
            Dimension size = sizes.get(entry.getKey());
            maxY = Math.max(maxY, pos[1]);
            minX = Math.min(minX, pos[0]);
            rightX = Math.max(rightX, pos[0] + size.width);
        }
        if (minX == Integer.MAX_VALUE) {
            minX = 0;
        }

        for (int[] pos : positions.values()) {
            pos[1] = maxY - pos[1];
        }
        int shift = PADDING - minX;
        for (int[] pos : positions.values()) {
            pos[0] += shift;
        }

        maxWidth = Math.max(1, rightX - minX) + PADDING * 2;
        maxHeight = maxY + nodeHeight(fm) + PADDING * 2;
    }

    // ====================== Reparto ======================

    private int place(DotModel model, String id, FontMetrics fm) {
        DotModel.DotNode node = model.node(id);
        if (node == null) {
            return 0;
        }
        Dimension size = sizeOf(node, fm);
        sizes.put(id, size);

        List<String> children = dedupe(model.children(id));
        if (children.isEmpty()) {
            int x = placeLeaf(id, size);
            positions.put(id, new int[]{x, 0});
            return x + size.width / 2;
        }

        int[] centers = new int[children.size()];
        for (int i = 0; i < children.size(); i++) {
            String child = children.get(i);
            place(model, child, fm);
            int[] pos = positions.get(child);
            centers[i] = pos[0] + sizes.get(child).width / 2;
        }
        int minCenter = centers[0];
        for (int c : centers) {
            minCenter = Math.min(minCenter, c);
        }
        int maxCenter = centers[0];
        for (int c : centers) {
            maxCenter = Math.max(maxCenter, c);
        }
        int center = (minCenter + maxCenter) / 2;

        int level = 0;
        for (String child : children) {
            level = Math.max(level, positions.get(child)[1]);
        }
        int y = level + nodeHeight(fm) + V_GAP;

        int x = center - size.width / 2;
        // Evita solapar al nodo con el primero de sus hijos.
        positions.put(id, new int[]{x, y});
        return center;
    }

    private int placeLeaf(String id, Dimension size) {
        int x = leafCursor;
        leafCursor += size.width + H_GAP;
        positions.put(id, new int[]{x, 0});
        return x + size.width / 2;
    }

    private Dimension sizeOf(DotModel.DotNode node, FontMetrics fm) {
        String label = node.label();
        int w = fm.stringWidth(label);
        if (w > MAX_LABEL_WIDTH) {
            w = MAX_LABEL_WIDTH;
        }
        return new Dimension(Math.max(34, w + 20), nodeHeight(fm));
    }

    private static int nodeHeight(FontMetrics fm) {
        return fm.getHeight() + 10;
    }

    private static List<String> dedupe(List<String> ids) {
        return new ArrayList<>(new LinkedHashSet<>(ids));
    }

    // ====================== Consulta ======================

    public int[] positionOf(String id) {
        return positions.get(id);
    }

    public Dimension sizeOf(String id) {
        return sizes.get(id);
    }

    public Dimension bounds() {
        return new Dimension(maxWidth, maxHeight);
    }

    public Set<String> nodeIds() {
        return positions.keySet();
    }

    /**
     * Padre de un nodo segun la posicion horizontal: el nodo mas cercano por
     * encima cuya caja solapa verticalmente.
     */
    public String findParent(DotModel model, String id) {
        int[] child = positions.get(id);
        if (child == null) {
            return null;
        }
        String best = null;
        int bestDistance = Integer.MAX_VALUE;
        for (String[] edge : model.edges()) {
            if (!edge[1].equals(id)) {
                continue;
            }
            int[] parent = positions.get(edge[0]);
            if (parent == null) {
                continue;
            }
            int distance = child[1] - parent[1];
            if (distance < 0) {
                continue;
            }
            if (distance < bestDistance) {
                bestDistance = distance;
                best = edge[0];
            }
        }
        return best;
    }
}
