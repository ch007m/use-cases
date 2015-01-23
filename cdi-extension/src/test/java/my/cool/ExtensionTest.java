package my.cool;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

@RunWith(Arquillian.class)
public class ExtensionTest {

    @Inject
    private Service svc;

    /**
     * Webapp with beans.xml and no classes
     */
    @Deployment
    public static WebArchive createWebArchive() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war");
        war.addAsLibrary(createJavaArchive());
        war.addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
        return war;
    }

    /**
     * Java Archive containing the extension
     **/
    public static JavaArchive createJavaArchive() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "test.jar");
        jar.addClasses(SimpleExtension.class);
        jar.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        return jar;
    }

    @Test
    public void testExtension() {
        Assert.assertEquals("foo", svc.getFoo().ping());
    }
}
