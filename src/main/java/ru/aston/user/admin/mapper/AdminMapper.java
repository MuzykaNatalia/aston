package ru.aston.user.admin.mapper;

import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.aston.user.admin.dto.AdminDto;
import ru.aston.user.admin.entity.Admin;

@Component
public class AdminMapper {
    public Collection<AdminDto> toCollectionAdminDto(Collection<Admin> allAdmin) {
        return allAdmin.stream().map(this::toAdminDto).collect(Collectors.toList());
    }

    public AdminDto toAdminDto(Admin admin) {
        return AdminDto.builder()
                .name(admin.getName())
                .email(admin.getEmail())
                .adminLevel(admin.getAdminLevel())
                .build();
    }

    public Admin toAdminForCreate(AdminDto adminDto) {
        Admin admin = new Admin();
        admin.setName(adminDto.getName());
        admin.setEmail(adminDto.getEmail());
        admin.setAdminLevel(adminDto.getAdminLevel());
        return admin;
    }

    public Admin toAdminForUpdate(AdminDto adminDto, long adminId) {
        Admin admin = new Admin();
        admin.setId(adminId);
        admin.setName(adminDto.getName());
        admin.setEmail(adminDto.getEmail());
        admin.setAdminLevel(adminDto.getAdminLevel());
        return admin;
    }
}
