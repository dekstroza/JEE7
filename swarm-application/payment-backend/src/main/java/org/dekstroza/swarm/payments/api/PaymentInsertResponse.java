package org.dekstroza.swarm.payments.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Payment response
 */
public class PaymentInsertResponse {

    /**
     * Full arg constructor
     * 
     * @param message
     *            Message, if status if FAILURE, or UUID if status is SUCCESS
     * @param status
     *            can be SUCCESS or FAILURE
     */
    public PaymentInsertResponse(final String message, final String status) {
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
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
