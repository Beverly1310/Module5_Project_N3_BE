package com.ra.controller;

import com.ra.model.dto.req.CategoryForm;
import com.ra.model.dto.req.ProductForm;
import com.ra.model.dto.res.ProductResponse;
import com.ra.model.entity.Category;
import com.ra.service.ICategoryService;
import com.ra.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import com.ra.model.entity.User;
import com.ra.service.AdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
    private final ICategoryService categoryService;
    private final IProductService productService;
    //    Category management

    //    Display all
    @GetMapping("/category")
    public ResponseEntity<Page<Category>> getCategories(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Page<Category> categories = categoryService.findCategoriesByKeyword(keyword, page - 1, size, sortBy, sortDirection);
        return ResponseEntity.ok(categories);
    }

    //Add
    @PostMapping("/category/add")
    public ResponseEntity<?> addCategory(@Valid @ModelAttribute CategoryForm categoryForm) {
        categoryService.add(categoryForm);
        return ResponseEntity.ok().build();
    }

    //    Update
    @GetMapping("/category/{id}/update")
    public ResponseEntity<CategoryForm> getCurrentCategory(@PathVariable Long id) {
        CategoryForm categoryForm=categoryService.getCategoryToUpdate(id);

        return ResponseEntity.ok(categoryForm);

    }


    @PutMapping("/category/{id}/update")
    public ResponseEntity<?> updateCategory(@Valid @ModelAttribute CategoryForm categoryForm, @PathVariable Long id) {
        categoryForm.setId(id);
        categoryService.update(categoryForm);
        return ResponseEntity.ok().build();

    }
//    Delete
    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok().build();

    }
//    Product management
//    Display all
@GetMapping("/product")
public ResponseEntity<Page<ProductResponse>> getProducts(
        @RequestParam(defaultValue = "") String keyword,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDirection) {
    Page<ProductResponse> productResponses = productService.findProductsByKeyword(keyword, page - 1, size, sortBy, sortDirection);
    return ResponseEntity.ok(productResponses);
}

    //Add
    @PostMapping("/product/add")
    public ResponseEntity<?> addProduct(@Valid @ModelAttribute ProductForm productForm) {
        productService.add(productForm);
        return ResponseEntity.ok().build();
    }

    //    Update
    @GetMapping("/product/{id}/update")
    public ResponseEntity<ProductForm> getProductToUpdate(@PathVariable Long id) {
        ProductForm form=productService.getProductToUpdate(id);

        return ResponseEntity.ok(form);

    }


    @PutMapping("/product/{id}/update")
    public ResponseEntity<?> updateProduct(@Valid @ModelAttribute ProductForm productForm, @PathVariable Long id) {
        productService.update(productForm);
        return ResponseEntity.ok().build();

    }
    //    Delete
    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok().build();

    private final AdminService adminService;
    @GetMapping("/user")
    public ResponseEntity<Page<User>> getAllUsers( @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                   @RequestParam(required = false) String direction,
                                                   @RequestParam(defaultValue = "") String search
                                       ) {
        Page<User> users = adminService.getUserWithPagingAndSorting(pageable,direction,search);
        return ResponseEntity.ok().body(users);
    }
    @PutMapping("/user/{userId}")
    public ResponseEntity<User> changeStatus(@PathVariable("userId") Long userId) {
        User user = adminService.changStatus(userId);
        return ResponseEntity.ok().body(user);
    }
    @PutMapping("user/setrole/{userId}")
    public ResponseEntity<User> changeRole(@PathVariable("userId") Long userId) {
       User user = adminService.setRole(userId);
       return ResponseEntity.ok().body(user);
    }
}
