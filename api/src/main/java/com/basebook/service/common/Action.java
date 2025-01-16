package com.basebook.service.common;

public interface Action<I,O> {
    O performAction(I input);
}
