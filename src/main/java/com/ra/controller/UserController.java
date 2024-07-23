package com.ra.controller;

import com.ra.model.dto.req.ChangePasswordRequest;
import com.ra.model.dto.req.UserEdit;
import com.ra.model.dto.res.CategoryWithProductsDTO;
import com.ra.model.dto.res.ResponseData;
import com.ra.model.entity.Product;
import com.ra.model.entity.User;
import com.ra.model.entity.WishList;
import com.ra.service.ICategoryService;
import com.ra.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final UserService userService;
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
    public  ResponseEntity<ResponseData<List<Product>>> getWishList() {
        List<Product> wishLists = userService.getWishList();
        return new ResponseEntity<>(new ResponseData<>("success", wishLists, HttpStatus.OK), HttpStatus.OK);
    }
    @DeleteMapping("/wishlist/{productId}")
    public ResponseEntity<ResponseData<List<Product>>> deleteWishList(@PathVariable Long productId) {
        List<Product> products = userService.removeProductFromWishList(productId);
        return new ResponseEntity<>(new ResponseData<>("success", products , HttpStatus.OK), HttpStatus.OK);
    }
}
