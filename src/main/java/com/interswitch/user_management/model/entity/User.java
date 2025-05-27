package com.interswitch.user_management.model.entity;

import com.fasterxml.jackson.annotation.*;
import com.interswitch.user_management.constant.UserType;
import com.interswitch.user_management.util.WebUtility;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "users")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public User(){

    }

    public User(final String lastName, final String firstName, final String email, final String password, final boolean enabled, String username) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.username = username;
    }

    public User(long id, final String lastName, final String firstName, final String email, final String password, final boolean enabled) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    private String uuid;
    @Column(name = "username", unique = true)
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String lastName;
    private String firstName;
    @Column(unique = true)
    private String email;
    private String phoneNumber;
    @Column(nullable = true, unique = true)
    private String fullPhoneNumber;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private String jobTitle;
    private String nationality;
    private boolean enabled;
    private boolean accountNonLocked;
    private boolean deleted;
    private boolean passwordChanged;
    @OneToOne(targetEntity = Role.class, fetch = FetchType.EAGER)
    private Role role;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private Set<Address> addresses;
    private String documentNumber;
    private String personaMobileNumber;
    @Basic
    @Column(nullable = false)
    private Timestamp createdAt;

    @Basic
    @Column(nullable = true)
    private Timestamp updatedAt;
    private boolean active = false;

    public void delete(){
        active = false;
    }

    public void activate(){
        active = true;
    }

    public String getNames() {
        return lastName + " " + firstName;
    }


    public Collection<String> getPermissionNames() {
        return role.getPermissions()
                .stream()
                .map(perm -> perm.getName())
                .collect(Collectors.toSet());
    }

    @PrePersist
    public void beforeSave() {
        if (!WebUtility.isBcryptHashed(this.password)) {
            this.setPassword(PASSWORD_ENCODER.encode(this.password));
        }
        this.createdAt = new Timestamp(new Date().getTime());
    }

    @PreUpdate
    private void beforeUpdate() {
        this.updatedAt = new Timestamp(new Date().getTime());
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", passwordChanged=" + passwordChanged +
                ", deleted=" + deleted +
                ", accountNonLocked=" + accountNonLocked +
                ", enabled=" + enabled +
                ", nationality='" + nationality + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", userType=" + userType +
                ", fullPhoneNumber='" + fullPhoneNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
