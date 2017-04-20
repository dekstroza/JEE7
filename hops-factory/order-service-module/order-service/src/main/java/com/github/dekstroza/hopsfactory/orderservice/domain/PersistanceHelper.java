package com.github.dekstroza.hopsfactory.orderservice.domain;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PersistanceHelper {

    @PersistenceContext(unitName = "OrderPU")
    private EntityManager entityManager;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Order persistInventory(Order order) throws Exception {
        entityManager.persist(order);
        return order;
    }

}
