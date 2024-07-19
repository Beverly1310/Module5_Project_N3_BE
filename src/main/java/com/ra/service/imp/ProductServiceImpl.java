package com.ra.service.imp;

import com.ra.model.dto.req.ProductForm;
import com.ra.model.dto.res.ProductResponse;
import com.ra.model.entity.Brand;
import com.ra.model.entity.Category;
import com.ra.model.entity.Image;
import com.ra.model.entity.Product;
import com.ra.repository.BrandRepository;
import com.ra.repository.CategoryRepository;
import com.ra.repository.ImageRepository;
import com.ra.repository.ProductRepository;
import com.ra.service.IProductService;
import com.ra.util.FileUploadService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ImageRepository imageRepository;
    private final FileUploadService fileUploadService;

    @Override
    public Page<ProductResponse> findProductsByKeyword(String keyword, int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productRepository.findByKeyword(keyword, pageable);

        return products.map(this::getResponse);
    }

    @Override
    @Transactional
    public void add(ProductForm productForm) {
        Category category = categoryRepository.findById(productForm.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        Brand brand = brandRepository.findById(productForm.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid brand ID"));

        String img = fileUploadService.uploadFileToServer(productForm.getImageFile());
        boolean isSaved = false;
        while (!isSaved) {
            String sku = UUID.randomUUID().toString();
            Product product = Product.builder()
                    .sku(sku)
                    .productName(productForm.getProductName())
                    .description(productForm.getDescription())
                    .image(img)
                    .category(category)
                    .brand(brand)
                    .createdAt(LocalDate.now())
                    .updatedAt(LocalDate.now())
                    .build();
            List<Image> imageList = new ArrayList<>();
            for (MultipartFile image : productForm.getImageFileList()) {
                String src = fileUploadService.uploadFileToServer(image);
                Image newImage = Image.builder()
                        .src(src)
                        .product(product).build();
                imageList.add(newImage);
            }
            try {
                productRepository.save(product);
                if (!imageList.isEmpty()) {
                    imageRepository.saveAll(imageList);
                }
                isSaved = true;
            } catch (DataIntegrityViolationException e) {
                System.out.println("SKU conflict detected, generating a new SKU and retrying...");
            }
        }

    }

    @Override
    @Transactional
    public void delete(Long id) {
        imageRepository.deleteAllByProductId(id);
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponse> findProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream().map(this::getResponse).toList();

    }

    @Override
    public ProductForm getProductToUpdate(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        List<Image> imageList = imageRepository.findAllByProductId(id);
        return ProductForm.builder()
                .brandId(product.getBrand().getId())
                .categoryId(product.getCategory().getId())
                .image(product.getImage())
                .imageList(imageList)
                .productName(product.getProductName())
                .description(product.getDescription())
                .build();
    }

    @Override
    public void removeImageFromForm(ProductForm productForm, Long imageId) {
        List<Image> images = productForm.getImageList();
        images.remove(imageRepository.findById(imageId).get());
        productForm.setImageList(images);
    }

    @Override
    @Transactional
    public void update(ProductForm productForm) {
        Product product = productRepository.findById(productForm.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        Category category = categoryRepository.findById(productForm.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        Brand brand = brandRepository.findById(productForm.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid brand ID"));

        // Handle the main image update
        String image = productForm.getImage();
        if (productForm.getImageFile() != null && !productForm.getImageFile().isEmpty()) {
            image = fileUploadService.uploadFileToServer(productForm.getImageFile());
        }

        // Handle the additional images
        List<MultipartFile> newFiles = productForm.getImageFileList();
        List<Image> existingImages = imageRepository.findAllByProductId(productForm.getId());

        // Add new images
        List<Image> newImages = new ArrayList<>();
        for (MultipartFile file : newFiles) {
            String src = fileUploadService.uploadFileToServer(file);
            newImages.add(Image.builder().product(product).src(src).build());
        }

        // Remove images that are not in the new list
        for (Image img : existingImages) {
            if (!newImages.contains(img)) {
                imageRepository.delete(img);
            }
        }

        // Update the product details
        product.setProductName(productForm.getProductName());
        product.setDescription(productForm.getDescription());
        product.setImage(image);
        product.setCategory(category);
        product.setBrand(brand);
        product.setUpdatedAt(LocalDate.now());

        // Save updated product and new images
        productRepository.save(product);
        if (!newImages.isEmpty()) {
            imageRepository.saveAll(newImages);
        }
    }

    @Override
    public ProductResponse findById(Long id) {
        return null;
    }

    @Override
    public List<ProductResponse> getNewest() {
        return null;
    }

    @Override
    public Page<ProductResponse> getAllProductsOnSale(String keyword, int page, int size, String sortBy, String sortDirection) {
        return null;
    }

    @Override
    public List<ProductResponse> getMostSold() {
        return null;
    }

    @Override
    public List<ProductResponse> getMostNotable() {
        return null;
    }

    @Override
    public ProductResponse getResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .sku(product.getSku())
                .productName(product.getProductName())
                .description(product.getDescription())
                .categoryName(product.getCategory().getCategoryName())
                .brandName(product.getBrand().getBrandName())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .image(product.getImage())
                .build();
    }
}
