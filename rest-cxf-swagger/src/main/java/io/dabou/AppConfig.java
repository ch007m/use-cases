package io.dabou;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import io.dabou.service.JaxRsApiApplication;
import io.dabou.service.PaymentService;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

import javax.ws.rs.ext.RuntimeDelegate;
import java.util.Arrays;

@Configuration
public class AppConfig {
    public static final String SERVER_PORT = "server.port";
    public static final String SERVER_HOST = "server.host";
    public static final String CONTEXT_PATH = "context.path";

    private static Class ServiceClazz = io.dabou.service.PaymentService.class;

    @Bean(destroyMethod = "shutdown")
    public SpringBus cxf() {
        return new SpringBus();
    }

    @Bean
    @DependsOn("cxf")
    public Server jaxRsServer() {
        JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint(jaxRsApiApplication(), JAXRSServerFactoryBean.class);
        factory.setServiceBeans(Arrays.<Object>asList(apiListingResourceJson()));
        factory.setResourceProvider(ServiceClazz, new MyResourceProvider(ServiceClazz));
        factory.setAddress(factory.getAddress());
        // Add CORS support
        factory.setProvider(new CrossOriginResourceSharingFilter());
        factory.setProviders(Arrays.<Object>asList(jsonProvider(), resourceListingProvider(), apiDeclarationProvider()));
        return factory.create();
    }

    @Bean
    @Autowired
    public BeanConfig swaggerConfig(Environment environment) {
        final BeanConfig config = new BeanConfig();

        config.setVersion("1.0.0");
        config.setScan(true);
        config.setResourcePackage(PaymentService.class.getPackage().getName());
        config.setBasePath(
                String.format("http://%s:%s/%s%s",
                        environment.getProperty(SERVER_HOST),
                        environment.getProperty(SERVER_PORT),
                        environment.getProperty(CONTEXT_PATH),
                        jaxRsServer().getEndpoint().getEndpointInfo().getAddress()
                )
        );

        return config;
    }

    @Bean
    public ApiDeclarationProvider apiDeclarationProvider() {
        return new ApiDeclarationProvider();
    }

    @Bean
    public ApiListingResourceJSON apiListingResourceJson() {
        return new ApiListingResourceJSON();
    }

    @Bean
    public ResourceListingProvider resourceListingProvider() {
        return new ResourceListingProvider();
    }

    @Bean
    public JaxRsApiApplication jaxRsApiApplication() {
        return new JaxRsApiApplication();
    }

    @Bean
    public JacksonJsonProvider jsonProvider() {
        return new JacksonJsonProvider();
    }
}