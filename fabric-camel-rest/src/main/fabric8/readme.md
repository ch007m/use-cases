# Camel REST & CRUD Quickstart

## Local deployment 

When the project is started locally using the Apache Camel Maven goal `mvn camel:run`, you can access the REST services using the following curl/http requests

* Curl

  * Get

 ```
 curl http://localhost:8080/cxf/rest/customerservice/customers/123
 ```

  * Post

 ```
 curl -i -H "Content-Type: application/json" -X POST -d '{"Customer":{"name":"Charles"}}'    http://localhost:8080/cxf/rest/customerservice/customers
 ```

  * Put

 ```
 curl -i -H "Content-Type: application/json" -X PUT -d '{"Customer":{"id":124,"name":"TUG"}}' http://localhost:8080/cxf/rest/customerservice/customers
 ```

  * Delete

 ```
 curl -X DEL http://localhost:8080/cxf/rest/customerservice/customers/124
 ```

* HTTPie

  HTTPie is a command line HTTP client, a user-friendly cURL replacement.
  http://httpie.org

  *  Get

```
http GET http://localhost:8080/cxf/rest/customerservice/customers/123
http GET http://localhost:8080/cxf/rest/customerservice/customers/124
```

 *  Post

 ```
echo '{"Customer":{"name":"TUG"}}' | http POST http://localhost:8080/cxf/rest/customerservice/customers
 ```

 *  Put

 ```
 echo '{"Customer":{"id":124,"name":"TUG2"}}' | http PUT http://localhost:8080/cxf/rest/customerservice/customers
 ```

 *  Delete

 ```
http DELETE http://localhost:8080/cxf/rest/customerservice/customers/124
 ```
 
 REMARK : The port number will be different when deployed on Fabric -> 8182, 8182
 
```
http GET http://localhost:8182/cxf/rest/customerservice/customers/123
http GET http://localhost:8182/cxf/rest/customerservice/customers/124
echo '{"Customer":{"name":"TUG"}}' | http POST http://localhost:8182/cxf/rest/customerservice/customers
echo '{"Customer":{"id":124,"name":"TUG2"}}' | http PUT http://localhost:8182/cxf/rest/customerservice/customers
http DELETE http://localhost:8182/cxf/rest/customerservice/customers/124
```
 





