package com.ra.service;


import com.ra.model.dto.req.BannerAdd;
import com.ra.model.dto.req.BannerEdit;
import com.ra.model.entity.Banner;
import com.ra.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {
    Page<User> getUserWithPagingAndSorting(Pageable pageable,String direction,String search);
    User changStatus(Long id);
    User findById(Long id);
    User setRole(Long id);
    List<Banner> getBanners();
    Banner addBanner(BannerAdd bannerAdd);
    void deleteBanner(Long id);
    Banner updateBanner(BannerEdit bannerEdit);
}
