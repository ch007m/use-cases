# Camel REST & CRUD Quickstart

When the project is deployed into Fabric and that a container has been created with the profile `demo-camelrest` then you can access the REST service using the following curl/http requests

## Curl

  * Get

 ```
 curl http://localhost:9090/rest/customerservice/customers/123
 ```

  * Post

 ```
 curl -i -H "Content-Type: application/json" -X POST -d '{"Customer":{"name":"Charles"}}' http://localhost:8080/rest/customerservice/customers
 ```

  * Put

 ```
 curl -i -H "Content-Type: application/json" -X PUT -d '{"Customer":{"id":124,"name":"TUG"}}' http://localhost:8080/rest/customerservice/customers
 ```

  * Delete

 ```
 curl -X DEL http://localhost:8080/rest/customerservice/customers/124
 ```

## HTTPie

  HTTPie is a command line HTTP client, a user-friendly cURL replacement.
  http://httpie.org

  *  Get

```
http GET http://localhost:9090/rest/customerservice/customers/123
http GET http://localhost:9090/rest/customerservice/customers/124
```

 *  Post

```
echo '{"Customer":{"name":"TUG"}}' | http POST http://localhost:9090/rest/customerservice/customers
```

 *  Put

```
echo '{"Customer":{"id":124,"name":"TUG2"}}' | http PUT http://localhost:9090/rest/customerservice/customers
```

 *  Delete

```
http DELETE http://localhost:9090/rest/customerservice/customers/124
```

## Summary of the commands

http GET http://localhost:9090/rest/customerservice/customers/123
echo '{"Customer":{"name":"TUG"}}' | http POST http://localhost:9090/rest/customerservice/customers
echo '{"Customer":{"id":124,"name":"TUG2"}}' | http PUT http://localhost:9090/rest/customerservice/customers
http DELETE http://localhost:9090/rest/customerservice/customers/124