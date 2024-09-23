package ru.aston.user.admin.service;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.exception.NotFoundException;
import ru.aston.user.admin.dto.AdminDto;
import ru.aston.user.admin.entity.Admin;
import ru.aston.user.admin.mapper.AdminMapper;
import ru.aston.user.admin.repository.AdminRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    @Transactional(readOnly = true)
    @Override
    public Collection<AdminDto> getAllAdmin(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Order.asc("id")));
        Collection<Admin> allAdmins = adminRepository.getAllAdmin(pageable);
        return adminMapper.toCollectionAdminDto(allAdmins);
    }

    @Transactional(readOnly = true)
    @Override
    public AdminDto getAdminById(long adminId) {
        Admin admin = adminRepository.getAdminById(adminId);
        if (admin == null) {
            throw new NotFoundException("Admin not found ID " + adminId);
        }

        return adminMapper.toAdminDto(admin);
    }

    @Transactional
    @Override
    public AdminDto createAdmin(AdminDto admin) {
        Admin createdAdmin = adminRepository.createAdmin(adminMapper.toAdminForCreate(admin));
        log.info("Admin with ID {} created", createdAdmin.getId());
        return adminMapper.toAdminDto(createdAdmin);
    }

    @Transactional
    @Override
    public AdminDto updateAdmin(long adminId, AdminDto admin) {
        Admin existingAdmin = adminRepository.getAdminById(adminId);
        if (existingAdmin == null) {
            throw new NotFoundException("Admin not found ID " + adminId);
        }

        if (admin.getName() != null && !admin.getName().isBlank()) {
            existingAdmin.setName(admin.getName());
        }
        if (admin.getEmail() != null) {
            existingAdmin.setEmail(admin.getEmail());
        }
        if (admin.getAdminLevel() > 0) {
            existingAdmin.setAdminLevel(admin.getAdminLevel());
        }


        Admin updatedAdmin = adminRepository.updateAdmin(existingAdmin);
        log.info("Admin with ID {} updated", adminId);
        return adminMapper.toAdminDto(updatedAdmin);
    }

    @Transactional
    @Override
    public void deleteAdmin(long adminId) {
        adminRepository.deleteAdmin(adminId);
        log.info("Admin with ID {} deleted", adminId);
    }
}
