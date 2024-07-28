package com.ra.repository;

import com.ra.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByCategoryId(long categoryId);
    @Query("SELECT p FROM Product p WHERE p.productName LIKE %:keyword% OR p.description LIKE %:keyword%")
    Page<Product> findByKeyword(String keyword, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.category.id=:id and ( p.productName LIKE %:keyword% OR p.description LIKE %:keyword%)")
    Page<Product> findByKeywordAndCategory(String keyword, Long id, Pageable pageable);
    List<Product> findByCategoryId(long id);
    @Query("select p from Product p where p.category.id=:categoryId")
    List<Product> findProductsByCategory(Long categoryId);
    @Query("select p from Product p order by p.createdAt desc limit 5")
    List<Product> getNewest();

    @Query("SELECT p FROM OrderDetail od JOIN od.id.productDetail.product p GROUP BY p.id ORDER BY SUM(od.orderQuantity) DESC limit 5")
    List<Product> findTop5MostSoldProducts();
    @Query("SELECT p FROM Product p WHERE p.id!=:productId and (p.category.categoryName = :categoryName OR p.brand.brandName = :brandName)")
    List<Product> findRelatedProducts(Long productId, String categoryName,String brandName,Pageable pageable);
}
