package ru.aston.user.admin.repository;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.aston.user.admin.entity.Admin;

@Repository
public class AdminRepositoryImpl implements AdminRepository {
    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public Collection<Admin> getAllAdmin(Pageable pageable) {
        TypedQuery<Admin> query = entityManager.createQuery("SELECT a FROM Admin a", Admin.class);

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    @Override
    public Admin getAdminById(long adminId) {
        return entityManager.find(Admin.class, adminId);
    }

    @Override
    public Admin createAdmin(Admin admin) {
        entityManager.persist(admin);
        return admin;
    }

    @Override
    public void deleteAdmin(long adminId) {
        Admin admin = entityManager.find(Admin.class, adminId);
        if (admin != null) {
            entityManager.remove(admin);
        }
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        return entityManager.merge(admin);
    }
}
