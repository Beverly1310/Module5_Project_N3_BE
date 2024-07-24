package com.ra.service;

import com.ra.model.dto.res.BrandFormResponse;
import com.ra.model.dto.res.ColorFormResponse;

import java.util.List;

public interface IColorService {
    List<ColorFormResponse> getAllForInput();

}
