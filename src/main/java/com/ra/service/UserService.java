package com.ra.service;

import com.ra.model.dto.req.ChangePasswordRequest;
import com.ra.model.dto.req.UserEdit;
import com.ra.model.entity.Product;
import com.ra.model.entity.User;
import com.ra.model.entity.WishList;

import java.util.List;

public interface UserService {
    User editUser(UserEdit userEdit);

    boolean changePassword(ChangePasswordRequest changePasswordRequest);

    List<Product> getWishList();

    List<Product> removeProductFromWishList(Long productId);
}
