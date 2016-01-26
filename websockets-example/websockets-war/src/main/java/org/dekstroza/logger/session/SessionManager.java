package org.dekstroza.logger.session;

import java.lang.management.ManagementFactory;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.*;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.validation.constraints.NotNull;
import javax.websocket.Session;

import org.dekstroza.logger.configuration.api.ConfigurationEnvironment;
import org.dekstroza.logger.partitioning.api.Partition;
import org.dekstroza.logger.partitioning.api.PartitionManager;
import org.slf4j.Logger;

/**
 * Session management singleton
 */
@LocalBean
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SessionManager implements SessionManagerMBean {

    @Inject
    private Logger log;

    @Any
    @Inject
    private ConfigurationEnvironment cfgEnvironment;

    private PartitionManager partitionManager;

    private ObjectName objectName = null;

    @PostConstruct
    public void postConstruct() {
        this.partitionManager = cfgEnvironment.selectPartitionManagerImplementation();
        registerInJMX();
    }

    @PreDestroy
    public void preDestroy() {
        unregisterFromJMX();
    }

    /**
     * Add new session to the session list
     *
     * @param session
     *            Session to add. New session can not be null.
     */
    @Asynchronous
    public void addSession(@NotNull final Session session) {
        log.debug("Adding session {}", session.getId());
        partitionManager.addSession(session);
    }

    /**
     * Remove session from the session list
     *
     * @param session
     *            Session to remove, can not be null
     */
    @Asynchronous
    public void removeSession(@NotNull final Session session) {

        log.debug("Removing session:{}", session.getId());
        partitionManager.removeSession(session);
    }

    /**
     * Send message to all opened sessions
     *
     * @param message
     *            Message to send, string format
     */

    @Asynchronous
    public void broadcastMessage(final String message) {
        final Iterator<Partition> iter = this.partitionManager.getPartitions();
        while (iter.hasNext()) {
            final Partition partition = iter.next();
            if (!partition.isEmpty()) {
                log.debug("Sending message to session group {}", partition.getName());
                sendMessage(partition, message);
            }
        }
    }

    @Asynchronous
    private void sendMessage(final Partition partition, final String message) {
        partition.broadcastMessage(message);
    }

    @Override
    public int getSessionCount() {
        return this.partitionManager.getSessionCount();
    }

    @Override
    public String getPartitionManagerImplementation() {
        return this.partitionManager.getClass().getCanonicalName();
    }

    private void registerInJMX() {
        log.debug("Registering in JMX Console");
        try {
            objectName = new ObjectName("org.dekstroza:service=WebsocketConsole");
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            mBeanServer.registerMBean(this, objectName);
        } catch (Exception e) {
            throw new IllegalStateException("Problem during registration into JMX:" + e);
        }
    }

    private void unregisterFromJMX() {
        log.debug("Unregistering in JMX Console");
        try {
            ManagementFactory.getPlatformMBeanServer().unregisterMBean(this.objectName);
        } catch (Exception e) {
            throw new IllegalStateException("Problem during unregistration of JMX:" + e);
        }
    }
}
