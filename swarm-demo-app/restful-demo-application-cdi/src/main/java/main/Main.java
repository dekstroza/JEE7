package main;

import io.dekstroza.github.jee7.swarmdemo.app.endpoints.ApplicationLoginEndpoint;
import io.dekstroza.github.jee7.swarmdemo.app.endpoints.PublicRestEndpoint;
import io.dekstroza.github.jee7.swarmdemo.app.endpoints.SecuredRestEndpoint;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

import io.dekstroza.github.jee7.swarmdemo.app.RestApplication;

public class Main {

    public static void main(String[] args) throws Exception {
        final Container container = new Container();
        container.start();
        final JAXRSArchive jaxrsArchive = ShrinkWrap.create(JAXRSArchive.class, "restful-demo-app.war");

        jaxrsArchive.addClass(ApplicationLoginEndpoint.class);
        jaxrsArchive.addClass(PublicRestEndpoint.class);
        jaxrsArchive.addClass(SecuredRestEndpoint.class);
        jaxrsArchive.addResource(RestApplication.class);

        jaxrsArchive.addAsWebInfResource(new ClassLoaderAsset("META-INF/beans.xml", Main.class.getClassLoader()), "classes/META-INF/beans.xml");
        jaxrsArchive.addAllDependencies();
        container.deploy(jaxrsArchive);

    }
}
