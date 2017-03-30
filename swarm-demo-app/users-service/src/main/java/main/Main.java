package main;

import io.dekstroza.github.jee7.swarmdemo.app.endpoints.HealthzEndpoint;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

import io.dekstroza.github.jee7.swarmdemo.app.RestApplication;
import io.dekstroza.github.jee7.swarmdemo.app.TokenServiceEndpoint;
import io.dekstroza.github.jee7.swarmdemo.app.endpoints.SecuredRestEndpoint;
import io.dekstroza.github.jee7.swarmdemo.app.endpoints.UserRegistrationEndpoint;

public class Main {

    public static void main(String[] args) throws Exception {
        final Swarm swarm = new Swarm();
        swarm.start();
        final JAXRSArchive jaxrsArchive = ShrinkWrap.create(JAXRSArchive.class, "restful-demo-app.war");

        jaxrsArchive.addClass(UserRegistrationEndpoint.class);
        jaxrsArchive.addClass(SecuredRestEndpoint.class);
        jaxrsArchive.addClass(TokenServiceEndpoint.class);
        jaxrsArchive.addClass(HealthzEndpoint.class);
        jaxrsArchive.addResource(RestApplication.class);

        jaxrsArchive.addAsWebInfResource(new ClassLoaderAsset("META-INF/beans.xml", Main.class.getClassLoader()), "classes/META-INF/beans.xml");
        jaxrsArchive.addAllDependencies();
        swarm.deploy(jaxrsArchive);

    }
}
