# All the power of Apache CXF framework in one simple project

## Introduction



## How to play with it

* Compile the project using maven tool

    mvn compile

* Launch the CXF Server exposing the WS using the Jetty Transport Connector

    mvn exec:java -Dexec.mainClass=org.dabou.StandaloneWS

* Run the client to consume the WebService HelloWorld

    mvn exec:java -Dexec.mainClass=org.dabou.ClientWS
