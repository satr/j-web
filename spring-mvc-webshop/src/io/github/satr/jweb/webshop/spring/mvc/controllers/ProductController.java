package io.github.satr.jweb.webshop.spring.mvc.controllers;

import io.github.satr.jweb.components.entities.Product;
import io.github.satr.jweb.components.repositories.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController() {
        productRepository = new ProductRepository();
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String getList(@RequestParam(value="name", required = false, defaultValue = "") String name, Model model) {
        ArrayList<String> errors = new ArrayList<>();
        List<Product> productList = null;
        try {
            productList = productRepository.getList();
        } catch (SQLException e) {
            errors.add(e.getMessage());//TODO - user-friendly message and system log
            productList = new ArrayList<>();
        }
        model.addAttribute("errors", errors);
        model.addAttribute("productList", productList);

        return "product/ListView";
    }
}
