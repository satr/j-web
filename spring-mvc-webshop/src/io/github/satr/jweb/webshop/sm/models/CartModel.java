package io.github.satr.jweb.webshop.sm.models;

import io.github.satr.jweb.components.entities.Account;
import io.github.satr.jweb.components.entities.CartItem;
import io.github.satr.jweb.components.entities.Product;
import io.github.satr.jweb.components.helpers.StringHelper;
import io.github.satr.jweb.components.models.OperationResult;
import io.github.satr.jweb.components.models.OperationValueResult;
import io.github.satr.jweb.components.repositories.CartRepository;
import io.github.satr.jweb.components.repositories.CartStatusRepository;
import io.github.satr.jweb.components.repositories.ProductRepository;
import io.github.satr.jweb.webshop.sm.helpers.Env;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartModel {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartStatusRepository cartStatusRepository;

    public CartModel() {
        productRepository = new ProductRepository();
        cartRepository = new CartRepository();
        cartStatusRepository = new CartStatusRepository();
    }

    private CartItem getCartItemBy(List<CartItem> cartItems, Integer productId) {
        for (CartItem cartItem: cartItems) {
            if(cartItem.getProduct().getId() == productId)
                return cartItem;
        }
        return null;
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


    private CartItem createCartItemFor(Product product, Account account) throws SQLException {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setAccountId(account.getId());
        cartItem.setPrice(product.getPrice());
        cartItem.setAmount(1);
        cartItem.setCreatedOn(Env.getTimestamp());
        cartItem.setStatus(cartStatusRepository.getStatusNotPayed());
        return cartItem;
    }

    public OperationValueResult<Product> getProduct(int sku) {
        OperationValueResult<Product> result = new OperationValueResult<>();
        try {
            result.setValue(productRepository.get(sku));
        } catch (SQLException e) {
            result.addError(e.getMessage());
        }
        return result;
    }

    public OperationValueResult<HashMap<Integer, Integer>> getProductAmountMapFrom(String[] productIdList, String[] amountList) {
        OperationValueResult<HashMap<Integer, Integer>> productAmountMapResult = new OperationValueResult<>();

        if(productIdList == null || amountList == null || productIdList.length != amountList.length) {
            productAmountMapResult.addError("Invalid parameters in the Update Cart request.");
            return productAmountMapResult;
        }

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

    public OperationResult updateCart(OperationValueResult<HashMap<Integer, Integer>> productAmountMapResult, OperationValueResult<List<CartItem>> getCartItemsResult) {
        ArrayList<CartItem> cartItemsToUpdate = getCartItemsToUpdate(productAmountMapResult.getValue(), getCartItemsResult.getValue());
        ArrayList<CartItem> cartItemsToRemove = extractCartItemsWithZeroAmountToRemovingList(cartItemsToUpdate);
        OperationResult updateCartResult = new OperationResult();
        try {
            cartRepository.save(cartItemsToUpdate);
            cartRepository.remove(cartItemsToRemove);
        } catch (Exception e) {
            updateCartResult.addError(e.getMessage());//TODO -  wrap with user-friendly message and log
        }
        return updateCartResult;
    }

    public OperationResult saveCart(Account account, Product product) {
        OperationResult saveCartResult = new OperationResult();
        try {
            CartItem cartItem = createCartItemFor(product, account);
            cartItem.setUpdatedOn(Env.getTimestamp());
            cartRepository.save(cartItem);
        } catch (SQLException e) {
            saveCartResult.addError(e.getMessage());//TODO - do not request user to be logged in to buy products
        }
        return saveCartResult;
    }

    public OperationResult removeCartItems(List<CartItem> cartItems, int productId) {
        OperationResult removeCartItemResult = new OperationResult();
        CartItem cartItemToRemove = getCartItemBy(cartItems, productId);
        try {
            cartRepository.remove(cartItemToRemove);
        } catch (Exception e) {
            removeCartItemResult.addError( e.getMessage());//TODO -  wrap with user-friendly message and log
        }
        return removeCartItemResult;
    }
}
