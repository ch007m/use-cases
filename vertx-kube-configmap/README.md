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
Mix Json & Properties
oc create configmap game-config --from-file=src/main/resources/game.properties
oc create configmap ui-config --from-file=src/main/resources/ui.json
oc create configmap app-config --from-file=src/main/resources/app.json
```

# To consult the configMap (optional)

```
oc get configmap/game-config -o yaml
oc get configmap/ui-config -o yaml
oc get configmap/app-config -o yaml
```

* Build and deploy the project
   
```
mvn -Popenshift   
```

# Consult the log of the pod

```
oc log simple-config-map-j9syq
W0913 19:18:19.483573   52409 cmd.go:269] log is DEPRECATED and will be removed in a future version. Use logs instead.
Sep 13, 2016 5:06:42 PM io.vertx.core.impl.launcher.commands.VertxIsolatedDeployer
INFO: Succeeded in deploying verticle
{
  "\"enemies.cheat.level\"" : "\"noGoodRotten\",",
  "\"enemies.cheat\"" : "true,",
  "\"secret.code.allowed\"" : "true,",
  "\"lives\"" : "3,",
  "}" : "",
  "{" : "",
  "\"secret.code.passphrase\"" : "\"UUDDLRLRBABAS\",",
  "\"enemies\"" : "\"aliens\",",
  "\"secret.code.lives\"" : 30.0,
  "app.properties" : "{\n    \"logging\":\"debug\",\n    \"hostname\":\"127.0.0.1\"\n}",
  "color.good" : "purple",
  "color.bad" : "yellow",
  "allow.textmode" : true,
  "how.nice.to.look" : "fairlyNice"
}
```

# Delete Replication controller, service, ConfigMap

```
oc delete service simple-vertx-configmap
oc delete rc simple-config-map

oc delete configmap/game-config
oc delete configmap/ui-config
oc delete configmap/app-config
```

# Resize Pods

```
oc scale rc simple-config-map --replicas=0
```

# Not used

```
Json
oc create configmap game-config --from-file=src/main/resources/game.json
oc create configmap ui-config --from-file=src/main/resources/ui.json
oc create configmap app-config --from-file=src/main/resources/app.json

Properties
oc create configmap game-config --from-file=src/main/resources/game.properties
oc create configmap ui-config --from-file=src/main/resources/ui.properties
oc create configmap app-config --from-file=src/main/resources/app.properties
```
