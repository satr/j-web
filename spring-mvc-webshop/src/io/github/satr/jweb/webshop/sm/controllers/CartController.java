package io.github.satr.jweb.webshop.sm.controllers;

import io.github.satr.jweb.components.entities.Account;
import io.github.satr.jweb.components.entities.CartItem;
import io.github.satr.jweb.components.entities.Product;
import io.github.satr.jweb.components.helpers.StringHelper;
import io.github.satr.jweb.components.models.OperationValueResult;
import io.github.satr.jweb.components.repositories.CartRepository;
import io.github.satr.jweb.components.repositories.CartStatusRepository;
import io.github.satr.jweb.components.repositories.ProductRepository;
import io.github.satr.jweb.webshop.sm.helpers.Env;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class CartController {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartStatusRepository cartStatusRepository;

    public CartController() {
        productRepository = new ProductRepository();
        cartRepository = new CartRepository();
        cartStatusRepository = new CartStatusRepository();
    }

    @RequestMapping(value = "/buy", method = RequestMethod.GET)
    public String buy(HttpServletRequest request, @RequestAttribute int sku, Model model) {
        Account account = Env.getAccountFromSession(request);
        if(account == null) {
            return "account/LoginView";//TODO - do not request user to be logged in to buy products
        }

        OperationValueResult<Product> getProductResult = getProduct(sku);
        if(getProductResult.isFailed()) {
            model.addAttribute(Env.ModelAttr.ERRORS);
            return Env.View.ERROR;
        }

        try {
            CartItem cartItem = createCartItemFor(getProductResult.getValue(), account);
            cartItem.setUpdatedOn(Env.getTimestamp());
            cartRepository.save(cartItem);
        } catch (SQLException e) {
            model.addAttribute(Env.ModelAttr.ERRORS, e.getMessage());
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

        OperationValueResult<List<CartItem>> getItemsResult = getCartItems(account.getId());
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

        OperationValueResult<List<CartItem>> getCartItemsResult = getCartItems(account.getId());
        if(getCartItemsResult.isFailed())
            return Env.showErrors(model, getCartItemsResult);

        CartItem cartItemToRemove = getCartItemBy(getCartItemsResult.getValue(), Integer.parseInt(productIdValue));
        try {
            cartRepository.remove(cartItemToRemove);
        } catch (Exception e) {
            return Env.showError(model, e.getMessage());//TODO -  wrap with user-friendly message and log
        }

        return showCart(request, model);
    }

    private CartItem getCartItemBy(List<CartItem> cartItems, Integer productId) {
        for (CartItem cartItem: cartItems) {
            if(cartItem.getProduct().getId() == productId)
                return cartItem;
        }
        return null;
    }

    private String updateCartItems(HttpServletRequest request, Model model) {
        String[] productIdList = request.getParameterValues(FormParams.PRODUCT_ID);
        String[] amountList = request.getParameterValues(FormParams.AMOUNT);

        OperationValueResult<HashMap<Integer, Integer>> productAmountMapResult = getProductAmountMapFrom(productIdList, amountList);
        if(productAmountMapResult.isFailed())
            return Env.showErrors(model, productAmountMapResult);

        Account account = Env.getAccountFromSession(request);
        if(account == null)
            return "account/LoginView";//TODO - do not request user to be logged in to buy products

        OperationValueResult<List<CartItem>> getCartItemsResult = getCartItems(account.getId());
        if(getCartItemsResult.isFailed())
            return Env.showErrors(model, getCartItemsResult);

        ArrayList<CartItem> cartItemsToUpdate = getCartItemsToUpdate(productAmountMapResult.getValue(), getCartItemsResult.getValue());
        ArrayList<CartItem> cartItemsToRemove = extractCartItemsWithZeroAmountToRemovingList(cartItemsToUpdate);
        try {
            cartRepository.save(cartItemsToUpdate);
            cartRepository.remove(cartItemsToRemove);
        } catch (Exception e) {
            return Env.showError(model, e.getMessage());//TODO -  wrap with user-friendly message and log
        }
        return showCart(request, model);
    }

    private ArrayList<CartItem> extractCartItemsWithZeroAmountToRemovingList(ArrayList<CartItem> cartItemsToUpdate) {
        ArrayList<CartItem> cartItemsToRemove = new ArrayList<>();
        for (CartItem cartItem: cartItemsToUpdate) {
            if(cartItem.getAmount() <= 0)
                cartItemsToRemove.add(cartItem);
        }
        for (CartItem cartItem: cartItemsToRemove) {
            cartItemsToUpdate.remove(cartItem);
        }
        return cartItemsToRemove;
    }

    private OperationValueResult<HashMap<Integer, Integer>> getProductAmountMapFrom(String[] productIdList, String[] amountList) {
        OperationValueResult<HashMap<Integer, Integer>> productAmountMapResult = new OperationValueResult<>();

        if(productIdList == null || amountList == null || productIdList.length != amountList.length)
            productAmountMapResult.addError("Invalid parameters in the Update Cart request.");

        HashMap<Integer, Integer> productAmountMap = new HashMap<>();
        for (int i = 0; i < productIdList.length; i++) {
            if(!StringHelper.isInteger(productIdList[i]) || !StringHelper.isInteger(amountList[i])) {
                productAmountMapResult.addError("Invalid parameters in the Update Cart request.");
                break;
            }
            productAmountMap.put(Integer.parseInt(productIdList[i]), Integer.parseInt(amountList[i]));
        }
        productAmountMapResult.setValue(productAmountMap);
        return productAmountMapResult;
    }

    private ArrayList<CartItem> getCartItemsToUpdate(HashMap<Integer, Integer> productAmountMap, List<CartItem> cartItems) {
        ArrayList<CartItem> cartItemsToUpdate = new ArrayList<>();
        for (CartItem cartItem: cartItems) {
            if(!productAmountMap.containsKey(cartItem.getProduct().getId()))
                continue;
            Integer amount = productAmountMap.get(cartItem.getProduct().getId());
            cartItem.setAmount(amount);
            cartItem.setPrice(cartItem.getProduct().getPrice() * amount);
            cartItemsToUpdate.add(cartItem);
        }
        return cartItemsToUpdate;
    }

    public CartItem createCartItemFor(Product product, Account account) throws SQLException {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setAccountId(account.getId());
        cartItem.setPrice(product.getPrice());
        cartItem.setAmount(1);
        cartItem.setCreatedOn(Env.getTimestamp());
        cartItem.setStatus(cartStatusRepository.getStatusNotPayed());
        return cartItem;
    }

    private OperationValueResult<Product> getProduct(int sku) {
        OperationValueResult<Product> result = new OperationValueResult<>();
        try {
            result.setValue(productRepository.get(sku));
        } catch (SQLException e) {
            result.addError(e.getMessage());
        }
        return result;
    }

    public OperationValueResult<List<CartItem>> getCartItems(int accountId) {
        OperationValueResult<List<CartItem>> result = new OperationValueResult<>();
        try {
            List<CartItem> cartItems = cartRepository.getList(accountId, cartStatusRepository.getStatusNotPayed());
            result.setValue(cartItems);
        } catch (SQLException e) {
            result.setValue(new ArrayList<>());
            result.addError(e.getMessage());//TODO -  wrap with user-friendly message and log
        }
        return result;
    }

    //----- Constants -------
    private class View {

        public final static String CartView = "cart/ListView";
    }

    private class ModelAttr {
        public final static String CART_ITEMS = "cartItems";
    }

    private class FormParams {
        public final static String TARGET_PRODUCT_ID = "targetProductId";
        public final static String AMOUNT = "amount";
        public final static String PRODUCT_ID = "productId";
    }
    private class FormActions {
        public final static String UPDATE = "Update";
        public final static String REMOVE = "Remove";
    }
}
