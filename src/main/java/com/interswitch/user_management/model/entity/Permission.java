package com.interswitch.user_management.model.entity;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "permissions")
@NoArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected boolean active;

    @Lob
    @Column(name = "description")
    private String description;

    private String name;

    public Permission(String name){
        this.setName(name);
    }

    public Permission(String name, String description){
        this.setName(name);
        this.setDescription(description);
    }

    public Permission(Long id){
        this.setId(id);
    }

    public void setName(String name) {
        this.name = name.startsWith("ROLE_") ? name : "ROLE_" + name;
        this.name = this.name.trim().toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Permission)) return false;
        Permission that = (Permission) o;
        return this == o
                || Objects.equals(this.getId(), that.getId())
                || (Objects.equals(active, that.active) && Objects.equals(getName(), that.getName()));
    }


    @Override
    public int hashCode() {
        return Objects.hash(getName(), active);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "name:" + this.getName() + "id:" + this.getId();
    }
}
