package org.dekstroza.websockets.rest;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Path("insert")
public class LogInsertHandler {

    @Inject
    private LogConsoleEndpoint webSocketManager;

    @Inject
    private Logger logger;

    @Produces(MediaType.TEXT_HTML)
    @GET
    public Response insertLog(@NotNull @QueryParam("log") final String logMessage) {
        try {
            logger.debug("insertLog called with log message=[{}]", logMessage);
            webSocketManager.broadcastMessage(logMessage);
            return Response.ok().build();
        } catch (Exception e) {
            logger.error("Error calling insertLog:", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
