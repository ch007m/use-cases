package io.dabou.util;

import io.dabou.MyResourceProvider;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.helpers.FileUtils;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.swagger.SwaggerFeature;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Java2Swagger {

    private String outputFile;
    private Boolean attachSwagger;
    private String classifier;
    private List<String> classResourceNames;
    private String outputFileName;
    private String outputFileExtension = "json";
    private String address;
    private ClassLoader resourceClassLoader;
    private List<Class<?>> res = new ArrayList<Class<?>>();

    private static String ServiceClazzString = "io.dabou.service.PaymentService";
    private static Class ServiceClazz = io.dabou.service.PaymentService.class;

    private final static Logger LOG = LoggerFactory.getLogger(Java2Swagger.class);

    public static void main(String[] args) throws Exception {
        Java2Swagger j2s = new Java2Swagger();
        j2s.classResourceNames = new ArrayList<>();
        j2s.classResourceNames.add(ServiceClazzString);
        j2s.res.add(ServiceClazz);
        j2s.execute();
    }

    public void execute() throws Exception {
        List<Class<?>> resourceClasses = loadResourceClasses();
/*        List <Object> resourceObjects = new ArrayList<Object>();
        for (Class<?> resourceClass : resourceClasses) {
            try {
                resourceObjects.add(resourceClass.newInstance());
            } catch (InstantiationException e) {
                throw new Exception(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                throw new Exception(e.getMessage(), e);
            }
        }*/

        Thread.currentThread().setContextClassLoader(getClassLoader(ServiceClazzString));

        List<Feature> features = new ArrayList<Feature>();
        features.add(new SwaggerFeature());

        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setAddress(address);
        // setup the resource providers
        for (Class<?> clazz : res) {
            sf.setResourceProvider(clazz, new MyResourceProvider(clazz));
        }
        // ADD CORS SUPPORT
        sf.setProvider(new CrossOriginResourceSharingFilter());

        sf.setResourceClasses(ServiceClazz);
        sf.setFeatures(features);

        //
        Server server = sf.create();

        InputStream in = null;
        try {
            String res = "";
            for (Class<?> resourceClass : resourceClasses) {
                com.wordnik.swagger.annotations.Api api = resourceClass.getAnnotation(com.wordnik.swagger.annotations.Api.class);
                if (api != null) {
                    String apiPath = api.value();
                    String serverAddress = server.getEndpoint().getEndpointInfo().getAddress();
                    String apiDocs = serverAddress + "/api-docs";
                    URL url = new URL(apiDocs + apiPath);
                    in = url.openStream();
                    LOG.info("Server path : " + url.toString());
                    res = res + getStringFromInputStream(in);
                }
            }
            generateJson(resourceClasses, res);
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        } finally {
            //server.stop();
        }
    }


    private void generateJson(List<Class<?>> resourceClasses, String swagger) throws Exception {

        if (outputFile == null) {
            // Put the json in target/generated/json

            String name = null;
            if (outputFileName != null) {
                name = outputFileName;
            } else if (resourceClasses.size() == 1) {
                name = resourceClasses.get(0).getSimpleName();
            } else {
                name = "application";
            }
            outputFile = ("target/generated/json/" + name + "."
                    + outputFileExtension).replace("/", File.separator);
        }

        BufferedWriter writer = null;
        try {
            FileUtils.mkDir(new File(outputFile).getParentFile());
            writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(swagger);

        } catch (IOException e) {
            throw new Exception(e.getMessage(), e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                throw new Exception(e.getMessage(), e);
            }
        }
    }


    private ClassLoader getClassLoader(String clazz) throws Exception {
        if (resourceClassLoader == null) {
            try {
                Class cl = Class.forName(clazz);
                resourceClassLoader = cl.getClassLoader();
            } catch (Exception e) {
                throw new Exception(e.getMessage(), e);
            }
        }
        return resourceClassLoader;
    }

    private List<Class<?>> loadResourceClasses() throws Exception {
        List<Class<?>> resourceClasses = new ArrayList<Class<?>>(classResourceNames.size());
        for (String className : classResourceNames) {
            try {
                resourceClasses.add(getClassLoader(className).loadClass(className));
            } catch (Exception e) {
                throw new Exception(e.getMessage(), e);
            }
        }

        return resourceClasses;
    }

    private static String getStringFromInputStream(InputStream in) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int c = 0;
        while ((c = in.read()) != -1) {
            bos.write(c);
        }
        in.close();
        bos.close();
        return bos.toString();
    }

}
