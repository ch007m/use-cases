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
    feature:install camel-groovy
    feature:install camel-script
    feature:install camel-script-groovy
```    
* Install the demo example
```    
    install -s mvn:my.cool.demo/camel-osgi-groovy/1.0
```    
