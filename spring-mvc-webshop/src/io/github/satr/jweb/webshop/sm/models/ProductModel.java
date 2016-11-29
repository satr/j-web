package io.github.satr.jweb.webshop.sm.models;

import io.github.satr.jweb.components.entities.Product;
import io.github.satr.jweb.components.entities.Stock;
import io.github.satr.jweb.components.helpers.StringHelper;
import io.github.satr.jweb.components.models.OperationResult;
import io.github.satr.jweb.components.models.OperationValueResult;
import io.github.satr.jweb.components.repositories.ProductRepository;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductModel {
    private final ProductRepository productRepository;

    public ProductModel() {
        productRepository = new ProductRepository();
    }

    public OperationResult saveProduct(Product product) {
        OperationResult result = new OperationResult();
        try {
            productRepository.save(product);
        } catch (SQLException e) {
            result.addError(e.getMessage());
        }
        return result;
    }

    public OperationValueResult<Product> getProductBy(int id) {
        OperationValueResult<Product> result = new OperationValueResult<>();
        try {
            result.setValue(productRepository.get(id));
            if(result.getValue() == null)
                result.addError("Product not found by SKU");
        } catch (SQLException e) {
            result.addError(e.getMessage());//TODO - user-friendly message and system log
        }
        return result;
    }

    public OperationResult validateEditableProduct(EditableProduct editableProduct, BindingResult bindingResult) {
        OperationResult operationResult = new OperationResult();

        for (ObjectError err: bindingResult.getAllErrors())
            operationResult.addError("Invalid value in %s", err.getObjectName());

        if(StringHelper.isEmptyOrWhitespace(editableProduct.getName()))
            operationResult.addError("Missed Name");

        if(editableProduct.getPrice() == null || editableProduct.getPrice() < 0)
            operationResult.addError("Invalid Price");

        if(editableProduct.getAmount() < 0)
            operationResult.addError("Invalid Amount");

        return operationResult;
    }

    public Product createProduct() {
        Product product = new Product();
        Stock stock = new Stock();
        product.setStock(stock);
        stock.setProduct(product);
        stock.setAmount(0);
        return product;
    }

    public OperationValueResult<List<Product>> getProducts() {
        OperationValueResult<List<Product>> productListResult = new OperationValueResult<>();
        try {
            productListResult.setValue(productRepository.getList());
        } catch (SQLException e) {
            productListResult.setValue(new ArrayList<>());
            productListResult.addError(e.getMessage());//TODO - user-friendly message and system log
        }
        return productListResult;
    }
}
