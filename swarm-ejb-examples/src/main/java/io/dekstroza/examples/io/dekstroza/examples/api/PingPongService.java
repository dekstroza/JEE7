package io.dekstroza.examples.io.dekstroza.examples.api;

import javax.ejb.Remote;
import javax.validation.constraints.NotNull;

public interface PingPongService {

    String pingPong(@NotNull final String message);
}
