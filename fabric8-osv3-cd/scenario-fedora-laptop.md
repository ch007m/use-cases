# Fedora laptop with OSEv3 binary (0.5)

* prerequisite - docker 1.6 

1) Install the CE scenario using json file 

https://github.com/fabric8io/jenkins-pipeline-dsl

https://gist.github.com/jstrachan/896e00a9007db2b42728#file-gistfile1-json-L1197

https://github.com/fabric8io/fabric8/issues/3888#issuecomment-99519716

./osc process -f fabric8-ce.json | ./osc create -f -

./osc delete pod,se,rc,dc,bc,oauthclient --all
./osc delete all --all
./osc delete oauthclient --all


2) Abandon approach 1) and switch to apps available under quickstarts

* Setup of the routes from the MacBook Pro to the lenovo Fedora 21 machine running OS

sudo route -n delete 172.0.0.0/8
sudo route -n add 172.0.0.0/8 192.168.1.85

3) Start & configure OSv0.5 on the Lenovo Fedora 21 machine

* Start openshift : sudo ./openshift start
* From another terminal, log to the console

./osc login
Username: admin
Authentication required for https://localhost:8443 (openshift)
Password:
Login successful.

You don't have any projects. You can try to create a new project, by running

    $./osc login

* Create the default project    

    $./osc create new-project default
    Error from server: namespaces "default" already exists

* Add role and access rights
 
    ./osadm policy add-role-to-user admin admin -n default
    ./openshift admin policy add-role-to-user cluster-admin admin -n default
    ./openshift admin policy add-role-to-group cluster-admin system:authenticated system:unauthenticated

* Issue
sudo chcon -t svirt_sandbox_file_t /var/lib/openshift

* Install the secret keys

./add-admin-secrets.sh

````
#!/bin/bash

cat <<EOF | ./osc create -f -
---
  apiVersion: "v1beta3"
  kind: "Secret"
  metadata:
    name: "openshift-cert-secrets"

  data:
    root-cert: "$(base64 -w 0 openshift.local.config/master/ca.crt)"
    admin-cert: "$(base64 -w 0 openshift.local.config/master/admin.crt)"
    admin-key: "$(base64 -w 0 openshift.local.config/master/admin.key)"
EOF
````

Result :

````
./osc get secret
NAME                     DATA
openshift-cert-secrets   3
````

* To route external requests to a local server using HA-proxy

    ./osadm router --credentials=openshift.local.config/master/openshift-router.kubeconfig --ports='80:80,443:443,9090:9090'



* We will deploy the Fabric8 console. So, clone locally into the lenovo - fedora21 machine the following git repos
 
 
git clone git@github.com:fabric8io/fabric8.git
git clone https://github.com/fabric8io/quickstarts
 

cd quickstarts/
cd apps
mvn install

cd console 

# INFO : https://github.com/fabric8io/fabric8/tree/master/components/kubernetes-api#configuration

* Define env required to compile the project

  export OPENSHIFT_DIR=/home/dabou/Temp/openshiftv3
  export KUBERNETES_MASTER=http://localhost:8443
  export KUBERNETES_TRUST_CERT=true
  export KUBERNETES_DOMAIN=foo.acme.com
  export KUBERNETES_NAMESPACE=default
  export KUBERNETES_NAMESPACE_ALL="default" # Added to be able to run the examples of kubernetes-api
  export KUBERNETES_CLIENT_CERTIFICATE_FILE=$OPENSHIFT_DIR/openshift.local.config/master/admin.crt
  export KUBERNETES_CLIENT_KEY_FILE=$OPENSHIFT_DIR/openshift.local.config/master/admin.key

* To avoid SSL Certificate validation

export SKIP_TLS_VERIFY=true
export KUBERNETES_TRUST_CERT=true

* Compile, generate the kubernetes json file

  mvn clean fabric8:json -Dfabric8.parameter.DOMAIN.value=foo.acme.com -Dfabric8.env.OAUTH_AUTHORIZE_URI="http://192.168.1.85:8443/oauth/authorize"

  File modified to specify the domain :

  https://gist.github.com/b23a1b906b41ed411c72

REMARK : The /etc/hosts file on the macbook pro contains this entry 

192.168.1.85  fabric8.foo.acme.com  

* Install it 

sudo chmod 655 /home/dabou/Temp/openshiftv3/openshift.local.config/master/admin.key

mvn fabric8:apply -Dfabric8.apply.recreate=true -Dfabric8.env.OAUTH_AUTHORIZE_URI="http://172.28.128.4:8443/oauth/authorize"

# Test with Vagrant

bash <(curl -sSL https://bit.ly/get-fabric8) -f -m fabric8.local

Edit /etc/hosts on the mac to add this entry

172.28.128.4    fabric8.local # + DOCKER_IP ADDRESS








