# Camel REST & CRUD Quickstart

When the project is deployed into Fabric and that a Fuse container has been created with the profile `demo-camelrest`
then you can access the REST service using the following curl/http requests

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

The project can work locally using the camel:run plugin and the curl or httpie commands described hereafter will also work

## Summary of the HTTPie commands

http GET http://localhost:9090/rest/customerservice/customers/123
echo '{"Customer":{"name":"TUG"}}' | http POST http://localhost:9090/rest/customerservice/customers
echo '{"Customer":{"id":124,"name":"TUG2"}}' | http PUT http://localhost:9090/rest/customerservice/customers
http DELETE http://localhost:9090/rest/customerservice/customers/124
 
## Fabric

The project must be deployed as a Fabric profile using the following maven command to be executed using a terminal

```
vn fabric8:deploy
```

1. Installation of Apiman & Keycloak
------------------------------------

ApiMan can be installed according to the instructions defined [here](http://www.apiman.io/latest/download.html). 
Here is the list of the instructions to be executed in order to install wildfly 8.2 server and to deploy the different war (apiman, keycloak) part of the
project

```
mkdir ~/apiman-1.1.3.Final
cd ~/apiman-1.1.3.Final
curl http://downloads.jboss.org/wildfly/8.2.0.Final/wildfly-8.2.0.Final.zip -o wildfly-8.2.0.Final.zip
curl http://downloads.jboss.org/overlord/apiman/1.1.3.Final/apiman-distro-wildfly8-1.1.3.Final-overlay.zip -o apiman-distro-wildfly8-1.1.3.Final-overlay.zip

rm -rf wildfly-8.2.0.Final
unzip wildfly-8.2.0.Final.zip
unzip -o apiman-distro-wildfly8-1.1.3.Final-overlay.zip -d wildfly-8.2.0.Final
cd wildfly-8.2.0.Final
export JBOSS_HOME=~/MyApplications/apiman-1.1.3.Final/wildfly-8.2.0.Final
jvm 1.7
./bin/standalone.sh -c standalone-apiman.xml`
```

2. Install the Keycloak APiman plugin
-------------------------------------

OAuth2 policy doesn’t appear in the standard list of policies in the Api manager UI;
that’s because the OAuth2 policy is an example of an apiman plugin, all of which are shipped separately from apiman, but are trivially easy to install

When logged into the [apiman manager UI](http://localhost:8080/apimanui/) as an administrator (for the quickstart that’s u:`admin`, p:`admin123!`), navigate to the manage plugins page:

![Apiman Keycloak plugin](image/sysadmin-manage-plugins.png)

Select add plugin, fill in the details: 

```
GAV	| Value 
Group | io.apiman.plugins
Artifact | apiman-plugins-keycloak-oauth-policy
Version | 1.1.3.Final
```
and add plugin. That’s it!

3. Creation of the organization
-------------------------------

4. Creation of the service
--------------------------


Service
-------
http://localhost:9090/rest/customerservice/customers/

Oauth
-----

http://127.0.0.1:8080/auth/realms/stottie

MIICnTCCAYUCBgFNvgU/kjANBgkqhkiG9w0BAQsFADASMRAwDgYDVQQDDAdzdG90dGllMB4XDTE1MDYwNDEwMDAyNFoXDTI1MDYwNDEwMDIwNFowEjEQMA4GA1UEAwwHc3RvdHRpZTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAKwVSFacR00MzGUZuPEuqImUDxIPa/a4d8hW/TV9ERVMNOL0GLEufAd4XXE8gr3htJ2y5wB7DlyUxCJo+yl1STKgEVcNi4k92cxIOIfyl978OtHiB9zs1eI93uhH/Ru40kIVcE/WPnq8gtJSzuS58DPUFp/MQXSk3GJIEk1eF3SKJIa0X+EBrJAuR6VFdA0ks0Hr9MQqitMi2cwoyiPeNxuGk3+NaWFNdwUI/ccPLQ4mRKCgjyZ/VWv5ilOflNM8kouhvtB0Wu8awGScRpVE5c0OZ6yceG1izF1g59jLvoiIm8I1Q7S/POmCf7KaWscpIBJXvSzJaTRoy+9nCSwDYe0CAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAi5o+4zVGtzZB88iMRvu85Amu9xMcyMaABAJKA4RNXMiYiStwVXNfRaqcECajAzx5BFo8XNxOEHe02U+/aac/j0H8IJT9Y9g8CCJnq0xIWrTAUSlBGdGUOJO0SZ7afy4iim68iJpLlQO3bE8lLzBkq+jlef6KQTH7Q1Ve8l692G4+x479v3gtVhOfnHxWUi7XRNOiP/Sjem2w6DFBgU3ua0tofJhZ9spK1YvIf4IjtJ0duhdl0jyPaakZM+tUp08ne6lN/5HCYOegQOXzs/1gD0d1l1r8h8E3rhsEtKY+51Ohlr8szYzSHJPP/1LNEXDjA4Nptul20eRIKwOGfTwyVw==

Fabric requests
---------------

http GET http://localhost:9090/rest/customerservice/customers/123
http --verify=no GET https://localhost:8443/apiman-gateway/demo/CustomerService/1.0/123
http --verify=no -a demo:demo GET https://localhost:8443/apiman-gateway/demo/CustomerService/basic/123
