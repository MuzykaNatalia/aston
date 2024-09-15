package ru.aston.user.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.aston.user.parent.entity.User;

@Entity
@Table(name = "administrator")
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends User {
    @Column(name = "admin_level", nullable = false)
    private int adminLevel;
}
