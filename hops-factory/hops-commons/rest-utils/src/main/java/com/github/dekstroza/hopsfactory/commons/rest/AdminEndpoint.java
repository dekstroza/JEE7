package com.github.dekstroza.hopsfactory.commons.rest;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Level;

import com.fasterxml.jackson.annotation.JsonProperty;

@RequestScoped
@Path("admin")
public class AdminEndpoint {

    @Inject
    @Any
    private Instance<ExposeLogControl> logLevelAdjustableEndpoints;

    private List<String> endpointClasses = new ArrayList<>();
    private final List<String> logLevels = Arrays.asList("WARN", "INFO", "DEBUG", "TRACE");

    @XmlRootElement
    class Logger implements Serializable {
        private final String clazz;
        private final String level;

        public Logger(String clazz, String level) {
            this.clazz = clazz;
            this.level = level;
        }

        @JsonProperty("class")
        public String getClazz() {
            return clazz;
        }

        public String getLevel() {
            return level;
        }
    }

    @PostConstruct
    public void findLogAdjustableEndpoints() {
        logLevelAdjustableEndpoints.forEach(logLevelAdjustableEndpoint -> {
            endpointClasses.add(logLevelAdjustableEndpoint.getClass().getSuperclass().getCanonicalName());
        });

    }

    @Produces(APPLICATION_JSON)
    @POST
    @Path("/logger/{clazz}")
    public Response setLogLevelForPackage(@PathParam("clazz") String clazz, @QueryParam("level") String level) {
        if (!endpointClasses.contains(clazz)) {
            return status(BAD_REQUEST)
                    .entity("Invalid class or package, allowed values are:\n" + endpointClasses.stream().collect(Collectors.joining("\n"))).build();
        }
        if (!logLevels.contains(level)) {
            return status(BAD_REQUEST).entity("Invalid log level, allowed values are:\n" + logLevels.stream().collect(Collectors.joining("\n")))
                    .build();
        }
        org.apache.log4j.Logger.getLogger(clazz).setLevel(Level.toLevel(level));
        return status(OK).build();
    }

    @Produces(APPLICATION_JSON)
    @GET
    @Path("logger")
    public Response getLogLevels() {
        return status(OK).entity(endpointClasses.stream().map(s -> new Logger(s, getLogLevelForClass(s))).collect(toList())).build();
    }

    private String getLogLevelForClass(String clazz) {
        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(clazz);
        if (logger != null) {
            Level level = logger.getEffectiveLevel();
            if (level != null) {
                return level.toString();
            }
        }
        return "NOT SET";
    }
}
