package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompletableFutureTest {

    private static final Logger log = LoggerFactory.getLogger(CompletableFutureTest.class);

    public class RequestInfo {
        private int id;
        private String request;

        public RequestInfo(int id, String request) {
            id = id;
            this.request = request;
        }

        public RequestInfo(final RequestInfo source) {
            id = source.getId();
            request = source.getRequest();
        }

        public int getId() {
            return id;
        }

        public String getRequest() {
            return request;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            RequestInfo that = (RequestInfo) o;

            if (getId() != that.getId())
                return false;
            return getRequest().equals(that.getRequest());
        }

        @Override
        public int hashCode() {
            int result = getId();
            result = 31 * result + getRequest().hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RequestInfo{" + "id=" + id + ", request='" + request + '\'' + '}';
        }
    }

    @Test
    public void testComposition() throws Exception {

        final RequestInfo info = new RequestInfo(0, "request0");
        CompletableFuture<RequestInfo> start = CompletableFuture.supplyAsync(() -> {
            log.info("Supplying request->{} on Thread->{}", info, Thread.currentThread().getName());
            return info;
        });

        CompletableFuture<RequestInfo> cf1 = start.thenApplyAsync(requestInfo -> {
            RequestInfo second = new RequestInfo(requestInfo.getId(), "request2");
            requestInfo = new RequestInfo(second);
            log.info("Aplying first transformation: request->{} on Thread->{}", second, Thread.currentThread().getName());
            throw new RuntimeException("Something bad happened at cf1");
            // return requestInfo;
        });

        CompletableFuture<RequestInfo> cf2 = start.thenApplyAsync(requestInfo -> {
            RequestInfo third = new RequestInfo(3, requestInfo.getRequest());
            requestInfo = new RequestInfo(third);
            log.info("Aplying second transformation: request->{} on Thread->{}", third, Thread.currentThread().getName());
            throw new RuntimeException("Something bad happened at cf2");

        });

        CompletableFuture<RequestInfo> cf3 = cf1.thenCombine(cf2, (requestInfo, requestInfo2) -> {
            RequestInfo fourth = new RequestInfo(requestInfo.getId(), requestInfo2.getRequest());
            log.info("Combining first and second transformation: request->{} on Thread->{}", fourth, Thread.currentThread().getName());
            return fourth;
        });

        CompletableFuture<RequestInfo> cf4 = cf3.thenApply(requestInfo -> {
            RequestInfo fifth = new RequestInfo(requestInfo.getId(), "applied");
            log.info("Combining first and second transformation: request->{} on Thread->{}", fifth, Thread.currentThread().getName());
            return requestInfo;
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });

        log.info("The end->{}", cf4.get());

    }
}
