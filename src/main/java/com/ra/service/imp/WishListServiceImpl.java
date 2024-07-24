package com.ra.service.imp;

import com.ra.model.dto.res.ProductResponse;
import com.ra.model.entity.Product;
import com.ra.model.entity.User;
import com.ra.model.entity.WishList;
import com.ra.repository.ProductRepository;
import com.ra.repository.UserRepository;
import com.ra.repository.WishlistRepository;
import com.ra.security.principal.CustomUserDetail;
import com.ra.service.IProductService;
import com.ra.service.IWishListService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements IWishListService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private static Long currentUserId() {
        CustomUserDetail customUserDetail= (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customUserDetail.getId();
    }

    @Override
    public void addToWishlist(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product with ID " + productId + " not found"));
        User user = userRepository.findById(currentUserId())
                .orElseThrow(() -> new IllegalArgumentException("You are currently not logged in."));

        WishList wishlist = new WishList();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        wishlist.setCreatedAt(LocalDate.now());
        wishlistRepository.save(wishlist);
    }

    @Override
    public void deleteFromWishlist(Long productId) {
        wishlistRepository.deleteByUserIdAndProductId(currentUserId(), productId);
    }

    @Override
    public boolean isFavourite(Long productId) {
       return wishlistRepository.existsByUserIdAndProductId(currentUserId(), productId);
    }

    @Override
    @Transactional
    public void addOrDeleteFromWishList(Long productId) {
        if (isFavourite(productId)){
            deleteFromWishlist(productId);
        } else {
            addToWishlist(productId);
        }
    }
}
