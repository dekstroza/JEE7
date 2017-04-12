package main;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jolokia.JolokiaFraction;

public class Main {

    public static void main(String[] args) throws Exception {
        final Swarm swarm = new Swarm();
        swarm.fraction(
                   new JolokiaFraction("/jmx")
        );
        swarm.start();
        swarm.deploy(createDeployment());

    }

    public static JAXRSArchive createDeployment() throws Exception {
        final JAXRSArchive jaxrsArchive = ShrinkWrap.create(JAXRSArchive.class, "token-service.war");
        jaxrsArchive.addPackages(true, "io.dekstroza.github.jee7.swarmdemo.app");
        jaxrsArchive.addAsWebInfResource(new ClassLoaderAsset("META-INF/beans.xml", Main.class.getClassLoader()), "classes/META-INF/beans.xml");
        jaxrsArchive.addAllDependencies();
        return jaxrsArchive;
    }
}
