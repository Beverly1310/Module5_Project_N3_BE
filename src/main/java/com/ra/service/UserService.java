package com.ra.service;

import com.ra.model.dto.req.ChangePasswordRequest;
import com.ra.model.dto.req.FormReview;
import com.ra.model.dto.req.UserEdit;
import com.ra.model.dto.res.TotalPriceRes;
import com.ra.model.entity.*;

import java.util.List;

public interface UserService {
    User editUser(UserEdit userEdit);

    boolean changePassword(ChangePasswordRequest changePasswordRequest);

    List<Product> getWishList();

    List<Product> removeProductFromWishList(Long productId);

    Orders checkOutCart(String couponCode, String note,Long addressId);

    List<Address> getPaymentAddress();

    TotalPriceRes getPaymentTotalPrice(String couponCode);

    List<ProductDetail> findSuccessOrder();

    Review createReview(FormReview formReview);
}
