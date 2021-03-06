package com.reviewtrungtam.webapp.center.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import com.reviewtrungtam.webapp.center.entity.Center;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CenterRepository extends PagingAndSortingRepository<Center, Integer> {
    Center findBySlugNameAndStatus(String slugName, int status);

    Center findByIdAndStatus(int id, int status);

    long countByStatus(int status);

    Page<Center> findAllByNameLike(String name, Pageable pageRequest);

    Page<Center> findAllBySlugNameLike(String name, Pageable pageRequest);

    @Query(value = "SELECT c, SUM(r.rating) AS total FROM Center c " +
            "JOIN Review r ON r.center = c " +
            "WHERE c.id = ?1", nativeQuery = true)
    Center findByIdAndTotalPoint(int id);
}
