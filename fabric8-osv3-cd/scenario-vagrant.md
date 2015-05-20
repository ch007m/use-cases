# Centos-7.1 using Vagrant VM

* Open a terminal and move to the fabric8 project
* Create the VM machine
```
    vagrant up
```
* Next ssh to the machine
```
    vagrant ssh
```
* Within the current directory, execute these commands to install the Fabric8 console as described [here](https://github.com/jstrachan/fabric8/blob/docs-2.1.x/docs/fabric8OnOpenShift.md#install-fabric8-apps)  
```
    export KUBERNETES_DOMAIN=vagrant.local
    export KUBERNETES_MASTER="https://172.28.128.4:8443"
    export KUBERNETES_TRUST_CERT="true"
    
    osc login
    
    osc process -v DOMAIN='vagrant.local' -f http://central.maven.org/maven2/io/fabric8/apps/base/2.1.1/base-2.1.1-kubernetes.json | osc create -f -
    osc process -v DOMAIN='vagrant.local' -f http://central.maven.org/maven2/io/fabric8/apps/cdelivery-core/2.1.1/cdelivery-core-2.1.1-kubernetes.json | osc create -f -
```
* Next, create the routes using this command from macosx. the routes will be created based on the [services deployed on openshift](https://github.com/jstrachan/fabric8/blob/docs-2.1.x/docs/fabric8OnOpenShift.md#creating-routes)
```
    osc login https://172.28.128.4:8443
    
    export KUBERNETES_MASTER=https://172.28.128.4:8443
    export KUBERNETES_DOMAIN=vagrant.local
    export KUBERNETES_NAMESPACE=default
    
    mvn io.fabric8:fabric8-maven-plugin:2.1.1:create-routes
```
* To delete the pods, services, routes created and redeploy the project, use these commands
```
    osc delete se,rc,dc,bc,oauthclient,pods,Route --all
    osc delete pods --all
    osc process -v DOMAIN='vagrant.local' -f http://central.maven.org/maven2/io/fabric8/apps/base/2.1.1/base-2.1.1-kubernetes.json | osc create -f -
```

* To delete a pod 
```
    osc delete pod -l component=console,provider=fabric8
```

* If the registry is required after a full delete
```
osdm registry --create --credentials=/var/lib/openshift/openshift.local.config/master/openshift-registry.kubeconfig
```

* To build & deploy a new image of a quickstart into the fabric8 namespace/project

* Build

```
osc project default
osc login -u admin -p admin https://172.28.128.4:8443
export DOCKER_REGISTRY=$(osc get -o yaml service docker-registry | grep portalIP | awk '{ print $2 }'):5000
export DOCKER_HOST=tcp://172.28.128.4:2375
mvn clean install docker:build -Ddocker.host=$DOCKER_HOST
```

* To deploy the image within the Docker registry
``` 
mvn docker:push -Ddocker.host=$DOCKER_HOST -Ddocker.username=admin -Ddocker.password=admin -Ddocker.registry=$DOCKER_REGISTRY
```

* To deploy the service, pod, replication controller

``` 
osc project fabric8
osc login -u admin -p admin https://172.28.128.4:8443
export KUBERNETES_NAMESPACE=fabric8
mvn fabric8:apply -Dfabric8.apply.recreate=true
``` 

* And create the routes
```
mvn io.fabric8:fabric8-maven-plugin:2.1.1:create-routes
```








