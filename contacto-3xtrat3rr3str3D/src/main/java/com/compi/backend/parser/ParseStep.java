package com.compi.backend.parser;

import com.compi.backend.StackAction;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ParseStep {
    public static final String MARK_START = "$";
    private final int number;
    private final StackAction type;
    private final String symbol;
    private final String terminal;
    private final int line;
    private final int column;
    private final boolean invalid;
    private final List<String> stack;
    private final List<String> consumed;

    @Override
    public String toString() {
        String detail;
        if (type == StackAction.SHIFT) {
            detail = terminal == null ? symbol : terminal + ": " + symbol;
            if (invalid) {
                detail += "   (no encaja en la regla)";
            }
            detail += "   (" + line + ":" + column + ")";
        } else {
            detail = symbol + "   (retira " + consumed.size() + ": "
                    + String.join(", ", consumed) + ")";
        }
        return String.format("[Paso %3d] %-6s %s", number, type.getTag(), detail);
    }
}
