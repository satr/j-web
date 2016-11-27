package io.github.satr.jweb.webshop.sm.models;

import io.github.satr.jweb.components.entities.Product;

public class EditableProduct extends Product {

    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public EditableProduct copyTo(Product product) {
        product.setId(getId());
        product.setName(getName());
        product.setPrice(getPrice());
        product.getStock().setAmount(getAmount());
        return this;
    }

    public EditableProduct copyFrom(Product product) {
        setId(product.getId());
        setName(product.getName());
        setPrice(product.getPrice());
        setAmount(product.getStock().getAmount());
        return this;
    }
}
