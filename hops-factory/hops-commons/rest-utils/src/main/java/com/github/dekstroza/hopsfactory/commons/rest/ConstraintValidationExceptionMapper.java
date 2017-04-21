package com.github.dekstroza.hopsfactory.commons.rest;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getMessage()).collect(Collectors.joining("\n"))).build();
    }
}
