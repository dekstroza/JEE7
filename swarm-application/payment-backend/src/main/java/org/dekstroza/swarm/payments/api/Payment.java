package org.dekstroza.swarm.payments.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Representation of single payment record
 */
public class Payment {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private int totalAmmount;
    private int feeDeductedAmmount;
    private int senderLocationId;
    private int receiverLocationId;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getTotalAmmount() {
        return totalAmmount;
    }

    public void setTotalAmmount(final int totalAmmount) {
        this.totalAmmount = totalAmmount;
    }

    public int getFeeDeductedAmmount() {
        return feeDeductedAmmount;
    }

    public void setFeeDeductedAmmount(final int feeDeductedAmmount) {
        this.feeDeductedAmmount = feeDeductedAmmount;
    }

    public int getSenderLocationId() {
        return senderLocationId;
    }

    public void setSenderLocationId(final int senderLocationId) {
        this.senderLocationId = senderLocationId;
    }

    public int getReceiverLocationId() {
        return receiverLocationId;
    }

    public void setReceiverLocationId(final int receiverLocationId) {
        this.receiverLocationId = receiverLocationId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
