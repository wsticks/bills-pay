package com.interswitch.user_management.model.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@Table(name = "addresses")
@NoArgsConstructor
public class Address {

    public Address(String addressLine1, String city, String zipCode, Country country) {
        this.addressLine1 = addressLine1;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    private String uuid;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String zipCode;
    private String state;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Country country;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
