package ru.aston.user.admin.repository;

import java.util.Collection;
import org.springframework.data.domain.Pageable;
import ru.aston.user.admin.entity.Admin;

public interface AdminRepository {
    Collection<Admin> getAllAdmin(Pageable pageable);

    Admin getAdminById(long adminId);

    Admin createAdmin(Admin admin);

    void deleteAdmin(long adminId);

    Admin updateAdmin(Admin admin);
}
