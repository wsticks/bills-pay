package com.interswitch.user_management.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.interswitch.user_management.constant.UserType;
import com.interswitch.user_management.model.entity.Address;
import com.interswitch.user_management.model.entity.User;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoggedInUserResponse {

    public LoggedInUserResponse(User user) {
        username = user.getUsername();
        names = user.getNames();
        lastName = user.getLastName();
        firstName = user.getFirstName();
        email = user.getEmail();
        phoneNumber = user.getPhoneNumber();
        fullPhoneNumber = user.getFullPhoneNumber();
        userType = user.getUserType();
        jobTitle = user.getJobTitle();
        nationality = user.getNationality();
        enabled = user.isEnabled();
        accountNonLocked = user.isAccountNonLocked();
        deleted = user.isDeleted();
        passwordChanged = user.isPasswordChanged();
        permissions = user.getPermissionNames();
        role = user.getRole().getName();
        uuid = user.getUuid();

        final Optional<Address> optionalAddress = user.getAddresses().stream().findFirst();
        if (optionalAddress.isPresent()) {
            address = optionalAddress.get().getAddressLine1();
            city = optionalAddress.get().getCity();
            region = optionalAddress.get().getState();
            zipCode = optionalAddress.get().getZipCode();
            country = optionalAddress.get().getCountry().getAlpha2Code();
        }
    }

    private String username;

    private String names;

    private String lastName;

    private String firstName;

    private String email;

    private String phoneNumber;

    private String callingCode;

    private String fullPhoneNumber;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String jobTitle;
    private String nationality;
    private boolean enabled;
    private boolean accountNonLocked;
    private boolean deleted;
    private boolean passwordChanged;
    private boolean profileUpdated;
    private boolean businessDocumentUpdated;
    private boolean idUpdated;
    private boolean businessAccountApproved;
    private boolean pinSet;
    private String referralCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date lastLoggedIn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date lastPasswordResetDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dateOfBirth;

    private String address, city, region, zipCode, country;

    private String identificationNumber;

    private String idIssuerCountry;

    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date idIssueDate;

    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date idExpiryDate;

    private Collection<String> permissions;
    private String role;
    private String uuid;
}
