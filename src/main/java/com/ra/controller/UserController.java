package com.ra.controller;

import com.ra.model.dto.req.CartItemRequest;
import com.ra.model.dto.req.ChangePasswordRequest;
import com.ra.model.dto.req.CommentRequest;
import com.ra.model.dto.req.UserEdit;
import com.ra.model.dto.res.*;
import com.ra.model.entity.*;
import com.ra.service.*;
import com.ra.model.dto.res.CategoryWithProductsDTO;
import com.ra.model.dto.res.ResponseData;
import com.ra.model.entity.User;
import com.ra.service.ICategoryService;
import com.ra.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final ICategoryService categoryService;
    private final IProductService productService;
    private final UserService userService;
    private final IWishListService wishListService;
    private final ICommentService commentService;
    private final ICartService cartService;

    //    Display categories and products
    @GetMapping("/category/{id}")
    public ResponseEntity<?> viewCategory(@PathVariable Long id) {
        Optional<CategoryWithProductsDTO> categoryWithProducts = categoryService.findCategoryWithProducts(id);
        if (categoryWithProducts.isEmpty()) {
            return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
        }

        CategoryWithProductsDTO dto = categoryWithProducts.get();
        if (dto.getProducts().isEmpty()) {
            dto.setMessage("No products found for this category.");
        }
        return ResponseEntity.ok(dto);
    }

    //    product
//    View product detail
    @GetMapping("/product/{id}")
    public ResponseEntity<?> viewProduct(@PathVariable Long id) {
        ProductResponse productResponse = productService.findById(id);
        productResponse.setOnWishlist(wishListService.isFavourite(id));
        return ResponseEntity.ok(productResponse);
    }

//    Get related products
    @GetMapping("product/{id}/related")
    public ResponseEntity<?> getRelatedProducts(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getRelatedProducts(id));
    }

    //    View comments
    @GetMapping("/product/{productId}/comment")
    public ResponseEntity<?> viewComments(@PathVariable Long productId,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "5") int size) {
        CommentSection commentSection = commentService.findAllByProduct(productId, page - 1, size);

        return ResponseEntity.ok(commentSection);
    }

    @PostMapping("/product/{productId}/comment")
    public ResponseEntity<?> addAComment(@Valid @RequestBody CommentRequest request) {
        commentService.addComment(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/product/{productId}/commentDetail")
    public ResponseEntity<?> addACommentDetail(@Valid @RequestBody CommentRequest request) {
        commentService.addCommentDetail(request);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable long id) {
        commentService.deleteCommentChain(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comment/commentDetail/{commentDetailId}")
    public ResponseEntity<?> deleteCommentDetail(@PathVariable long commentDetailId) {
        commentService.deleteCommentDetail(commentDetailId);
        return ResponseEntity.ok().build();
    }

    //    Display all
    @GetMapping("/product")
    public ResponseEntity<ProductPageResponse> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Page<ProductResponse> productResponses = productService.findProductsByKeyword(keyword, page - 1, size, sortBy, sortDirection);
        ProductPageResponse response = new ProductPageResponse();
        response.setProducts(productResponses.getContent());
        response.setTotalPages(productResponses.getTotalPages());
        for (ProductResponse res : response.getProducts()) {
            res.setOnWishlist(wishListService.isFavourite(res.getId()));
        }
        return ResponseEntity.ok(response);
    }

    //    Newest products
    @GetMapping("/product/newest")
    public ResponseEntity<?> getNewestProducts() {
        return ResponseEntity.ok(productService.getNewest());
    }
//    Most sold
    @GetMapping("/product/mostSold")
    public ResponseEntity<?> getMostSoldProducts() {
        return ResponseEntity.ok(productService.getMostSold());
    }

    //    Shopping cart
    @GetMapping("/cart")
    public ResponseEntity<?> getAllCartItems() {
        CartListResponse cartListResponse=cartService.getAllItemsInCart();
        return ResponseEntity.ok(cartListResponse);
    }

    @GetMapping("/getCartCount")
    public ResponseEntity<?> getCartCount() {
        return ResponseEntity.ok(cartService.cartNumber());
    }

    @PostMapping("/cart/add")
    public ResponseEntity<?> addItemToCart(@Valid @RequestBody CartItemRequest request){
        cartService.add(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/cart/update")
    public ResponseEntity<?> changeItemQuantity(@Valid @RequestBody CartItemRequest request){
        cartService.changeQuantity(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable Long cartId){
        cartService.deleteItemFromCart(cartId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("cart/clearAll")
    public ResponseEntity<?> clearAllCartItems(){
        cartService.deleteAllItems();
        return ResponseEntity.ok().build();
    }
    //  Wishlist
    @PostMapping("/product/{productId}/wishlist")
    public ResponseEntity<?> addToWishlist(@PathVariable long productId) {
        wishListService.addOrDeleteFromWishList(productId);
        return ResponseEntity.ok().build();
    }



    @PutMapping("/edit")
    public ResponseEntity<ResponseData<User>> editAccount(@Valid @ModelAttribute UserEdit userEdit) {
        User user = userService.editUser(userEdit);
        return new ResponseEntity<>(new ResponseData<>("success", user, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/account/change-password")
    public ResponseEntity<ResponseData<String>> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
        return new ResponseEntity<>(new ResponseData<>("success", "Success", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/wishlist")
    public ResponseEntity<ResponseData<List<Product>>> getWishList() {
        List<Product> wishLists = userService.getWishList();
        return new ResponseEntity<>(new ResponseData<>("success", wishLists, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/wishlist/{productId}")
    public ResponseEntity<ResponseData<List<Product>>> deleteWishList(@PathVariable Long productId) {
        List<Product> products = userService.removeProductFromWishList(productId);
        return new ResponseEntity<>(new ResponseData<>("success", products, HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping("/payment")
    public ResponseEntity<?> doPayment(@RequestParam(value = "couponCode", required = false) String couponCode, @RequestParam(value = "note", required = false) String note, @RequestParam(value = "addressId", required = false) Long addressId) {
        Orders orders = userService.checkOutCart(couponCode, note,addressId);
        return new ResponseEntity<>(new ResponseData<>("success", orders, HttpStatus.OK), HttpStatus.OK);
    }
    @GetMapping("/payment/address")
    public ResponseEntity<ResponseData<?>> getPaymentAddress() {
        List<Address> addresses = userService.getPaymentAddress();
        return new ResponseEntity<>(new ResponseData<>("success",addresses,HttpStatus.OK ), HttpStatus.OK);
    }
    @GetMapping("/payment/totalPrice")
    public ResponseEntity<?> getPaymentTotalPrice(@RequestParam(value = "couponCode", required = false) String couponCode) {
        TotalPriceRes totalPrice = userService.getPaymentTotalPrice(couponCode);
        return new ResponseEntity<>(new ResponseData<>("success",totalPrice,HttpStatus.OK ), HttpStatus.OK);
    }
}
