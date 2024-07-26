package com.ra.service;


import com.ra.model.dto.req.*;

import com.ra.model.dto.res.OrderStatistics;

import com.ra.model.dto.res.SoldProduct;

import com.ra.model.entity.*;
import com.ra.model.entity.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {
    Page<User> getUserWithPagingAndSorting(Pageable pageable, String direction, String search);

    User changStatus(Long id);

    User findById(Long id);

    User setRole(Long id);

    List<Banner> getBanners();

    Banner addBanner(BannerAdd bannerAdd);

    void deleteBanner(Long id);

    Banner updateBanner(BannerEdit bannerEdit);

    List<Orders> getOrders();

    List<Coupon> getCoupons();

    Coupon addCoupon(CouponAdd couponAdd);

    void deleteCoupon(Long id);

    Coupon updateCoupon(CouponEdit couponEdit);

    List<Event> getEvents();

    Event addEvent(EventAdd eventAdd);

    void deleteEvent(Long id);

    Event updateEvent(EventEdit eventEdit);


    List<OrderStatistics> getOrderStatistics(Integer year);

    List<SoldProduct> getSoldProduct(Integer year);

}
