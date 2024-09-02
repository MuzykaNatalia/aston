package ru.aston.user.admin.service;

import java.util.Collection;
import ru.aston.user.admin.dto.AdminDto;
import ru.aston.user.admin.entity.Admin;
import ru.aston.user.admin.mapper.AdminMapper;
import ru.aston.user.admin.repository.AdminRepository;

public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper = new AdminMapper();

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Collection<AdminDto> getAllAdmin() {
        return adminMapper.toCollectionAdminDto(adminRepository.getAllAdmin());
    }

    @Override
    public AdminDto getAdminById(long adminId) {
        Admin admin = adminRepository.getAdminById(adminId);
        if (admin == null) {
            throw new RuntimeException("Admin not found");
        }

        return adminMapper.toAdminDto(admin);
    }

    @Override
    public AdminDto createAdmin(AdminDto admin) {
        validate(admin);
        Admin createdAdmin = adminRepository.createAdmin(adminMapper.toAdminForCreate(admin));
        return adminMapper.toAdminDto(createdAdmin);
    }

    @Override
    public AdminDto updateAdmin(long adminId, AdminDto admin) {
        validate(admin);
        Admin updatedAdmin = adminRepository.updateAdmin(adminMapper.toAdminForUpdate(admin, adminId));
        if (updatedAdmin == null) {
            throw new RuntimeException("Admin not found");
        }
        return adminMapper.toAdminDto(updatedAdmin);
    }

    @Override
    public void deleteAdmin(long adminId) {
        adminRepository.deleteAdmin(adminId);
    }

    private void validate(AdminDto admin) {
        if (admin == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (admin.getName() == null || (admin.getName() != null && admin.getName().isBlank())) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (admin.getEmail() == null || (admin.getEmail() != null && admin.getEmail().isBlank())) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (admin.getEmail() != null && !admin.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email is not valid");
        }
        if (admin.getAdminLevel() <= 0) {
            throw new IllegalArgumentException("Admin level cannot be null");
        }
    }
}
