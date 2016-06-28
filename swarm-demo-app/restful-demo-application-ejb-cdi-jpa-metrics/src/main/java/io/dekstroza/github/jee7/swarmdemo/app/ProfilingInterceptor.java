package io.dekstroza.github.jee7.swarmdemo.app;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class ProfilingInterceptor {

    private static final StatsdCommunicator statsdCommunicator = StatsdCommunicator.getInstance();

    @AroundInvoke
    public Object measureInvocationTime(final InvocationContext ctx) throws Exception {
        final long start = System.nanoTime();
        final Object result = ctx.proceed();
        final long timeTaken = System.nanoTime() - start;
        statsdCommunicator.recordLatency(ctx.getMethod().getName(), timeTaken);
        return result;

    }
}
