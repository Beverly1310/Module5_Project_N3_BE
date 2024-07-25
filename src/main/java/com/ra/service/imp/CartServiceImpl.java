package com.ra.service.imp;

import com.ra.model.dto.req.CartItemRequest;
import com.ra.model.dto.res.CartItemResponse;
import com.ra.model.dto.res.CartListResponse;
import com.ra.model.entity.ShoppingCart;
import com.ra.repository.CartRepository;
import com.ra.repository.ProductDetailRepository;
import com.ra.repository.ProductRepository;
import com.ra.repository.UserRepository;
import com.ra.security.principal.CustomUserDetail;
import com.ra.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductDetailRepository productDetailRepository;

    private static Long currentUserId() {
        CustomUserDetail customUserDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customUserDetail.getId();
    }

    @Override
    public void add(CartItemRequest request) {
        if (request.getQuantity() > 0) {
            if (cartRepository.existsByUserIdAndProductDetailId(currentUserId(), request.getProductDetailId())) {
                ShoppingCart cart = cartRepository.findByUserIdAndProductDetailId(currentUserId(), request.getProductDetailId());
                cart.setQuantity(cart.getQuantity() + request.getQuantity());
                cartRepository.save(cart);
            } else {
                ShoppingCart cart = ShoppingCart.builder()
                        .user(userRepository.findById(currentUserId()).orElseThrow(() -> new RuntimeException("User not found")))
                        .quantity(request.getQuantity())
                        .productDetail(productDetailRepository.findById(request.getProductDetailId()).orElseThrow(() -> new RuntimeException("Product detail not found"))).build();
                cartRepository.save(cart);
            }
        }
    }

    @Override
    public void changeQuantity(CartItemRequest request) {
        if (!cartRepository.existsByUserIdAndProductDetailId(currentUserId(), request.getProductDetailId())) {
            throw new RuntimeException("This item is no longer in your cart!");
        } else {
            ShoppingCart cart = cartRepository.findByUserIdAndProductDetailId(currentUserId(), request.getProductDetailId());
            if (request.getQuantity() > 0) {
                cart.setQuantity(request.getQuantity());
                cartRepository.save(cart);
            } else {
                cartRepository.delete(cart);
            }
        }
    }

    @Override
    public CartListResponse getAllItemsInCart() {
        List<ShoppingCart> shoppingCartList = cartRepository.findAllByUserId(currentUserId());
        List<CartItemResponse> cartItemResponses = shoppingCartList.stream()
                .map(this::getResponse)
                .collect(Collectors.toList());
        return CartListResponse.builder().cartList(cartItemResponses).build();
    }

    @Override
    public void deleteItemFromCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    @Override
    @Transactional
    public void deleteAllItems() {
        cartRepository.deleteAllByUserId(currentUserId());
    }

    @Override
    public CartItemResponse getResponse(ShoppingCart cart) {
        return CartItemResponse.builder()
                .cartId(cart.getId())
                .productDetailId(cart.getProductDetail().getId())
                .productName(cart.getProductDetail().getProduct().getProductName() + " " + cart.getProductDetail().getProductDetailName())
                .image(cart.getProductDetail().getImage())
                .unitPrice(cart.getProductDetail().getUnitPrice())
                .quantity(cart.getQuantity()).build();
    }

    @Override
    public int cartNumber() {
        return (int) cartRepository.findAllByUserId(currentUserId()).stream()
                .mapToLong(ShoppingCart::getQuantity)
                .sum();
    }
}
