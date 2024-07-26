package com.ra.service.imp;

import com.ra.model.cons.OrderStatus;
import com.ra.model.dto.req.ChangePasswordRequest;
import com.ra.model.dto.req.UserEdit;
import com.ra.model.dto.res.TotalPriceRes;
import com.ra.model.entity.*;
import com.ra.repository.*;

import com.ra.security.principal.CustomUserDetail;
import com.ra.service.UserService;
import com.ra.util.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;
    private final WishlistRepository wishListRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductDetailRepository productDetailRepository;
    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;
    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public boolean changePassword(ChangePasswordRequest changePasswordRequest) {
        try {
            User user = userRepository.findById(getCurrentUser().getId()).orElseThrow(() -> new NoSuchElementException("User not found"));
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);
            return true;
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Change password failed");
        }
    }


    public static CustomUserDetail getCurrentUser() {
        return (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public User editUser(UserEdit userEdit) {
        User user = userRepository.findById(getCurrentUser().getId()).orElseThrow(() -> new NoSuchElementException("User not found"));
        if (userEdit.getFullName() != null && !userEdit.getFullName().isBlank()) {
            user.setFullName(userEdit.getFullName());
        }
        if (userEdit.getEmail() != null && !userEdit.getEmail().isBlank()) {
            user.setEmail(userEdit.getEmail());
        }
        if (userEdit.getPhone() != null && !userEdit.getPhone().isBlank()) {
            user.setPhone(userEdit.getPhone());
        }
        if (userEdit.getAvatar() != null && !userEdit.getAvatar().isEmpty()) {
            user.setAvatar(fileUploadService.uploadFileToServer(userEdit.getAvatar()));
        }
        user.setUpdatedAt(userEdit.getUpdatedAt());
        return userRepository.save(user);
    }

    @Override
    public List<Product> getWishList() {
        Long userId = getCurrentUser().getId();
        List<WishList> wishLists = wishListRepository.getAllByUserId(userId);
        List<Product> products = wishLists.stream().map(wl -> productRepository.findById(wl.getProduct().getId()).orElseThrow(() -> new NoSuchElementException("Product not found"))).toList();
        return products;
    }

    @Override
    @Transactional
    public List<Product> removeProductFromWishList(Long productId) {
        wishListRepository.deleteByProductId(productId);
        List<Product> products = getWishList();
        return products;
    }

    @Override
    public Orders checkOutCart(String couponCode, String note,Long addressId) {
        User user = userRepository.findById(getCurrentUser().getId()).orElseThrow(() -> new NoSuchElementException("User not found"));
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new NoSuchElementException("You do not have contact information yet, please add contact information first."));
        Coupon coupon = null;
        if (couponCode!= null && !couponCode.isBlank()) {
            coupon = couponRepository.findCouponByCode(couponCode).orElseThrow(() -> new NoSuchElementException("Coupon not found"));
        }
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByUserId(user.getId());
        if (!shoppingCarts.isEmpty()) {
            double totalPrice = shoppingCarts.stream().map((cart) -> {
                Double productPrice = productDetailRepository.findById(cart.getProductDetail().getId()).orElseThrow(() -> new NoSuchElementException("Product type not found")).getUnitPrice();
                return cart.getQuantity() * productPrice;
            }).reduce(0.0, Double::sum);
            Orders orders = Orders.builder()
                    .createdAt(LocalDate.now())
                    .district(address.getDistrict())
                    .note(note)
                    .phone(address.getPhone())
                    .province(address.getProvince())
                    .serialNumber(UUID.randomUUID().toString())
                    .status(OrderStatus.WAITING)
                    .streetAddress(address.getStreetAddress())
                    .ward(address.getStreetAddress())
                    .user(user)
                    .build();
            if (coupon != null) {
                orders.setCoupon(coupon);
                orders.setTotalPriceAfterCoupon(totalPrice - totalPrice * (Double.parseDouble(coupon.getDiscount()) / 100));
                coupon.setQuantity(coupon.getQuantity() - 1);
                couponRepository.save(coupon);

            } else {
                orders.setCoupon(null);
                orders.setTotalPriceAfterCoupon(0D);

            }
            ordersRepository.save(orders);
            Optional<Event> event = eventRepository.findByStartDate(orders.getCreatedAt());
            if (event.isPresent()) {
                if (event.orElse(null).isStatus()) {
                    orders.setTotalDiscountedPrice(orders.getTotalPriceAfterCoupon() + totalPrice * (Double.parseDouble(event.orElse(null).getDiscount()) / 100));
                } else {
                    orders.setTotalDiscountedPrice(orders.getTotalPriceAfterCoupon());
                }
            }

            orders.setTotalPrice(totalPrice - orders.getTotalDiscountedPrice());
            for (ShoppingCart shoppingCart : shoppingCarts) {
                ProductDetail productDetail = productDetailRepository.findById(shoppingCart.getProductDetail().getId()).orElseThrow(() -> new NoSuchElementException("Product type not found"));
                OrderDetail orderDetail = OrderDetail.builder()
                        .id(new OrderDetailId(orders, shoppingCart.getProductDetail()))
                        .orderQuantity(shoppingCart.getQuantity())
                        .unitPrice(productDetail.getUnitPrice())
                        .orderDetailName(user.getFullName())
                        .build();
                if (productDetail.getStock() < orderDetail.getOrderQuantity()) {
                    throw new RuntimeException("Product quantity less than order quantity");
                } else {
                    productDetail.setStock(productDetail.getStock()-orderDetail.getOrderQuantity());
                }
                productDetailRepository.save(productDetail);
                orderDetailRepository.save(orderDetail);
                shoppingCartRepository.delete(shoppingCart);
            }

            return orders;
        } else {
            throw new NoSuchElementException("You have nothing to pay");
        }

    }

    @Override
    public List<Address> getPaymentAddress() {
        List<Address> addresses = addressRepository.findAllByUserId(getCurrentUser().getId());
        return addresses;
    }

    @Override
    public TotalPriceRes getPaymentTotalPrice(String couponCode) {
        User user = userRepository.findById(getCurrentUser().getId()).orElseThrow(() -> new NoSuchElementException("User not found"));
        Coupon coupon = null;
        if (couponCode!= null && !couponCode.isBlank()) {
            coupon = couponRepository.findCouponByCode(couponCode).orElseThrow(() -> new NoSuchElementException("Coupon not found"));
        }
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByUserId(user.getId());
        if (!shoppingCarts.isEmpty()) {
            double totalPrice = shoppingCarts.stream().map((cart) -> {
                Double productPrice = productDetailRepository.findById(cart.getProductDetail().getId()).orElseThrow(() -> new NoSuchElementException("Product type not found")).getUnitPrice();
                return cart.getQuantity() * productPrice;
            }).reduce(0.0, Double::sum);
            double totalPriceAfterDiscount = totalPrice;
            if (coupon != null) {
              totalPriceAfterDiscount = totalPrice - totalPrice * (Double.parseDouble(coupon.getDiscount()) / 100);
            }
            Optional<Event> event = eventRepository.findByStartDate(LocalDate.now());
            if (event.isPresent()) {
                if (event.orElse(null).isStatus()) {
                     totalPriceAfterDiscount = totalPriceAfterDiscount - totalPrice * (Double.parseDouble(event.orElse(null).getDiscount()) / 100);
                }
            }
            return new TotalPriceRes(totalPrice, totalPriceAfterDiscount);
        } else {
            return new TotalPriceRes(0D,0D);
        }
    }
}
