package ru.aston.user.admin.repository;

import java.util.Collection;
import ru.aston.user.admin.entity.Admin;

public interface AdminRepository {
    Collection<Admin> getAllAdmin();

    Admin getAdminById(long adminId);

    Admin createAdmin(Admin admin);

    void deleteAdmin(long adminId);

    Admin updateAdmin(Admin existingAdmin);
}
