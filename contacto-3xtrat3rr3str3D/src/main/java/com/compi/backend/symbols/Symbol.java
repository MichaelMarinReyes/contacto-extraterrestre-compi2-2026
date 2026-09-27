package com.compi.backend.symbols;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Symbol {
    private final String name;
    private final Type type;
    private final SymbolCategory category;
    private int offset;
    private boolean isGlobal;
    private boolean isByReference;

    private List<Symbol> parameters = new ArrayList<>();
    private Type returnType;

    private List<Symbol> members = new ArrayList<>();

    private int line = -1;
    private int column = -1;
    private String scope;
    private String language;
    private String sourceFile;

    private boolean working;

    public Symbol(String name, Type type, SymbolCategory category) {
        this.name = name;
        this.type = type;
        this.category = category;
    }

    public Symbol(String name, Type type, SymbolCategory category, int offset, boolean isGlobal) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.offset = offset;
        this.isGlobal = isGlobal;
    }

    public void addParameter(Symbol param) {
        this.parameters.add(param);

        param.setScopeIfAbsent(name);
    }

    public void addMember(Symbol member) {
        this.members.add(member);

        member.setScopeIfAbsent(name);
    }

    public Symbol getMember(String memberName) {
        for (Symbol s : members) {
            if (s.getName().equals(memberName)) return s;
        }
        return null;
    }

    public Symbol inFile(String fileName) {
        this.sourceFile = fileName;
        return this;
    }

    public Symbol at(ParserRuleContext ctx) {
        if (ctx != null && ctx.getStart() != null) {
            this.line = ctx.getStart().getLine();
            this.column = ctx.getStart().getCharPositionInLine() + 1;
        }
        return this;
    }

    public void setScopeIfAbsent(String scopeName) {
        if (this.scope == null) {
            this.scope = scopeName;
        }
    }

    public Symbol inLanguage(String languageId) {
        this.language = languageId;
        return this;
    }

    public Symbol markWorking() {
        this.working = true;
        return this;
    }

    @Override
    public String toString() {
        return String.format("Symbol(%s, %s, %s, offset=%d, isGlobal=%s, ambito=%s, linea=%d)",
                name, type, category, offset, isGlobal, scope, line);
    }
}
