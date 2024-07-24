package com.ra.service.imp;

import com.ra.model.dto.req.ProductDetailRequest;
import com.ra.model.dto.req.ProductForm;
import com.ra.model.dto.res.ImageFormResponse;
import com.ra.model.dto.res.ProductResponse;
import com.ra.model.entity.*;
import com.ra.repository.*;
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
    private final ProductDetailRepository productDetailRepository;
    private final ColorRepository colorRepository;

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
                if (productForm.getDetailRequests() != null) {
                    for (ProductDetailRequest detailRequest : productForm.getDetailRequests()) {
                        ProductDetail productDetail = new ProductDetail();
                        String src = fileUploadService.uploadFileToServer(detailRequest.getImageFile());
                        productDetail.setImage(detailRequest.getImage());
                        productDetail.setProductDetailName(detailRequest.getProductDetailName());
                        productDetail.setStatus(true);
                        productDetail.setStock(detailRequest.getStock());
                        productDetail.setUnitPrice(detailRequest.getUnitPrice());
                        Color color = colorRepository.findById(detailRequest.getColorId()).orElseThrow();
                        productDetail.setColor(color);
                        productDetail.setProduct(product);
                        productDetailRepository.save(productDetail);
                    }
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
        List<ProductDetail> productDetails = productDetailRepository.findAllByProductId(id);
        List<ProductDetailRequest> productDetailRequests = new ArrayList<>();
        for (ProductDetail productDetail : productDetails) {
            productDetailRequests.add(ProductDetailRequest.builder()
                    .productDetailId(productDetail.getId())
                    .image(productDetail.getImage())
                    .stock(productDetail.getStock())
                    .unitPrice(productDetail.getUnitPrice())
                    .colorId(productDetail.getColor().getId())
                    .build());
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

        // Update product details
        if (productForm.getDetailRequests() == null || productForm.getDetailRequests().isEmpty()) {
            // Remove existing details if removing all
            productDetailRepository.deleteAllByProductId(productForm.getId());
        } else {
            List<ProductDetail> currentDetailList = productDetailRepository.findAllByProductId(productForm.getId());
            List<ProductDetailRequest> requests = productForm.getDetailRequests();
//            Remove deleted details
            for (ProductDetail detail : currentDetailList) {
                if (requests.stream().noneMatch(request->request.getProductDetailId().equals(detail.getId()))){
                    productDetailRepository.delete(detail);
                }
            }
            for (ProductDetailRequest detailRequest : requests) {
//               Add new details
                if (!productDetailRepository.existsByProductIdAndColorId(productForm.getId(), detailRequest.getColorId())) {
                    ProductDetail productDetail = new ProductDetail();
                    productDetail.setId(detailRequest.getProductDetailId()); // For update, otherwise skip if new
                    productDetail.setImage(fileUploadService.uploadFileToServer(detailRequest.getImageFile()));
                    productDetail.setProductDetailName(detailRequest.getProductDetailName());
                    productDetail.setStatus(detailRequest.isStatus());
                    productDetail.setStock(detailRequest.getStock());
                    productDetail.setUnitPrice(detailRequest.getUnitPrice());
                    Color color = colorRepository.findById(detailRequest.getColorId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid color ID"));
                    productDetail.setColor(color);
                    productDetail.setProduct(product);
                    productDetailRepository.save(productDetail);
                } else {
//                    Update existing details
                    ProductDetail productDetail = productDetailRepository.findById(detailRequest.getProductDetailId()).get();
                    if (detailRequest.getImageFile().getSize() > 0) {
                        productDetail.setImage(fileUploadService.uploadFileToServer(detailRequest.getImageFile()));
                    }
                    productDetail.setProductDetailName(detailRequest.getProductDetailName());
                    productDetail.setStatus(detailRequest.isStatus());
                    productDetail.setStock(detailRequest.getStock());
                    productDetail.setUnitPrice(detailRequest.getUnitPrice());
                    productDetailRepository.save(productDetail);
                }
            }
        }
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
        Product product = productRepository.findById(id).get();
        return getResponse(product);
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
