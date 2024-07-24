package com.ra.service.imp;

import com.ra.model.dto.res.BrandFormResponse;
import com.ra.model.dto.res.ColorFormResponse;
import com.ra.model.entity.Brand;
import com.ra.model.entity.Color;
import com.ra.repository.ColorRepository;
import com.ra.service.IColorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ColorServiceImpl implements IColorService {
    private final ColorRepository colorRepository;
    @Override
    public List<ColorFormResponse> getAllForInput() {
        List<Color> colors=colorRepository.findAll();
        List<ColorFormResponse> responses=new ArrayList<>();
        for (Color color:colors){
            ColorFormResponse response= ColorFormResponse.builder()
                    .colorId(color.getId())
                    .colorName(color.getColorName()).build();
            responses.add(response);
        }
        return responses;
    }
}
