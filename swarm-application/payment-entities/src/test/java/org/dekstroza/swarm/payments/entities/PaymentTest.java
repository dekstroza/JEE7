package org.dekstroza.swarm.payments.entities;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

public class PaymentTest {

    @Test
    public void testEquals__WhenIdEquals() throws Exception {
        UUID uuid = UUID.randomUUID();
        Payment first = new Payment();
        first.setId(uuid.toString());

        Payment second = new Payment();
        second.setId(uuid.toString());
        Assert.assertTrue(first.equals(second));
    }

    @Test
    public void testEquals__FailsWhenIdDifferent() throws Exception {
        UUID firstUUID = UUID.randomUUID();
        UUID secondUUID = UUID.randomUUID();
        Payment first = new Payment();
        first.setId(firstUUID.toString());

        Payment second = new Payment();
        second.setId(secondUUID.toString());
        Assert.assertFalse(first.equals(second));
    }
}