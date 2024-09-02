package ru.aston.user.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.aston.user.parent.entity.User;

@Entity
@Table(name = "administrator")
@NoArgsConstructor
@Getter
@Setter
public class Admin extends User {
    @Column(name = "admin_level", nullable = false)
    private int adminLevel;
}
