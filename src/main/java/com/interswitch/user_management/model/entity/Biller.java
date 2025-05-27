package com.interswitch.user_management.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "billers")
public class Biller {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String billerId =  UUID.randomUUID().toString();
  private String billerName;

  @OneToMany(mappedBy = "biller", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Product> productList;

  @ManyToOne
  @JoinColumn(name = "category_id")
  @JsonIgnore
  private Category category;

  @CreationTimestamp private Timestamp createdAt;

  @UpdateTimestamp private Timestamp updatedAt;
    }