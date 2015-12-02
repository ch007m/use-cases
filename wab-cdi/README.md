# Install

features:install pax-cdi-1.1-web-weld
install -s wrap:mvn:org.scannotation/scannotation/1.0.3
install -s wrap:mvn:org.apache.httpcomponents/httpcore/4.4.1
install -s mvn:io.apiman/resteasy-cdi/2.3.10.Final_1
install -s mvn:io.apiman/resteasy-jaxrs/2.3.10.Final_1

install -s wrap:mvn:org.jboss.classfilewriter/jboss-classfilewriter/1.1.2.Final
install -s mvn:org.jboss.weld/weld-osgi-bundle/2.3.0.Final
install -s mvn:my.cool.demo/wab-cdi/1.0

install -s wrap:mvn:org.jboss.weld.servlet/weld-servlet-core/2.3.0.Final
install -s wrap:mvn:org.jboss.weld.environment/weld-environment-common/2.3.0.Final