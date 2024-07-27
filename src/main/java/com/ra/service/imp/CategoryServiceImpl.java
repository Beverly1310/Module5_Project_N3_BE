package com.ra.service.imp;

import com.ra.model.dto.req.CategoryForm;
import com.ra.model.dto.res.CategoryFormResponse;
import com.ra.model.dto.res.CategoryWithProductsDTO;
import com.ra.model.dto.res.ProductResponse;
import com.ra.model.entity.Category;
import com.ra.model.entity.Product;
import com.ra.repository.CategoryRepository;
import com.ra.repository.ProductRepository;
import com.ra.service.ICategoryService;
import com.ra.service.IProductService;
import com.ra.util.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final IProductService productService;
    private final FileUploadService uploadService;

    @Override
    public Page<Category> findCategoriesByKeyword(String keyword, int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return categoryRepository.findByKeyword(keyword, pageable);
    }

    @Override
    public CategoryForm getCategoryToUpdate(long id) {
        Category category = categoryRepository.findById(id).get();
        return CategoryForm.builder().id(id)
                .image(category.getImage())
                .description(category.getDescription())
                .categoryName(category.getCategoryName())
                .build();
    }

    @Override
    public void add(CategoryForm categoryForm) {
        String imgLink = uploadService.uploadFileToServer(categoryForm.getImageFile());
        Category category = Category.builder().categoryName(categoryForm.getCategoryName())
                .description(categoryForm.getDescription())
                .image(imgLink)
                .createdAt(LocalDate.now())
                .status(true)
                .build();
        categoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        if (!productRepository.existsByCategoryId(id)) {
            categoryRepository.deleteById(id);
        }
    }

    @Override
    public void update(CategoryForm categoryForm) {
        Category category = categoryRepository.findById(categoryForm.getId()).get();
        category.setCategoryName(categoryForm.getCategoryName());
        category.setDescription(categoryForm.getDescription());
        if (categoryForm.getImageFile()!=null) {
            String imgLink = uploadService.uploadFileToServer(categoryForm.getImageFile());
            category.setImage(imgLink);
        }
        categoryRepository.save(category);
    }

    @Override
    public Optional<CategoryWithProductsDTO> findCategoryWithProducts(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {

            List<ProductResponse> responses =productService.findProductsByCategory(categoryId);
            String message = responses.isEmpty() ? "No products found for this category." : null;
            return Optional.of(new CategoryWithProductsDTO(category.get(), responses, message));
        }
        return Optional.empty();
    }

    @Override
    public List<CategoryFormResponse> getAllForInput() {
        List<Category> categories=categoryRepository.findAll();
        List<CategoryFormResponse> responses=new ArrayList<>();
        for (Category category:categories){
            CategoryFormResponse categoryForm= CategoryFormResponse.builder()
                    .categoryId(category.getId())
                    .categoryName(category.getCategoryName()).
                    build();
            responses.add(categoryForm);
        }
        return responses;
    }

}
