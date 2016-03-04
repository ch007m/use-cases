package my.cool.demo;

import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import java.io.File;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class BasicTestCase {

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return ShrinkWrap
                .create(WebArchive.class)
                .addPackage("my.cool.demo")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new File("src/main/webapp/jetty-web.xml"))
                .addAsWebInfResource(new File("src/main/webapp/jetty-env.xml"))
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));
    }

    @Test
    public void getMessage(@ArquillianResteasyResource("rest") Message messageResource) {
        // Given
        final String name = "charles";

        // When
        final Response result = messageResource.printMessage(name);

        // Then
        assertNotNull(result);
        assertEquals(200,result.getStatus());
    }
}
