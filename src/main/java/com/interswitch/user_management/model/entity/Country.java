package com.interswitch.user_management.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "countries")
@Data
public class Country {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    private String uuid;
    @Column(unique = true, nullable = false)
    @NotNull(message = "English Name Cannot be null")
    @Size(min=3)
    private String name;
    @Column(unique = true, nullable = false)
    @NotNull(message = "Spanish Name Cannot be null")
    @Size(min=3)
    private String officialNameEs;
    @Column(unique = true, nullable = false)
    @NotNull(message = "French Name Cannot be null")
    @Size(min=3)
    private String officialNameFr;
    @Column(unique = true, nullable = false)
    @Size(min = 2, max = 2)
    private String alpha2Code;
    @Size(min = 3, max = 3)
    private String alpha3Code;
    private String currencyCode;
    private String currencyName;
    private String currencyNumericCode;
    private String capital;
    private String region;
    private String subRegion;
    private String otherCurrencies;
    private boolean canTransact;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    @PrePersist
    public void onPrePersistChild(){
        if(name != null){
            this.name =name.toUpperCase();
        }
    }

    @PreUpdate
    public void onPreUpdateChild(){
        if(name != null){
            this.name =name.toUpperCase();
        }
    }

    @Override
    public String toString() {
        return "Country{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", officialNameEs='" + officialNameEs + '\'' +
                ", officialNameFr='" + officialNameFr + '\'' +
                ", alpha2Code='" + alpha2Code + '\'' +
                ", alpha3Code='" + alpha3Code + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", currencyName='" + currencyName + '\'' +
                ", currencyNumericCode='" + currencyNumericCode + '\'' +
                ", capital='" + capital + '\'' +
                ", region='" + region + '\'' +
                ", subRegion='" + subRegion + '\'' +
                ", otherCurrencies='" + otherCurrencies + '\'' +
                ", canTransact=" + canTransact +
                '}';
    }
}
