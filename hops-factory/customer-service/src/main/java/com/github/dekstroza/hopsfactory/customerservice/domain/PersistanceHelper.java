package com.github.dekstroza.hopsfactory.customerservice.domain;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PersistanceHelper {

    @PersistenceContext(unitName = "CustomerPU")
    private EntityManager entityManager;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Customer persistCustomer(Customer customer) throws Exception {
        entityManager.persist(customer);
        return customer;
    }

}
