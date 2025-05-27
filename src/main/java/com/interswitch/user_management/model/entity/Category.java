package com.interswitch.user_management.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryName;

    @Column(name = "category_id", unique = true, nullable = false, updatable = false)
    @JsonManagedReference
    private String categoryId = UUID.randomUUID().toString();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    @ToString.Exclude
    private List<Biller> billers;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @PrePersist
    public void generateCategoryId() {
        if (categoryId == null) {
            categoryId = UUID.randomUUID().toString();
        }
    }

    // Helper method to set bidirectional relationship
    public void addBiller(Biller biller) {
        billers.add(biller);
        biller.setCategory(this);
    }

    public void removeBiller(Biller biller) {
        billers.remove(biller);
        biller.setCategory(null);
    }
}
