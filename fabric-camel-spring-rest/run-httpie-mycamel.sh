#!/usr/bin/env bash

TOKEN=$(curl -X POST http://127.0.0.1:8080/auth/realms/fuse/protocol/openid-connect/token -H "Content-Type: application/x-www-form-urlencoded" -d 'username=cmoulliard' -d 'password=apiman' -d 'grant_type=password' -d 'client_id=apiman' | jq -r '.access_token')
APIGATEWAY=https://127.0.0.1:8443/apiman-gateway
ORG=demo
SERVICE=CustomerService
VERSION=fuse2
URL=$APIGATEWAY/$ORG/$SERVICE/$VERSION

echo ">>> Gateway Service $URL"

echo ">>> TOKEN Received"
echo ">>> We will execute a request to call now our service using the API Man"

echo ">>> GET Customer : 123"

http --verify=no GET $URL/123 "Authorization: Bearer $TOKEN"

# echo ">>> POST Customer : 124"
#
# echo '{"Customer":{"name":"DEMO"}}' | http --verify=no POST $URL "Authorization: Bearer $TOKEN"
#
# echo ">>> PUT Customer : 124"
#
# echo '{"Customer":{"id":124,"name":"DEMO2"}}' | http --verify=no PUT $URL "Authorization: Bearer $TOKEN"
#
# echo ">>> DEL Customer : 124"
#
# http --verify=no DELETE $URL/124 "Authorization: Bearer $TOKEN"
#
# echo ">>> GET Customer : 124"
#
# http --verify=no GET $URL/124 "Authorization: Bearer $TOKEN"