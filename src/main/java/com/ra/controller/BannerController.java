package com.ra.controller;

import com.ra.model.dto.res.ResponseData;
import com.ra.model.entity.Banner;
import com.ra.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banner")
@RequiredArgsConstructor
public class BannerController {
    private  final BannerService bannerService;
    @GetMapping("/getbanners")
    public ResponseEntity<ResponseData<List<Banner>>> displayBanner() {
     List<Banner> banners = bannerService.displayBanners();
    return new ResponseEntity<>(new ResponseData<>("success",banners, HttpStatus.OK ), HttpStatus.OK);

    }
}
