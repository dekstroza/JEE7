package main;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.config.logging.Level;
import org.wildfly.swarm.config.logging.SyslogHandler;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.logging.LoggingFraction;

import io.dekstroza.github.jee7.swarmdemo.app.ProfilingInterceptor;
import io.dekstroza.github.jee7.swarmdemo.app.RestApplication;
import io.dekstroza.github.jee7.swarmdemo.app.endpoints.ApplicationLoginEndpoint;
import io.dekstroza.github.jee7.swarmdemo.app.endpoints.ApplicationUserRestEndpoint;
import io.dekstroza.github.jee7.swarmdemo.app.endpoints.PublicRestEndpoint;
import io.dekstroza.github.jee7.swarmdemo.app.endpoints.SecuredRestEndpoint;
import io.dekstroza.github.jee7.swarmdemo.app.util.ContainerFactory;

public class Main {

    public static void main(String[] args) throws Exception {

        final Container container = ContainerFactory.create();

        final JAXRSArchive jaxrsArchive = ShrinkWrap.create(JAXRSArchive.class, "restful-demo-app-ejb-cdi-jpa.war");
        jaxrsArchive.addClass(ApplicationLoginEndpoint.class);
        jaxrsArchive.addClass(ApplicationUserRestEndpoint.class);
        jaxrsArchive.addClass(PublicRestEndpoint.class);
        jaxrsArchive.addClass(SecuredRestEndpoint.class);
        jaxrsArchive.addClass(ProfilingInterceptor.class);

        jaxrsArchive.addResource(RestApplication.class);

        jaxrsArchive.addAsWebInfResource(new ClassLoaderAsset("META-INF/beans.xml", Main.class.getClassLoader()), "classes/META-INF/beans.xml");
        jaxrsArchive.addAllDependencies();

        SyslogHandler handler = new SyslogHandler("io.dekstroza.github.jee7.swarmdemo");
        handler.serverAddress("localhost");
        handler.port(514);

        SyslogHandler syslogHandler = new SyslogHandler("syslog");
        syslogHandler.serverAddress("localhost");
        syslogHandler.port(514);
        syslogHandler.appName("swarm-demo-app");
        syslogHandler.level(Level.INFO);
        syslogHandler.enabled(true);

        container.fraction(new LoggingFraction().syslogHandler(syslogHandler).rootLogger(Level.ALL, syslogHandler.getKey()));
        container.start();
        container.deploy(jaxrsArchive);

    }
}
