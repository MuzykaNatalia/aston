package ru.aston.user.admin.repository;

import java.util.Collection;
import ru.aston.common.AbstractRepository;
import ru.aston.user.admin.entity.Admin;

public class AdminRepositoryImpl extends AbstractRepository<Admin, Long> implements AdminRepository {
    public AdminRepositoryImpl() {
        super(Admin.class);
    }

    @Override
    public Collection<Admin> getAllAdmin() {
        return super.getAll();
    }

    @Override
    public Admin getAdminById(long adminId) {
        return super.getById(adminId);
    }

    @Override
    public Admin createAdmin(Admin admin) {
        return super.save(admin);
    }

    @Override
    public void deleteAdmin(long adminId) {
        super.delete(adminId);
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        return super.update(admin);
    }
}
