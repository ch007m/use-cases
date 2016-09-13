# Instructions

* Launch minishift

```
minishift delete
minishift start --deploy-registry=true --memory=3072 --vm-driver="xhyve"
minishit docker-env
eval $(minishift docker-env)
```

* Setup the Kubernetes ENV variable required on your machine 

```
export KUBERNETES_MASTER=https://$(minishift ip):8443
```    
* Log on to openshift
```    
oc login $(minishift ip):8443 -u admin -p admin
```    
# Create a new project

```    
oc new-project vertx-demo
oc policy add-role-to-user view openshift-dev -n vertx-demo
oc policy add-role-to-group view system:serviceaccounts -n vertx-demo
```

# Create the ConfigMap

```
oc create configmap game-config --from-file=src/main/resources/game.properties --from-file=src/main/resources/ui.properties
oc create configmap app-config --from-file=src/main/resources/app.properties
```

# To consult the configMap (optional)

```
oc get configmap game-config -o yaml
oc get configmap/app-config -o yaml
```

* Build and deploy the project
   
```
mvn -Popenshift   
```

# Delete Replication controller, service, ConfigMap

```
oc delete service simple-vertx-configmap
oc delete rc simple-config-map
oc delete configmap/game-config
oc delete configmap/app-config
```

# Resize Pods

```
oc scale rc simple-config-map --replicas=0
```
