package com.compi.backend;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StackAction {

    SHIFT("shift"),
    REDUCE("reduce"),
    NEUTRAL("none");

    private final String tag;
}
