#!/usr/bin/env bash

TOKEN=$(curl -X POST http://127.0.0.1:8080/auth/realms/myadmin/protocol/openid-connect/token -H "Content-Type: application/x-www-form-urlencoded" -d 'username=admin' -d 'password=admin123!' -d 'grant_type=password' -d 'client_id=rest-manage' | jq -r '.access_token')
APIGATEWAY=http://localhost:8080/auth/admin
URL=$APIGATEWAY

echo ">>> Gateway Service $URL"

echo ">>> TOKEN Received"
echo ">>> We will execute a request to call now our service using the API Man"

echo ">>> GET Realms"

http --verify=no GET $URL/realms "Authorization: Bearer $TOKEN"

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