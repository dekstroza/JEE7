package org.dekstroza.websockets.session;

/**
 * Session manager mbean interface
 */
public interface SessionManagerMBean {

    /**
     * Returns total number of sessions/
     * 
     * @return Number of sessions
     */
    int getSessionCount();

    /**
     * Returns fully qualified class name for injected partition manager implementation.
     * 
     * @return Fully qualified class name of partition manager.
     */
    String getPartitionManagerImplementation();

}
