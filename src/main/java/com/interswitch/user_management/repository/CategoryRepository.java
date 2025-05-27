package com.interswitch.user_management.repository;

import com.interswitch.user_management.model.entity.Category;
import com.interswitch.user_management.model.request.CategoryRequest;
import com.interswitch.user_management.model.response.GlobalResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByCategoryId(String categoryId);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.billers WHERE c.categoryId = :categoryId")
    Category findCategoryWithBillers(@Param("categoryId") String categoryId);

}
