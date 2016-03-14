package org.dekstroza.swarm.payments.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representation of single payment entry in payments table
 */
@Entity
@Table(schema = "public", name = "payments")
@XmlRootElement
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name = "firstname", length = 50, nullable = false)
    private String firstName;

    @Column(name = "lastName", length = 50, nullable = false)
    private String lastName;

    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @Column(name = "deducted_fee_amount", nullable = false)
    private Integer feeDeductedAmount;

    @Column(name = "receiver_location_id", nullable = false)
    private Integer receiverLocationId;

    @Column(name = "sender_location_id", nullable = false)
    private Integer senderLocationId;

    @Column(name = "is_completed", insertable = false, updatable = true)
    private Boolean payed;

    @Column(name = "passport_id", length = 100, nullable = false)
    private String passportId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", insertable = false, updatable = false)
    private Date created;

    @Column(name = "completed")
    private Date completed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getFeeDeductedAmount() {
        return feeDeductedAmount;
    }

    public void setFeeDeductedAmount(final Integer feeDeductedAmount) {
        this.feeDeductedAmount = feeDeductedAmount;
    }

    public Integer getReceiverLocationId() {
        return receiverLocationId;
    }

    public void setReceiverLocationId(final Integer receiverLocationId) {
        this.receiverLocationId = receiverLocationId;
    }

    public Integer getSenderLocationId() {
        return senderLocationId;
    }

    public void setSenderLocationId(final Integer senderLocationId) {
        this.senderLocationId = senderLocationId;
    }

    public Boolean getPayed() {
        return payed;
    }

    public void setPayed(final Boolean payed) {
        this.payed = payed;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(final String passportId) {
        this.passportId = passportId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(final Date created) {
        this.created = created;
    }

    public Date getCompleted() {
        return completed;
    }

    public void setCompleted(final Date completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final Payment payment = (Payment) other;
        return id.equals(payment.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
