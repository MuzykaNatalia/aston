package ru.aston.common;

import java.util.Collection;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.hibernate.Transaction;
import ru.aston.utils.HibernateUtil;

public class AbstractRepository<T, ID> {
    private final Class<T> entityClass;

    public AbstractRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T getById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            T existing = session.get(entityClass, id);
            if (existing == null) {
                throw new IllegalArgumentException(entityClass.getSimpleName() + " not found with ID: " + id);
            }
            return existing;
        }
    }

    public Collection<T> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM " + entityClass.getSimpleName();
            Query<T> query = session.createQuery(hql, entityClass);
            return query.getResultList();
        }
    }

    public T save(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            T savedEntity = session.merge(entity);
            transaction.commit();
            return savedEntity;
        } catch (ConstraintViolationException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Constraint violation: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error saving " + entityClass.getSimpleName(), e);
        }
    }

    public T update(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            T updatedEntity = session.merge(entity);
            transaction.commit();
            return updatedEntity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating " + entityClass.getSimpleName(), e);
        }
    }

    public void delete(ID id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.remove(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting " + entityClass.getSimpleName(), e);
        }
    }
}
