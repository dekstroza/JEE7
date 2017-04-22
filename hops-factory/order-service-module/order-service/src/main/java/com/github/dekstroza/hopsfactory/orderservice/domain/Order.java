package com.github.dekstroza.hopsfactory.orderservice.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@SuppressWarnings("WeakerAccess")
@XmlRootElement
@Entity(name = "order")
@Table(name = "orders", schema = "order_service")
public class Order implements Serializable {

    @Id
    @Column
    @GeneratedValue
    private UUID id;

    @Column
    private UUID inventoryId;

    @Column
    private UUID customerId;

    @Column
    private double quantity;

    @Column
    private double price;

    @Column
    private ORDER_STATES status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date orderDate;

    public enum ORDER_STATES {
        NEW_ORDER
    }

    public Order() {

    }

    public Order(UUID inventoryId, UUID customerId, double quantity, double totalAmmount, ORDER_STATES status, Date orderDate) {
        this.inventoryId = inventoryId;
        this.customerId = customerId;
        this.quantity = quantity;
        this.price = totalAmmount;
        this.status = status;
        this.orderDate = orderDate;
    }

    public UUID getId() {
        return id;
    }

    public UUID getInventoryId() {
        return inventoryId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public ORDER_STATES getStatus() {
        return status;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setInventoryId(UUID inventoryId) {
        this.inventoryId = inventoryId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double totalAmmount) {
        this.price = totalAmmount;
    }

    public void setStatus(ORDER_STATES status) {
        this.status = status;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Order copyFrom(final Order newValues) {
        setId(newValues.getId());
        setStatus(newValues.getStatus());
        setQuantity(newValues.getQuantity());
        setOrderDate(newValues.getOrderDate());
        setPrice(newValues.getPrice());
        setInventoryId(newValues.getInventoryId());
        setCustomerId(newValues.getCustomerId());
        return this;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", inventoryId=" + inventoryId + ", customerId=" + customerId + ", quantity=" + quantity + ", totalAmmount="
                   + price + ", status='" + status + '\'' + ", orderDate=" + orderDate + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Order order = (Order) o;

        if (Double.compare(order.quantity, quantity) != 0)
            return false;
        if (Double.compare(order.price, price) != 0)
            return false;
        if (!id.equals(order.id))
            return false;
        if (!inventoryId.equals(order.inventoryId))
            return false;
        if (!customerId.equals(order.customerId))
            return false;
        if (!status.equals(order.status))
            return false;
        return orderDate.equals(order.orderDate);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id.hashCode();
        result = 31 * result + inventoryId.hashCode();
        result = 31 * result + customerId.hashCode();
        temp = Double.doubleToLongBits(quantity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + status.hashCode();
        result = 31 * result + orderDate.hashCode();
        return result;
    }
}
