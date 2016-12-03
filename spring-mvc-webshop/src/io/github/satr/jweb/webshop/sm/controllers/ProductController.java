package io.github.satr.jweb.webshop.sm.controllers;

import io.github.satr.jweb.components.entities.Product;
import io.github.satr.jweb.components.models.OperationResult;
import io.github.satr.jweb.components.models.OperationValueResult;
import io.github.satr.jweb.frontend.models.EditableProduct;
import io.github.satr.jweb.frontend.models.ProductModel;
import io.github.satr.jweb.webshop.sm.helpers.Env;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {

    private final ProductModel productModel;

    public ProductController() {
        productModel = new ProductModel();
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String getList(Model model) {
        OperationValueResult<List<Product>> productListResult = productModel.getProducts();
        model.addAttribute(ModelAttr.ERRORS, productListResult.getErrors());
        model.addAttribute(ModelAttr.PRODUCT_LIST, productListResult.getValue());
        return View.LIST;
    }

    @RequestMapping(value = "/product/detail", method = RequestMethod.GET)
    public String detail(@RequestParam(value="id", required = true, defaultValue = "-1")int id, Model model) {
        OperationValueResult<Product> result = productModel.getProductBy(id);
        if(result.isFailed()) {
            model.addAttribute(ModelAttr.ERRORS, result.getErrors());
            return View.ERROR;
        }
        model.addAttribute(ModelAttr.PRODUCT, result.getValue());
        return View.DETAIL;
    }

    @RequestMapping(value = "/product/add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(ModelAttr.ACTION, Action.ADD);
        model.addAttribute(ModelAttr.PRODUCT, new EditableProduct());
        return View.EDIT;
    }

    @RequestMapping(value = "/product/edit", method = RequestMethod.GET)
    public String edit(@RequestParam(value="id", required = true, defaultValue = "-1")int id, Model model) {
        OperationValueResult<Product> result = productModel.getProductBy(id);
        if(result.isFailed()) {
            model.addAttribute(ModelAttr.ERRORS, result.getErrors());
            return View.ERROR;
        }

        model.addAttribute(ModelAttr.PRODUCT, new EditableProduct().copyFrom(result.getValue()));
        model.addAttribute(ModelAttr.ACTION, Action.EDIT);
        return View.EDIT;
    }

    @RequestMapping(value = "/product/edit", method = RequestMethod.POST)
    public String processEdit(@ModelAttribute EditableProduct editableProduct, BindingResult bindingResult, Model model) {
        OperationResult operationResult = productModel.validateEditableProduct(editableProduct, Env.getErrors(bindingResult));
        if (operationResult.isFailed())
            return getEditView(Action.EDIT, editableProduct, operationResult, model);

        OperationValueResult<Product> productResult = productModel.getProductBy(editableProduct.getId());
        if(productResult.isFailed()) {
            model.addAttribute(ModelAttr.ERRORS, productResult.getErrors());
            return View.ERROR;
        }
        Product product = productResult.getValue();
        editableProduct.copyTo(product);
        OperationResult saveResult = productModel.saveProduct(product);
        if(saveResult.isFailed()) {
            model.addAttribute(ModelAttr.ERRORS, saveResult.getErrors());//TODO - user-friendly message and system log
            return View.ERROR;
        }
        model.addAttribute(ModelAttr.PRODUCT, product);
        return View.DETAIL;
    }

    @RequestMapping(value = "/product/add", method = RequestMethod.POST)
    public String processAdd(@ModelAttribute EditableProduct editableProduct, BindingResult bindingResult, Model model) {
        OperationResult operationResult = productModel.validateEditableProduct(editableProduct, Env.getErrors(bindingResult));
        if (operationResult.isFailed())
            return getEditView(Action.ADD, editableProduct, operationResult, model);

        Product product = productModel.createProduct();
        editableProduct.copyTo(product);
        OperationResult saveResult = productModel.saveProduct(product);
        if(saveResult.isFailed()) {
            model.addAttribute(ModelAttr.ERRORS, saveResult.getErrors());//TODO - user-friendly message and system log
            return View.ERROR;
        }
        model.addAttribute(ModelAttr.PRODUCT, product);
        return View.DETAIL;
    }

    private String getEditView(String action, EditableProduct editableProduct, OperationResult operationResult, Model model) {
        model.addAttribute(ModelAttr.ERRORS, operationResult.getErrors());
        model.addAttribute(ModelAttr.PRODUCT, editableProduct);
        model.addAttribute(ModelAttr.ACTION, action);
        return View.EDIT;
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
