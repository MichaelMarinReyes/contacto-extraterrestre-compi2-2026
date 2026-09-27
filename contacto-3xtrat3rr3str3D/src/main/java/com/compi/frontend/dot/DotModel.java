package com.compi.frontend.dot;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DotModel {

    private static final Pattern NODE = Pattern.compile("^\\s*(n\\d+)\\s*\\[\\s*label\\s*=\\s*\"(.*)\"\\s*\\]\\s*;?\\s*$");
    private static final Pattern EDGE = Pattern.compile("^\\s*(n\\d+)\\s*->\\s*(n\\d+)\\s*;?\\s*$");
    private static final Pattern UNESCAPED_QUOTE = Pattern.compile("\\\\\"");

    public record DotNode(String id, String label) {
    }

    private final Map<String, DotNode> nodes = new LinkedHashMap<>();
    private final List<String[]> edges = new ArrayList<>();
    private final Map<String, String> parentOf = new LinkedHashMap<>();

    private DotModel() {
    }

    public static DotModel parse(String dot) {
        DotModel model = new DotModel();
        if (dot == null || dot.isBlank()) {
            return model;
        }
        for (String rawLine : dot.split("\\R")) {
            String line = rawLine.trim();
            if (line.isEmpty() || line.startsWith("//")) {
                continue;
            }
            Matcher edge = EDGE.matcher(line);
            if (edge.matches()) {
                String from = edge.group(1);
                String to = edge.group(2);
                model.edges.add(new String[]{from, to});
                model.parentOf.putIfAbsent(to, from);
                continue;
            }
            Matcher node = NODE.matcher(line);
            if (node.matches()) {
                model.nodes.put(node.group(1), new DotNode(node.group(1), unescape(node.group(2))));
            }
        }
        return model;
    }

    private static String unescape(String value) {
        return UNESCAPED_QUOTE.matcher(value).replaceAll("\"");
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public DotNode node(String id) {
        return nodes.get(id);
    }

    public Map<String, DotNode> nodes() {
        return nodes;
    }

    public int nodeCount() {
        return nodes.size();
    }

    public List<String[]> edges() {
        return edges;
    }

    public List<String> children(String id) {
        List<String> result = new ArrayList<>();
        for (String[] e : edges) {
            if (e[0].equals(id)) {
                result.add(e[1]);
            }
        }
        return result;
    }

    public String root() {
        for (String id : nodes.keySet()) {
            if (!parentOf.containsKey(id)) {
                return id;
            }
        }
        return nodes.keySet().stream().findFirst().orElse(null);
    }

    public int depth() {
        String r = root();
        return r == null ? 0 : depth(r, 0);
    }

    private int depth(String id, int level) {
        List<String> kids = children(id);
        if (kids.isEmpty()) {
            return level;
        }
        int max = level;
        for (String kid : kids) {
            max = Math.max(max, depth(kid, level + 1));
        }
        return max;
    }
}
