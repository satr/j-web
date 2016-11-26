package io.github.satr.jweb.webshop.sm.models;

import java.util.ArrayList;
import java.util.List;

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

    public List<String> getErrors() {
        return errors;
    }
}
