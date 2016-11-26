package io.github.satr.jweb.webshop.sm.models;

import io.github.satr.jweb.components.entities.Product;

public class EditableProduct extends Product {

    public EditableProduct copyTo(Product product) {
        product.setId(getId());
        product.setName(getName());
        product.setPrice(getPrice());
//            product.setAmount(getAmount());
        return this;
    }

    public EditableProduct copyFrom(Product product) {
        setId(product.getId());
        setName(product.getName());
        setPrice(product.getPrice());
//            setAmount(product.getAmount());
        return this;
    }
}
