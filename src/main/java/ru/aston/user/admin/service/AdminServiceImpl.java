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
        validateId(adminId);
        Admin admin = adminRepository.getAdminById(adminId);
        return adminMapper.toAdminDto(admin);
    }

    @Override
    public AdminDto createAdmin(AdminDto admin) {
        if (admin.getName() == null || admin.getEmail() == null) {
            throw new IllegalArgumentException("Name, email cannot be null or empty");
        }
        if (admin.getAdminLevel() <= 0) {
            throw new IllegalArgumentException("Admin level cannot be null");
        }
        validate(admin);
        Admin createdAdmin = adminRepository.createAdmin(adminMapper.toAdminForCreate(admin));
        return adminMapper.toAdminDto(createdAdmin);
    }

    @Override
    public AdminDto updateAdmin(long adminId, AdminDto admin) {
        validateId(adminId);
        validate(admin);

        Admin existingAdmin = adminRepository.getAdminById(adminId);
        if (admin.getName() != null) {
            existingAdmin.setName(admin.getName());
        }
        if (admin.getEmail() != null) {
            existingAdmin.setEmail(admin.getEmail());
        }
        if (admin.getAdminLevel() > 0) {
            existingAdmin.setAdminLevel(admin.getAdminLevel());
        }

        Admin updatedAdmin = adminRepository.updateAdmin(existingAdmin);
        return adminMapper.toAdminDto(updatedAdmin);
    }

    @Override
    public void deleteAdmin(long adminId) {
        validateId(adminId);
        adminRepository.deleteAdmin(adminId);
    }

    private void validate(AdminDto admin) {
        if (admin.getName() != null && admin.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (admin.getEmail() != null && admin.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (admin.getEmail() != null && !admin.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email is not valid");
        }
    }

    private void validateId(long adminId) {
        if (adminId <= 0) {
            throw new IllegalArgumentException("Admin ID must be greater than zero");
        }
    }
}
