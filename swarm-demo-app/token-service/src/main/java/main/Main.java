package main;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

import io.dekstroza.github.jee7.swarmdemo.app.TokenServiceApplication;
import io.dekstroza.github.jee7.swarmdemo.app.domain.Credentials;
import io.dekstroza.github.jee7.swarmdemo.app.endpoints.HealthzEndpoint;
import io.dekstroza.github.jee7.swarmdemo.app.endpoints.TokenGeneratorEndpoint;

public class Main {

    public static void main(String[] args) throws Exception {
        final Swarm swarm = new Swarm();
        swarm.start();
        swarm.deploy(createDeployment());

    }

    public static JAXRSArchive createDeployment() throws Exception {
        final JAXRSArchive jaxrsArchive = ShrinkWrap.create(JAXRSArchive.class, "token-service.war");
        jaxrsArchive.addClass(TokenGeneratorEndpoint.class);
        jaxrsArchive.addResource(TokenServiceApplication.class);
        jaxrsArchive.addClass(HealthzEndpoint.class);
        jaxrsArchive.addClass(Credentials.class);

        jaxrsArchive.addAsWebInfResource(new ClassLoaderAsset("META-INF/beans.xml", Main.class.getClassLoader()), "classes/META-INF/beans.xml");
        jaxrsArchive.addAllDependencies();
        return jaxrsArchive;
    }
}
