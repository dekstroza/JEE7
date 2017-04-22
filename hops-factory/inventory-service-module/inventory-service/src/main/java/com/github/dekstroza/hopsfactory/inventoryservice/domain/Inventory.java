package com.github.dekstroza.hopsfactory.inventoryservice.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity(name = "inventory")
@Table(name = "inventory", schema = "inventory_service")
public class Inventory implements Serializable {

    @Id
    @Column
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID supplierId;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private double availableQuantity;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    public Inventory() {
    }

    /**
     * Constructor with required persistent attributes
     * 
     * @param supplierId
     * @param price
     * @param availableQuantity
     * @param name
     * @param description
     */
    public Inventory(UUID supplierId, double price, double availableQuantity, String name, String description) {
        this.supplierId = supplierId;
        this.price = price;
        this.availableQuantity = availableQuantity;
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public UUID getSupplierId() {
        return supplierId;
    }

    public double getPrice() {
        return price;
    }

    public double getAvailableQuantity() {
        return availableQuantity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Inventory inventory = (Inventory) o;

        if (Double.compare(inventory.getPrice(), getPrice()) != 0)
            return false;
        if (Double.compare(inventory.getAvailableQuantity(), getAvailableQuantity()) != 0)
            return false;
        if (!getId().equals(inventory.getId()))
            return false;
        if (!getSupplierId().equals(inventory.getSupplierId()))
            return false;
        if (!getName().equals(inventory.getName()))
            return false;
        return getDescription().equals(inventory.getDescription());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getId().hashCode();
        result = 31 * result + getSupplierId().hashCode();
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getAvailableQuantity());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getName().hashCode();
        result = 31 * result + getDescription().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Inventory{" + "id=" + id + ", supplierId=" + supplierId + ", price=" + price + ", availableQuantity=" + availableQuantity + ", name='"
                + name + '\'' + ", description='" + description + '\'' + '}';
    }
}
