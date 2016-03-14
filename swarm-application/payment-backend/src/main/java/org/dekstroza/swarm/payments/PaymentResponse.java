package org.dekstroza.swarm.payments;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Representation of operation response on payments table
 */
public class PaymentResponse {

    public enum ResponseStatus {
        SUCCESS, ERROR
    }

    /**
     * Full arg constructor
     * 
     * @param message
     *            Message, if status if FAILURE, or UUID if status is SUCCESS
     * @param status
     *            can be SUCCESS or FAILURE
     */
    public PaymentResponse(final String message, final ResponseStatus status) {
        this.message = message;
        this.status = status;
    }

    /**
     * Error message or uuid in case of success
     */
    private String message;
    /**
     * Status indication, can be success or failure
     */
    private ResponseStatus status;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(final ResponseStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
