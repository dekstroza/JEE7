package com.github.dekstroza.hopsfactory.supplierservice.domain;

import static javax.ejb.TransactionAttributeType.REQUIRED;

import java.util.Collection;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class PersistanceHelper {

    @PersistenceContext(unitName = "SupplierPU")
    private EntityManager entityManager;

    @TransactionAttribute(REQUIRED)
    public Supplier persistSupplier(Supplier supplier) throws Exception {
        entityManager.persist(supplier);
        return supplier;
    }

    public Collection<Supplier> getAllSuppliers() {
        TypedQuery<Supplier> query = entityManager.createQuery("SELECT s FROM supplier s", Supplier.class);
        return query.getResultList();
    }

    @TransactionAttribute(REQUIRED)
    public Supplier findById(UUID id) {
        return entityManager.find(Supplier.class, id);
    }

}
