package main;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

import io.dekstroza.github.jee7.swarmdemo.app.ProfilingInterceptor;
import io.dekstroza.github.jee7.swarmdemo.app.RestApplication;
import io.dekstroza.github.jee7.swarmdemo.app.StatsdCommunicator;
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
        jaxrsArchive.addClass(StatsdCommunicator.class);
        jaxrsArchive.addResource(RestApplication.class);

        jaxrsArchive.addAsWebInfResource(new ClassLoaderAsset("META-INF/beans.xml", Main.class.getClassLoader()), "classes/META-INF/beans.xml");
        jaxrsArchive.addAllDependencies();

        container.start();
        container.deploy(jaxrsArchive);

    }
}
