package com.ra.service.imp;

import com.ra.model.entity.Banner;
import com.ra.repository.BannerRepository;
import com.ra.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {
    private final BannerRepository bannerRepository;

    @Override
    public List<Banner> displayBanners() {
        List<Banner> banners = bannerRepository.findAll();
        Collections.reverse(banners);
        if (banners.isEmpty()) {
            return banners;
        } else if (banners.size() < 5) {
            banners = banners.stream().limit(banners.size()).toList();

        } else {
            banners = banners.stream().limit(5).toList();

        }
        return banners;
    }
}
