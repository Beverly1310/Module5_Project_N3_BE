package com.ra.service;

import com.ra.model.dto.req.CartItemRequest;
import com.ra.model.dto.res.CartItemResponse;
import com.ra.model.dto.res.CartListResponse;
import com.ra.model.entity.ShoppingCart;

public interface ICartService {
    void add(CartItemRequest request);

    void changeQuantity(CartItemRequest request);

    CartListResponse getAllItemsInCart();

    void deleteItemFromCart(Long cartId);
    void deleteAllItems();
    CartItemResponse getResponse(ShoppingCart cart);
    int cartNumber();
}
