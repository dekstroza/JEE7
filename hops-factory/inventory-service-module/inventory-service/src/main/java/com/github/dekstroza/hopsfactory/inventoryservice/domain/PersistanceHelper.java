package com.github.dekstroza.hopsfactory.inventoryservice.domain;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PersistanceHelper {

    @PersistenceContext(unitName = "InventoryPU")
    private EntityManager entityManager;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Inventory persistInventory(Inventory inventory) throws Exception {
        entityManager.persist(inventory);
        return inventory;
    }

}
