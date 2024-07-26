package com.ra.controller;


import com.ra.model.dto.req.*;
import com.ra.model.dto.res.*;
import com.ra.model.entity.*;
import com.ra.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final AdminService adminService;
    private final IColorService colorService;
    private final IBrandService brandService;
    private final IOrderService orderService;
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
        List<ColorFormResponse> colors=colorService.getAllForInput();
        ProductForm productForm = ProductForm.builder()
                .categoryList(categories)
                .brandList(brands)
                .colorList(colors)
                .build();

        return ResponseEntity.ok(productForm);
    }

    @PostMapping("/product/add")
    public ResponseEntity<?> addProduct(@Valid @ModelAttribute ProductForm productForm) {
        productService.add(productForm);
        return ResponseEntity.ok().build();
    }

    //    Update
    @GetMapping("/product/{id}/update")
    public ResponseEntity<ProductForm> getProductToUpdate(@PathVariable Long id) {
        ProductForm form = productService.getProductToUpdate(id);

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
//    Orders
    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        Page<OrderResponse> orders = orderService.findAll(page - 1, size, sortField, sortDirection);
        return ResponseEntity.ok(orders);
    }
//  View in details

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> viewOrderInDetails(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.viewFullDetail(orderId));
    }
//    Change status
    @PutMapping("/orders/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id,@RequestParam String status){
        orderService.changeStatus(id,status);
        return ResponseEntity.ok().build();
    };

    @GetMapping("/coupon")
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        List<Coupon> coupons = adminService.getCoupons();
        return ResponseEntity.ok().body(coupons);
    }
    @PostMapping("/coupon")
    public ResponseEntity<Coupon> addCoupon(@Valid @RequestBody CouponAdd couponAdd) {
        Coupon coupon = adminService.addCoupon(couponAdd);
        return ResponseEntity.ok().body(coupon);
    }
    @DeleteMapping("/coupon/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) {
        adminService.deleteCoupon(id);
        return ResponseEntity.ok().body("Deleted coupon");
    }
    @PutMapping("/coupon")
    public ResponseEntity<?> updateCoupon(@Valid @RequestBody CouponEdit couponEdit) {
        Coupon coupon = adminService.updateCoupon(couponEdit);
        return ResponseEntity.ok().body(coupon);
    }
    @GetMapping("/event")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = adminService.getEvents();
        return ResponseEntity.ok().body(events);
    }
    @PostMapping("/event")
    public ResponseEntity<Event> addEvent(@Valid @RequestBody EventAdd eventAdd) {
        Event event = adminService.addEvent(eventAdd);
        return ResponseEntity.ok().body(event);
    }
    @DeleteMapping("/event/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        adminService.deleteEvent(id);
        return ResponseEntity.ok().body("Deleted event");
    }
    @PutMapping("/event")
    public ResponseEntity<?> updateEvent(@Valid @RequestBody EventEdit eventEdit) {
        Event event = adminService.updateEvent(eventEdit);
        return ResponseEntity.ok().body(event);
    }
    @GetMapping("/orderstatistics")
    public ResponseEntity<List<OrderStatistics>> getAllOrderStatistics(@RequestParam(value = "year",required = false)Integer year) {
        List<OrderStatistics> orderStatistics = adminService.getOrderStatistics(year);
        return ResponseEntity.ok().body(orderStatistics);
    }

    @GetMapping("/soldproduct")
    public ResponseEntity<List<SoldProduct>> getSoldProduct(@RequestParam(value = "year",required = false)Integer year) {
        List<SoldProduct> soldProduct = adminService.getSoldProduct(year);
        return ResponseEntity.ok().body(soldProduct);
    }

    @GetMapping("/salerevenue")
    public ResponseEntity<List<SaleRevenue>> getSaleRevenues(@RequestParam(value = "year",required = false)Integer year) {
        List<SaleRevenue> saleRevenue = adminService.getSaleRevenue(year);
        return ResponseEntity.ok().body(saleRevenue);
    }

}

