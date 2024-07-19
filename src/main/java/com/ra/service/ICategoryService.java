package com.ra.service;

import com.ra.model.dto.req.CategoryForm;
import com.ra.model.dto.res.CategoryWithProductsDTO;
import com.ra.model.entity.Category;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ICategoryService {
    Page<Category> findCategoriesByKeyword(String keyword, int page, int size, String sortBy, String sortDirection);

    CategoryForm getCategoryToUpdate(long id);
    void add(CategoryForm categoryForm);
    void delete (Long id);
    void update(CategoryForm updateCategoryForm);
    Optional<CategoryWithProductsDTO> findCategoryWithProducts(Long categoryId);

}
