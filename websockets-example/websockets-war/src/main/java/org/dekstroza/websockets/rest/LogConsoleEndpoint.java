package org.dekstroza.websockets.rest;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.dekstroza.websockets.session.SessionManager;
import org.slf4j.Logger;

@ServerEndpoint("/console")
public class LogConsoleEndpoint {

    @Inject
    private Logger log;

    @Inject
    private SessionManager sessionManager;

    @OnOpen
    public void onCreateSession(final Session session) {
        this.sessionManager.addSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        log.error("Error caught:", error);
    }

    @OnClose
    public void onCloseSession(final Session session) {
        sessionManager.removeSession(session);
    }

    /**
     * Send message to all sessions. Messages will be sent in asynchronous fashion.
     * 
     * @param message
     *            to be send to all connected web sockets.
     */
    public void broadcastMessage(final String message) {
        sessionManager.broadcastMessage(message);
    }
}
