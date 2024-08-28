package ru.aston.user.admin.service;

import java.util.Collection;
import ru.aston.user.admin.dto.AdminDto;

public interface AdminService {
    Collection<AdminDto> getAllAdmin();

    AdminDto getAdminById(long adminId);

    AdminDto createAdmin(AdminDto admin);

    AdminDto updateAdmin(long adminId, AdminDto admin);

    void deleteAdmin(long adminId);
}
