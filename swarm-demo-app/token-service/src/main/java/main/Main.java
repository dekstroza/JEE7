package main;

import io.dekstroza.github.jee7.swarmdemo.app.endpoints.HealthzEndpoint;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

import app.RestApplication;
import io.dekstroza.github.jee7.swarmdemo.app.endpoints.TokenGeneratorEndpoint;

public class Main {

    public static void main(String[] args) throws Exception {
        final Swarm swarm = new Swarm();
        swarm.start();
        final JAXRSArchive jaxrsArchive = ShrinkWrap.create(JAXRSArchive.class, "restful-demo-app.war");

        jaxrsArchive.addClass(TokenGeneratorEndpoint.class);
        jaxrsArchive.addResource(RestApplication.class);
        jaxrsArchive.addClass(HealthzEndpoint.class);

        jaxrsArchive.addAsWebInfResource(new ClassLoaderAsset("META-INF/beans.xml", Main.class.getClassLoader()), "classes/META-INF/beans.xml");
        jaxrsArchive.addAllDependencies();
        swarm.deploy(jaxrsArchive);

    }
}
