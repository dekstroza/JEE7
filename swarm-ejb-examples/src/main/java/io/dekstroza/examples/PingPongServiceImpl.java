package io.dekstroza.examples;

import io.dekstroza.examples.io.dekstroza.examples.api.PingPongService;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.validation.constraints.NotNull;

import static java.lang.String.format;
import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

@Stateless
@TransactionAttribute(NOT_SUPPORTED)
@Remote(PingPongService.class)
public class PingPongServiceImpl implements PingPongService {

    public String pingPong(@NotNull String message) {
        return format("Pong:%s", message);
    }
}
