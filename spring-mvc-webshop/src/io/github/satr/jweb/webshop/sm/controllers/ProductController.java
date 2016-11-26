package io.github.satr.jweb.webshop.sm.controllers;

import io.github.satr.jweb.components.entities.Product;
import io.github.satr.jweb.components.helpers.StringHelper;
import io.github.satr.jweb.components.repositories.ProductRepository;
import io.github.satr.jweb.webshop.sm.editable.EditableProduct;
import io.github.satr.jweb.webshop.sm.models.OperationResult;
import io.github.satr.jweb.webshop.sm.models.OperationValueResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String getList(Model model) {
        ArrayList<String> errors = new ArrayList<>();
        List<Product> productList = null;
        try {
            productList = productRepository.getList();
        } catch (SQLException e) {
            errors.add(e.getMessage());//TODO - user-friendly message and system log
            productList = new ArrayList<>();
        }
        model.addAttribute(ModelAttr.ERRORS, errors);
        model.addAttribute(ModelAttr.PRODUCT_LIST, productList);
        return View.LIST;
    }

    @RequestMapping(value = "/product/detail", method = RequestMethod.GET)
    public String detail(@RequestParam(value="id", required = true, defaultValue = "-1")int id, Model model) {
        OperationValueResult<Product> result = getProductBy(id);
        if(result.isFailed()) {
            model.addAttribute(ModelAttr.ERRORS, result.getErrors());
            return View.ERROR;
        }
        model.addAttribute(ModelAttr.PRODUCT, new EditableProduct().copyFrom(result.getValue()));
        return View.DETAIL;
    }

    @RequestMapping(value = "/product/add", method = RequestMethod.GET)
    public String addBegin(Model model) {
        model.addAttribute(ModelAttr.ACTION, Action.ADD);
        model.addAttribute(ModelAttr.PRODUCT, new EditableProduct());
        return View.EDIT;
    }

    @RequestMapping(value = "/product/edit", method = RequestMethod.GET)
    public String editBegin(@RequestParam(value="id", required = true, defaultValue = "-1")int id, Model model) {
        OperationValueResult<Product> result = getProductBy(id);
        if(result.isFailed()) {
            model.addAttribute(ModelAttr.ERRORS, result.getErrors());
            return View.ERROR;
        }

        model.addAttribute(ModelAttr.PRODUCT, new EditableProduct().copyFrom(result.getValue()));
        model.addAttribute(ModelAttr.ACTION, Action.EDIT);
        return View.EDIT;
    }

    @RequestMapping(value = "/product/edit", method = RequestMethod.POST)
    public String editCommit(@ModelAttribute EditableProduct editableProduct, BindingResult bindingResult, Model model) {
        OperationResult operationResult = validateEditableProduct(editableProduct, bindingResult, model);
        if (operationResult.isFailed())
            return getEditView(Action.EDIT, editableProduct, operationResult, model);

        OperationValueResult<Product> productResult = getProductBy(editableProduct.getId());
        if(productResult.isFailed()) {
            model.addAttribute(ModelAttr.ERRORS, productResult.getErrors());
            return View.ERROR;
        }
        Product product = productResult.getValue();
        editableProduct.copyTo(product);
        OperationResult saveResult = saveProduct(product);
        if(saveResult.isFailed()) {
            model.addAttribute(ModelAttr.ERRORS, saveResult.getErrors());//TODO - user-friendly message and system log
            return View.ERROR;
        }
        model.addAttribute(ModelAttr.PRODUCT, product);
        return View.DETAIL;
    }

    @RequestMapping(value = "/product/add", method = RequestMethod.POST)
    public String addCommit(@ModelAttribute EditableProduct editableProduct, BindingResult bindingResult, Model model) {
        OperationResult operationResult = validateEditableProduct(editableProduct, bindingResult, model);
        if (operationResult.isFailed())
            return getEditView(Action.ADD, editableProduct, operationResult, model);

        Product product = new Product();
        editableProduct.copyTo(product);
        OperationResult saveResult = saveProduct(product);
        if(saveResult.isFailed()) {
            model.addAttribute(ModelAttr.ERRORS, saveResult.getErrors());//TODO - user-friendly message and system log
            return View.ERROR;
        }
        model.addAttribute(ModelAttr.PRODUCT, product);
        return View.DETAIL;
    }

    private String getEditView(String action, @ModelAttribute EditableProduct editableProduct, OperationResult operationResult, Model model) {
        model.addAttribute(ModelAttr.ERRORS, operationResult.getErrors());
        model.addAttribute(ModelAttr.PRODUCT, editableProduct);
        model.addAttribute(ModelAttr.ACTION, action);
        return View.EDIT;
    }

    private OperationResult validateEditableProduct(EditableProduct editableProduct, BindingResult bindingResult, Model model) {
        OperationResult operationResult = new OperationResult();

        for (ObjectError err: bindingResult.getAllErrors())
            operationResult.addError("Invalid value in %s", err.getObjectName());

        if(StringHelper.isEmptyOrWhitespace(editableProduct.getName()))
            operationResult.addError("Missed Name");

        if(editableProduct.getPrice() == null || editableProduct.getPrice() < 0)
            operationResult.addError("Invalid Price");

        return operationResult;
    }

    private OperationResult saveProduct(Product product) {
        OperationResult result = new OperationResult();
        try {
            productRepository.save(product);
        } catch (SQLException e) {
            result.addError(e.getMessage());
        }
        return result;
    }

    private OperationValueResult<Product> getProductBy(@RequestParam(value = "id", required = true, defaultValue = "-1") int id) {
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

    //-- Constants --
    private class View {
        public static final String EDIT = "product/EditView";
        public static final String DETAIL = "product/DetailView";
        public static final String LIST = "product/ListView";
        public static final String ERROR = "ErrorView";
    }

    private class ModelAttr {
        public final static String PRODUCT = "product";
        public final static String PRODUCT_LIST = "productList";
        public static final String ACTION = "action";
        public static final String ERRORS = "errors";
    }
    
    private class Action {

        public static final String ADD = "add";
        public static final String EDIT = "edit";
    }

}
