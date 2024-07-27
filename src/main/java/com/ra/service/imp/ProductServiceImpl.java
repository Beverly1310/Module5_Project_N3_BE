package com.ra.service.imp;

import com.ra.model.dto.req.ProductForm;
import com.ra.model.dto.res.ImageFormResponse;
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
import java.util.stream.Collectors;

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
                    .updatedAt(LocalDate.now()).status(true)
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
        List<ImageFormResponse> responses = new ArrayList<>();
        List<Long> idResponses = new ArrayList<>();
        for (Image image : imageList) {
            responses.add(new ImageFormResponse(image.getId(), image.getSrc()));
            idResponses.add(image.getId());
        }
        return ProductForm.builder()
                .brandId(product.getBrand().getId())
                .categoryId(product.getCategory().getId())
                .image(product.getImage()).imageList(responses).imageIdList(idResponses)
                .productName(product.getProductName())
                .description(product.getDescription())
                .build();
    }

    @Override
    public void removeImageFromForm(ProductForm productForm, Long imageId) {

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

        // Update the main image
        String image = product.getImage();
        if (productForm.getImageFile() != null && !productForm.getImageFile().isEmpty()) {
            image = fileUploadService.uploadFileToServer(productForm.getImageFile());
        }
        product.setImage(image);

        // Get existing images for this product
        List<Image> existingImages = imageRepository.findAllByProductId(productForm.getId());

        // Prepare to add new images
        List<MultipartFile> files = productForm.getImageFileList();
        if (files != null) {
            for (MultipartFile file : files) {
                String src = fileUploadService.uploadFileToServer(file);
                Image newImage = Image.builder().src(src).product(product).build();
                imageRepository.save(newImage);
            }
        }

        // Remove images that are no longer in the list
        List<Long> currentImageIdList = productForm.getImageIdList();
//        if (currentImageIdList!=null){
            for (Image existingImage : existingImages) {
                if (currentImageIdList.stream().noneMatch(id -> id.equals(existingImage.getId()))) {
                    imageRepository.delete(existingImage);
                }
            }

//        }

        // Update the product details
        product.setProductName(productForm.getProductName());
        product.setDescription(productForm.getDescription());
        product.setCategory(category);
        product.setBrand(brand);
        product.setUpdatedAt(LocalDate.now());

        // Save updated product
        productRepository.save(product);


    }


    @Override
    public ProductResponse findById(Long id) {
        return null;
    }

    @Override
    public List<ProductResponse> getNewest() {
        List<Product> list = productRepository.getNewest();
        List<ProductResponse> newestProducts = new ArrayList<>();
        for (Product product : list) {
            newestProducts.add(getResponse(product));
        }
        return newestProducts;    }

    @Override
    public Page<ProductResponse> getAllProductsOnSale(String keyword, int page, int size, String sortBy, String sortDirection) {
        return null;
    }

    @Override
    public List<ProductResponse> getMostSold() {
        List<Product> topProducts = productRepository.findTop5MostSoldProducts();
        return topProducts.stream()
                .map(this::getResponse)
                .collect(Collectors.toList());
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
                .image(product.getImage()).status(product.isStatus())
                .build();
    }
}
