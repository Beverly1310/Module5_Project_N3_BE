package com.ra.service.imp;

import com.ra.model.cons.RoleName;
import com.ra.model.dto.req.BannerAdd;
import com.ra.model.dto.req.BannerEdit;

import com.ra.model.dto.req.CouponAdd;
import com.ra.model.dto.req.CouponEdit;
import com.ra.model.entity.Banner;
import com.ra.model.entity.Coupon;
import com.ra.model.entity.Orders;
import com.ra.model.entity.User;
import com.ra.repository.*;

import com.ra.model.entity.Banner;
import com.ra.model.entity.Orders;
import com.ra.model.entity.User;
import com.ra.repository.BannerRepository;
import com.ra.repository.OrdersRepository;
import com.ra.repository.RoleRepository;
import com.ra.repository.UserRepository;

import com.ra.service.AdminService;
import com.ra.util.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BannerRepository bannerRepository;
    private final FileUploadService fileUploadService;
    private final OrdersRepository ordersRepository;

    private final CouponRepository couponRepository;


    @Override
    public Page<User> getUserWithPagingAndSorting(Pageable pageable, String direction, String search) {
        if (direction != null) {
            if (direction.equalsIgnoreCase("desc")) {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().descending());
            } else {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().ascending());
            }
        }
        Page<User> users;
        if (search.isEmpty()) {
            users = userRepository.getAll(pageable);
        } else {
            users = userRepository.findAllByFullNameContains(search, pageable);
        }
        return users;
    }

    @Override
    public User changStatus(Long id) {
        User user = findById(id);
        if (user != null) {
            if (user.getRoles().stream().anyMatch(role -> role.getRoleName() == RoleName.ADMIN)) {
                throw new RuntimeException("You can not block Admin");
            } else {
                userRepository.updateQueryChangeStatus(id);
                return findById(id);
            }
        } else {
            throw new NoSuchElementException("User not found");
        }
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User setRole(Long id) {
        User user = findById(id);
        if (user != null) {
            if (user.getRoles().stream().anyMatch(role -> role.getRoleName() == RoleName.MANAGER)) {
                user.getRoles().remove(roleRepository.findRoleByRoleName(RoleName.MANAGER).orElseThrow(() -> new NoSuchElementException("Role not found")));
            } else {
                user.getRoles().add(roleRepository.findRoleByRoleName(RoleName.MANAGER).orElseThrow(() -> new NoSuchElementException("Role not found")));
            }
            userRepository.save(user);
            return user;
        } else {
            throw new NoSuchElementException("User not found");
        }
    }


    @Override
    public List<Banner> getBanners() {
        List<Banner> banners = bannerRepository.findAll();
        return banners;

    }

    @Override
    public Banner addBanner(BannerAdd bannerAdd) {
        Banner banner = Banner.builder()
                .bannerName(bannerAdd.getBannerName())
                .createdAt(LocalDate.now())
                .description(bannerAdd.getDescription())
                .status(true)
                .build();

        if (bannerAdd.getImage() != null && !bannerAdd.getImage().isEmpty()) {

            banner.setImage(fileUploadService.uploadFileToServer(bannerAdd.getImage()));
        } else {
            banner.setImage(null);
        }
        return bannerRepository.save(banner);
    }

    @Override
    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }

    @Override
    public Banner updateBanner(BannerEdit bannerEdit) {
        Banner banner = bannerRepository.findById(bannerEdit.getId()).orElse(null);
        if (banner != null) {
            if (bannerEdit.getBannerName() != null && !bannerEdit.getBannerName().isEmpty()) {
                banner.setBannerName(bannerEdit.getBannerName());
            }
            if (bannerEdit.getDescription() != null && !bannerEdit.getDescription().isEmpty()) {
                banner.setDescription(bannerEdit.getDescription());
            }
            if (bannerEdit.getImage() != null && !bannerEdit.getImage().isEmpty()) {

                banner.setImage(fileUploadService.uploadFileToServer(bannerEdit.getImage()));
            }
            return bannerRepository.save(banner);
        }
        return banner;
    }

    @Override
    public List<Orders> getOrders() {
        return ordersRepository.findAll();
    }

    @Override
    public List<Coupon> getCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public Coupon addCoupon(CouponAdd couponAdd) {
        Coupon coupon = Coupon.builder()
                .code(couponAdd.getCode())
                .discount(couponAdd.getDiscount())
                .startDate(couponAdd.getStartDate())
                .endDate(couponAdd.getEndDate())
                .quantity(couponAdd.getQuantity())
                .status(true)
                .title(couponAdd.getTitle())
                .build();
        return couponRepository.save(coupon);
    }

    @Override
    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    @Override
    public Coupon updateCoupon(CouponEdit couponEdit) {
        Coupon coupon = couponRepository.findById(couponEdit.getId()).orElse(null);
        Coupon couponExist = couponRepository.findCouponByCode(couponEdit.getCode()).orElse(null);
        if (coupon != null) {
            if (couponExist != null && !Objects.equals(couponExist.getId(), couponEdit.getId())) {
                throw new RuntimeException("Coupon already exists");
            } else {

                if (couponEdit.getDiscount() != null && !couponEdit.getDiscount().isEmpty()) {
                    coupon.setDiscount(couponEdit.getDiscount());
                }
                if (couponEdit.getCode() != null && !couponEdit.getCode().isEmpty()) {
                    coupon.setCode(couponEdit.getCode());
                }
                if (couponEdit.getEndDate() != null) {
                    coupon.setEndDate(couponEdit.getEndDate());
                }
                if (couponEdit.getStartDate() != null) {
                    coupon.setStartDate(couponEdit.getStartDate());
                }
                if (couponEdit.getTitle() != null && !couponEdit.getTitle().isEmpty()) {
                    coupon.setTitle(couponEdit.getTitle());
                }
                if (couponEdit.getQuantity() != null) {
                    coupon.setQuantity(couponEdit.getQuantity());
                }
                return couponRepository.save(coupon);
            }

        } else {
            throw new RuntimeException("Coupon not found");
        }
    }

}
