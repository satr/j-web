package io.github.satr.jweb.webshop.sm.models;

public class OperationValueResult<T> extends OperationResult {
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
