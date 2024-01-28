package com.example.back.repository;

import com.example.back.entity.Region;
import com.example.back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByUserAndAndRegionName(User user, String regionName);
    Optional<List<Region>> findAllByUser(User user);

    @Query(value = "select r from Region r where r.user.id = :id")
    Region findByRegionId(Long id);

    @Query(value = "select r from Region r where r.id = :id")
    List<Region> findByRegion(Long id);

    Optional<Region> findByUser_Id(Long userId);

}
