# Deploy on Karaf

* Download Apache Karaf 3.0.2, decompress the zip/tar.gz
* Move to the bin directory
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
```    
* Install the demo example
```    
    install -s mvn:my.cool.demo/camel-osgi-groovy/1.0
```    
