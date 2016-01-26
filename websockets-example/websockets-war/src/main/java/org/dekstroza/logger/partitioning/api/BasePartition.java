
package org.dekstroza.logger.partitioning.api;

import java.io.IOException;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base Partition class
 */
public abstract class BasePartition implements Partition {

    private Logger log = LoggerFactory.getLogger(BasePartition.class);

    protected void sendMessage(final Session session, final String message) {
        if (session != null && session.isOpen()) {
            try {
                log.debug("Sending message {} to session {}", message, session.getId());
                session.getBasicRemote().sendText(message);
                log.trace("Message {} sent to session {}", message, session.getId());
            } catch (IOException ioe) {
                log.error("Error sending message {} to session {}", message, session.getId());
                ioe.printStackTrace();
            }
        }
    }
}
