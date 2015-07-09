#!/usr/bin/env bash

TOKEN=$(curl -X POST http://127.0.0.1:8080/auth/realms/stottie/protocol/openid-connect/token  -H "Content-Type: application/x-www-form-urlencoded" -d 'username=rincewind' -d 'password=apiman' -d 'grant_type=password' -d 'client_id=apiman' | jq -r '.access_token')

echo ">>> TOKEN Received"
echo ">>> We will execute a request to call now our service using the API Man"

echo ">>> GET Customer : 123"

http --verify=no https://127.0.0.1:8443/apiman-gateway/Newcastle/CustomerService/2.0/123 "Authorization: Bearer $TOKEN"

echo ">>> POST Customer : 124"

echo '{"Customer":{"name":"SMALS"}}' | http --verify=no POST https://127.0.0.1:8443/apiman-gateway/Newcastle/CustomerService/2.0 "Authorization: Bearer $TOKEN"

echo ">>> PUT Customer : 124"

echo '{"Customer":{"id":124,"name":"SMALS2"}}' | http --verify=no PUT https://127.0.0.1:8443/apiman-gateway/Newcastle/CustomerService/2.0 "Authorization: Bearer $TOKEN"

echo ">>> DEL Customer : 124"

http --verify=no DELETE https://127.0.0.1:8443/apiman-gateway/Newcastle/CustomerService/2.0/124 "Authorization: Bearer $TOKEN"

echo ">>> GET Customer : 124"

http --verify=no https://127.0.0.1:8443/apiman-gateway/Newcastle/CustomerService/2.0/124 "Authorization: Bearer $TOKEN"