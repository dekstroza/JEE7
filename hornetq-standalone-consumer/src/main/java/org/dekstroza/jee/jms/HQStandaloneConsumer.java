package org.dekstroza.jee.jms;

import java.util.Properties;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HQStandaloneConsumer {

    private static final Logger log = LoggerFactory.getLogger(HQStandaloneConsumer.class);

    // Set up all the default values
    private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION = "jms/queue/CmDataChangeQueue";
    private static final String DEFAULT_USERNAME = "guest";
    private static final String DEFAULT_PASSWORD = "";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "remote://localhost:4447";

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        MessageConsumer consumer;
        Destination destination;
        Context context = null;
        CommandLine cmd = null;
        Options options = new Options();
        options.addOption("host", false, "jms server ip address");
        options.addOption("port", false, "jms server port");
        options.addOption("queue", false, "jms queue name, default: CmDataChangeQueue");
        options.addOption("cf", false, "connection factory to use, default: jms/RemoteConnectionFactory");
        options.addOption("usr", false, "username for jms connection, default is guest");
        options.addOption("pwd", false, "password for the user, default is empty.");
        CommandLineParser commandLineParser = new DefaultParser();
        try {
            cmd = commandLineParser.parse(options, args);
        } catch (Exception ignored) {
            if (log.isDebugEnabled()) {
                log.error("Error caught parsing cmd line:", ignored);
            }
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("standalone-hq-client", options);
            System.exit(-1);
        }

        final String host = cmd.hasOption("host") ? cmd.getOptionValue("host") : "localhost";
        final String port = cmd.hasOption("port") ? cmd.getOptionValue("port") : "4447";
        final String username = cmd.hasOption("usr") ? cmd.getOptionValue("usr") : DEFAULT_USERNAME;
        final String password = cmd.hasOption("pwd") ? cmd.getOptionValue("pwd") : DEFAULT_PASSWORD;
        final String destinationString = cmd.hasOption("queue") ? cmd.getOptionValue("queue") : DEFAULT_DESTINATION;
        final String connectionFactoryString = cmd.hasOption("cf") ? cmd.getOptionValue("cf") : DEFAULT_CONNECTION_FACTORY;

        final String providerURL = new StringBuilder("remote://").append(host).append(":").append(port).toString();

        try {
            // Set up the context for the JNDI lookup
            final Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
            env.put(Context.PROVIDER_URL, providerURL);
            env.put(Context.SECURITY_PRINCIPAL, username);
            env.put(Context.SECURITY_CREDENTIALS, password);
            context = new InitialContext(env);

            // Perform the JNDI lookups
            log.info("Attempting to acquire connection factory [{}]", connectionFactoryString);
            connectionFactory = (ConnectionFactory) context.lookup(connectionFactoryString);
            log.info("Found connection factory [{}] in JNDI.", connectionFactoryString);
            log.info("Attempting to acquire destination [{}]", destinationString);
            destination = (Destination) context.lookup(destinationString);
            log.info("Found destination [{}] in JNDI.", destinationString);

            // Create connection
            connection = connectionFactory.createConnection(username, password);

            //Create session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //Create consumer
            consumer = session.createConsumer(destination);
            //Start consumer
            connection.start();
            //Receive messages
            while (true) {
                final Message msg = consumer.receive();
                ObjectMessage objMsg = (ObjectMessage) msg;
                log.info("Got message:[{}]", objMsg.getObject().toString());
            }

        } catch (Exception e) {
            log.error("Error caught:", e);
        } finally {
            if (context != null) {
                context.close();
            }

            if (connection != null) {
                connection.close();
            }
        }
    }
}
