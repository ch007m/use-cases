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
    osc delete pod,se,rc,dc,bc,oauthclient,Route --all
    osc process -v DOMAIN='vagrant.local' -f http://central.maven.org/maven2/io/fabric8/apps/base/2.1.1/base-2.1.1-kubernetes.json | osc create -f -
```





