package com.ra.controller;

import com.ra.model.dto.req.BannerAdd;
import com.ra.model.dto.req.BannerEdit;
import com.ra.model.dto.req.CategoryForm;
import com.ra.model.dto.req.ProductForm;
import com.ra.model.dto.res.BrandFormResponse;
import com.ra.model.dto.res.CategoryFormResponse;
import com.ra.model.dto.res.ProductPageResponse;
import com.ra.model.dto.res.ProductResponse;
import com.ra.model.entity.Banner;
import com.ra.model.entity.Category;

import com.ra.model.entity.Orders;

import com.ra.model.entity.User;
import com.ra.service.AdminService;
import com.ra.service.IBrandService;

import com.ra.service.ICategoryService;
import com.ra.service.IProductService;
import jakarta.persistence.criteria.Order;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import com.ra.model.entity.User;
import com.ra.service.AdminService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ICategoryService categoryService;
    private final IProductService productService;
    private final IBrandService brandService;
    private final AdminService adminService;

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
        CategoryForm categoryForm = categoryService.getCategoryToUpdate(id);

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
    return ResponseEntity.ok(response);
}



    //Add
    @GetMapping("product/add")
    public ResponseEntity<ProductForm> addNewProduct() {
        List<CategoryFormResponse> categories = categoryService.getAllForInput();
        List<BrandFormResponse> brands = brandService.getAllForInput();

        ProductForm productForm = ProductForm.builder()
                .categoryList(categories)
                .brandList(brands)
                .build();

        return ResponseEntity.ok(productForm);
    }
    @PostMapping("/product/add")
    public ResponseEntity<?> submitNewProduct(@Valid @ModelAttribute ProductForm productForm) {
        productService.add(productForm);
        return ResponseEntity.ok().build();
    }

    //    Update
    @GetMapping("/product/{id}/update")
    public ResponseEntity<ProductForm> getProductToUpdate(@PathVariable Long id) {
        List<CategoryFormResponse> categories = categoryService.getAllForInput();
        List<BrandFormResponse> brands = brandService.getAllForInput();
        ProductForm form=productService.getProductToUpdate(id);
        form.setBrandList(brands);
        form.setCategoryList(categories);

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
    }

    @GetMapping("/user")
    public ResponseEntity<Page<User>> getAllUsers
            (@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
             @RequestParam(required = false) String direction,
             @RequestParam(defaultValue = "") String search
            ) {
        Page<User> users = adminService.getUserWithPagingAndSorting(pageable, direction, search);
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

    @GetMapping("/banner")
    public ResponseEntity<List<Banner>> getAllBanners() {
        List<Banner> banners = adminService.getBanners();
        return ResponseEntity.ok().body(banners);
    }

    @PostMapping("/banner")
    public ResponseEntity<Banner> addBanner(@Valid @ModelAttribute BannerAdd bannerAdd) {
        Banner banner = adminService.addBanner(bannerAdd);
        return ResponseEntity.ok().body(banner);
    }
    @DeleteMapping("/banner/{bannerId}")
    public ResponseEntity<?> deleteBanner(@PathVariable  Long bannerId) {
        adminService.deleteBanner(bannerId);
        return ResponseEntity.ok().body("Deleted banner");
    }
    @PutMapping("/banner")
    public ResponseEntity<?> updateBanner(@Valid @ModelAttribute BannerEdit bannerEdit) {
        Banner banner = adminService.updateBanner(bannerEdit);
        return ResponseEntity.ok().body(banner);

    }
    @GetMapping("/orders")
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok().body(adminService.getOrders());
    }
}

