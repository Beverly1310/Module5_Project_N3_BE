package com.ra.service;

import com.ra.model.dto.res.ProductResponse;
import com.ra.model.entity.WishList;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public interface IWishListService {
    void addToWishlist(Long productId);
    void deleteFromWishlist(Long productId);
    boolean isFavourite(Long productId);
    void addOrDeleteFromWishList(Long productId);
}
