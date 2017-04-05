package main;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.swagger.SwaggerArchive;

import io.dekstroza.github.jee7.swarmdemo.app.CORSFilter;
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
        // Enable the swagger bits
        SwaggerArchive swaggerArchive = ShrinkWrap.create(SwaggerArchive.class, "token-service.war");

        swaggerArchive.setResourcePackages("io.dekstroza.github.jee7.swarmdemo.app.endpoints");
        swaggerArchive.setTitle("Token Service");
        swaggerArchive.setVersion("v1.0.0");
        swaggerArchive.setContextRoot("/api");

        final JAXRSArchive jaxrsArchive = swaggerArchive.as(JAXRSArchive.class);
        jaxrsArchive.addClass(CORSFilter.class);
        jaxrsArchive.addClass(TokenGeneratorEndpoint.class);
        jaxrsArchive.addClass(TokenServiceApplication.class);
        jaxrsArchive.addClass(HealthzEndpoint.class);
        jaxrsArchive.addClass(Credentials.class);
        jaxrsArchive.addAsWebInfResource(new ClassLoaderAsset("META-INF/beans.xml", Main.class.getClassLoader()), "classes/META-INF/beans.xml");

        jaxrsArchive.addAllDependencies();
        return jaxrsArchive;
    }
}
