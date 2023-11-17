package com.example.back.repository;

import com.example.back.entity.Product;
import com.example.back.entity.Region;
import com.example.back.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByRegionAndUser(Region region, User user);
    Optional<List<Product>> findAllByUser(User user);
    //Product List 조회, 지역명 검색 가능
    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN FETCH p.region r " +
            "JOIN FETCH p.user u " +
            "JOIN FETCH p.productImages pi " +
            "WHERE r.regionName LIKE %:regionName% " +
            "ORDER BY p.regTime DESC ")
    Slice<Product> findByRegion_RegionNameContainsOrderByRegTimeDesc(@Param("regionName") String regionName, Pageable pageable);
    // Product List의  조회수, 관심수 조회, 최신 순서 정렬
    @Query("SELECT p, COUNT(sp), COUNT(CASE WHEN a.status = 'Y' THEN a ELSE NULL END) FROM Product p " +
            "LEFT JOIN p.selectProducts sp " +
            "LEFT JOIN p.attentions a " +
            "WHERE p IN :products " +
            "GROUP BY p " +
            "ORDER BY p.regTime DESC")
    List<Object[]> findProductsAndCount(List<Product> products);

    @Query("SELECT DISTINCT p " +
            "FROM Product p " +
            "JOIN FETCH p.region r " +
            "JOIN FETCH p.user u " +
            "LEFT JOIN FETCH p.productImages pi " +
            "WHERE p.pdTitle LIKE %:productTitle% " +
            "ORDER BY p.regTime DESC")
    Slice<Product> findProductsByProductName(String productTitle, Pageable pageable);

}
