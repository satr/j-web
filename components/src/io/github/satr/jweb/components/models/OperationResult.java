package io.github.satr.jweb.components.models;

import java.util.ArrayList;

public class OperationResult {
    private final ArrayList<String> errors = new ArrayList<>();

    public OperationResult addError(String format, Object... args) {
        errors.add(String.format(format, args));
        return this;
    }

    public boolean isSucceeded() {
        return errors.isEmpty();
    }

    public boolean isFailed() {
        return !isSucceeded();
    }

    public ArrayList<String> getErrors() {
        return errors;
    }
}
