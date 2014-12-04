# Deploy on Karaf

* Download [Apache Karaf 3.0.2](http://www.apache.org/dyn/closer.cgi/karaf/3.0.2/apache-karaf-3.0.2.tar.gz), decompress the zip/tar.gz
* Open a terminal and move to the bin directory
* Start Karaf
```
    ./karaf
```    
* Install the Camel 2.14 Repository
```    
    feature:repo-add camel 2.14.0
```    
* Deploy camel & camel-groovy features
```    
    feature:install camel
    feature:install camel-script
    feature:install camel-script-groovy
```    
* Install the demo example
```    
    install -s mvn:my.cool.demo/camel-osgi-groovy/1.0
``` 
   
* Validate the result
```
    2014-12-04 11:55:31,998 | INFO  | l for user karaf | BlueprintCamelContext            | 70 - org.apache.camel.camel-core - 2.14.0 | Route: groovy-token started and consuming from: Endpoint[timer://groovy?period=3000]
    2014-12-04 11:55:31,998 | INFO  | l for user karaf | BlueprintCamelContext            | 70 - org.apache.camel.camel-core - 2.14.0 | Total 1 routes, of which 1 is started.
    2014-12-04 11:55:32,000 | INFO  | l for user karaf | BlueprintCamelContext            | 70 - org.apache.camel.camel-core - 2.14.0 | Apache Camel 2.14.0 (CamelContext: camel-1) started in 0.330 seconds
    2014-12-04 11:55:33,012 | INFO  | - timer://groovy | groovy-token                     | 70 - org.apache.camel.camel-core - 2.14.0 | >> Before to execute this groovy script - body.replaceAll('"','')
    2014-12-04 11:55:33,012 | INFO  | - timer://groovy | groovy-token                     | 70 - org.apache.camel.camel-core - 2.14.0 | This is a message containing double quotes. Example --> "Hello".
    2014-12-04 11:55:33,253 | INFO  | - timer://groovy | groovy-token                     | 70 - org.apache.camel.camel-core - 2.14.0 | >> After Groovy script executed - Double quotes have been removed
    2014-12-04 11:55:33,253 | INFO  | - timer://groovy | groovy-token                     | 70 - org.apache.camel.camel-core - 2.14.0 | This is a message containing double quotes. Example --> Hello.
```    
    
