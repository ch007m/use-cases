# Instructions

* Launch minishift

```
  minishift start
  minishit docker-env
```

* Setup the Kubernetes ENV variable required on your machine 

```
    export KUBERNETES_MASTER=https://192.168.64.11:8443
```    
* Log to openshift
```    
    oc login 192.168.64.11:8443 -u admin -p admin
```    
# Create a new project
    
    oc new-project vertx-demo
    oc policy add-role-to-user view openshift-dev -n vertx-demo
    oc policy add-role-to-group view system:serviceaccounts -n vertx-demo
    
# Create the ConfigMap

    oc create configmap game-config --from-literal=key=some.properties
    oc create configmap from-json --from-literal=key=file-json.json
    oc create configmap special-config    

* To compile
```
    mvn clean install
```    
* To build the docker image, generate the Kubernetes config file and deploy on OpenShift 
```
    mvn -Popenshift   
```

# Troubleshoot

    oc delete service simple-vertx-configmap
    oc delete rc simple-config-map
    
    mkdir -p target/temp
    tar -vxf target/simple-config-map-1.0.0-SNAPSHOT-fat.jar -C target/temp

