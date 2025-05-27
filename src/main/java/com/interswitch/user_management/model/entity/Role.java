package com.interswitch.user_management.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interswitch.user_management.constant.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "roles")
@Data
public class Role {
    private static final long serialVersionUID = 1L;

    public Role(){

    }
    public Role(String name){this.setName(name);
    }
    public Role(String name, String description){
    	this.setName(name);
    	this.setDescription(description);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    private String uuid;
    @Column(unique = true, nullable = false)
    @NotNull(message = "Name Cannot be null")
    @Size(min=3)
    private String name;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    private User user;
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    private Set<Permission> permissions;
    @Lob
    @Column(name="description")
    private String description;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    private boolean enabled;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    private boolean active = false;

    public Collection<String> getPermissionNames() {
        if (permissions == null) {
            permissions = new HashSet<>();
        }

        return permissions.stream().map(p -> p.getName()).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "Role{" + "id=" + this.getId() + "name=" + this.getName() + ", description=" + description + ", enabled=" + enabled + '}';
    }
}
