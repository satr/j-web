package io.github.satr.jweb.webshop.sm.controllers;

import io.github.satr.jweb.components.entities.Account;
import io.github.satr.jweb.components.entities.CartItem;
import io.github.satr.jweb.components.entities.Product;
import io.github.satr.jweb.components.helpers.StringHelper;
import io.github.satr.jweb.components.models.OperationResult;
import io.github.satr.jweb.components.models.OperationValueResult;
import io.github.satr.jweb.frontend.models.CartModel;
import io.github.satr.jweb.webshop.sm.helpers.Env;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
public class CartController {

    private final CartModel cartModel;

    public CartController() {
        cartModel = new CartModel();
    }

    @RequestMapping(value = "/buy", method = RequestMethod.GET)
    public String buy(HttpServletRequest request, @RequestAttribute int sku, Model model) {
        Account account = Env.getAccountFromSession(request);
        if(account == null) {
            return "account/LoginView";//TODO - do not request user to be logged in to buy products
        }

        OperationValueResult<Product> getProductResult = cartModel.getProduct(sku);
        if(getProductResult.isFailed()) {
            model.addAttribute(Env.ModelAttr.ERRORS);
            return Env.View.ERROR;
        }

        OperationResult saveCartResult = cartModel.saveCart(account, getProductResult.getValue());

        if(saveCartResult.isFailed()) {
            model.addAttribute(Env.ModelAttr.ERRORS, saveCartResult.getErrors());
            return Env.View.ERROR;
        }

        return showCart(request, model);
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public String showCart(HttpServletRequest request, Model model) {
        Account account = Env.getAccountFromSession(request);
        if(account == null) {
            return "account/LoginView";//TODO - do not request user to be logged in to buy products
        }

        OperationValueResult<List<CartItem>> getItemsResult = cartModel.getCartItems(account.getId());
        if(getItemsResult.isFailed()) {
            model.addAttribute(Env.ModelAttr.ERRORS, getItemsResult.getErrors());
            return Env.View.ERROR;
        }
        List<CartItem> cartItems = getItemsResult.getValue();
        double sum = 0;
        double discount = 0;
        for (CartItem cartItem: cartItems) {
            sum += cartItem.getPrice();
            discount += cartItem.getDiscount();
        }
        if(sum < discount) {
            sum = 0;
            discount = sum;
        }
        model.addAttribute(ModelAttr.CART_ITEMS, cartItems);
        model.addAttribute(Env.ModelAttr.SUM, sum);
        model.addAttribute(Env.ModelAttr.DISCOUNT, discount);
        model.addAttribute(Env.ModelAttr.TOTAL, sum - discount);
        return CartController.View.CartView;
    }

    @RequestMapping(value = "/cart/update", method = RequestMethod.POST)
    public String updateCart(HttpServletRequest request, @RequestParam String action, Model model) {
        if(FormActions.UPDATE.equals(action))
            return updateCartItems(request, model);
        if(FormActions.REMOVE.equals(action))
            return removeProductFromCart(request, model);

        return showCart(request, model);
    }

    private String removeProductFromCart(HttpServletRequest request, Model model) {
        String productIdValue = request.getParameter(FormParams.TARGET_PRODUCT_ID);
        if(!StringHelper.isInteger(productIdValue))
            return Env.showError(model, "Missed or invalid value in %s", FormParams.TARGET_PRODUCT_ID);

        Account account = Env.getAccountFromSession(request);
        if(account == null)
            return "account/LoginView";//TODO - do not request user to be logged in to buy products

        OperationValueResult<List<CartItem>> getCartItemsResult = cartModel.getCartItems(account.getId());
        if(getCartItemsResult.isFailed())
            return Env.showErrors(model, getCartItemsResult);

        int productId = Integer.parseInt(productIdValue);
        OperationResult removeCartItemResult = cartModel.removeCartItems(getCartItemsResult.getValue(), productId);

        if(removeCartItemResult.isFailed())
            return Env.showErrors(model,removeCartItemResult);

        return showCart(request, model);
    }

    private String updateCartItems(HttpServletRequest request, Model model) {
        String[] productIdList = request.getParameterValues(FormParams.PRODUCT_ID);
        String[] amountList = request.getParameterValues(FormParams.AMOUNT);

        OperationValueResult<HashMap<Integer, Integer>> productAmountMapResult = cartModel.getProductAmountMapFrom(productIdList, amountList);
        if(productAmountMapResult.isFailed())
            return Env.showErrors(model, productAmountMapResult);

        Account account = Env.getAccountFromSession(request);
        if(account == null)
            return "account/LoginView";//TODO - do not request user to be logged in to buy products

        OperationValueResult<List<CartItem>> getCartItemsResult = cartModel.getCartItems(account.getId());
        if(getCartItemsResult.isFailed())
            return Env.showErrors(model, getCartItemsResult);

        OperationResult updateCartResult = cartModel.updateCart(productAmountMapResult, getCartItemsResult);
        if(updateCartResult.isFailed())
            return Env.showErrors(model, updateCartResult);

        return showCart(request, model);
    }

    //----- Constants -------
    private class View {

        final static String CartView = "cart/ListView";
    }

    private class ModelAttr {
        final static String CART_ITEMS = "cartItems";
    }

    private class FormParams {
        final static String TARGET_PRODUCT_ID = "targetProductId";
        final static String AMOUNT = "amount";
        final static String PRODUCT_ID = "productId";
    }
    private class FormActions {
        final static String UPDATE = "Update";
        final static String REMOVE = "Remove";
    }
}
