package io.dekstroza.github.jee7.swarmdemo.app;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfilingInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ProfilingInterceptor.class);

    @AroundInvoke
    public Object measureInvocationTime(final InvocationContext ctx) throws Exception {
        final long start = System.nanoTime();
        final Object result = ctx.proceed();
        final long timeTaken = System.nanoTime() - start;
        logger.info("{}: {}ns", ctx.getMethod().getName(), timeTaken);
        return result;

    }
}
