#!/usr/bin/env bash

TOKEN=$(curl -X POST http://127.0.0.1:8080/auth/realms/stottie/protocol/openid-connect/token  -H "Content-Type: application/x-www-form-urlencoded" -d 'username=rincewind' -d 'password=apiman' -d 'grant_type=password' -d 'client_id=apiman' | jq -r '.access_token')

echo ">>> TOKEN Received"
echo ">>> We will execute a request to call now our service using the API Man"

# curl -v -k -H "Authorization: Bearer $TOKEN" https://127.0.0.1:8443/apiman-gateway/Newcastle/EchoService/1.0

echo ">>> GET Customer : 123"

curl -k -H "Authorization: Bearer $TOKEN" https://127.0.0.1:8443/apiman-gateway/Newcastle/CustomerService/2.0/123

echo ">>> POST Customer : 124"

curl -k -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -X POST -d '{"Customer":{"name":"Charles"}}' https://127.0.0.1:8443/apiman-gateway/Newcastle/CustomerService/2.0

echo ">>> GET Customer : 124"

curl -k -H "Authorization: Bearer $TOKEN" https://127.0.0.1:8443/apiman-gateway/Newcastle/CustomerService/2.0/124

echo ">>> PUT Customer : 124"

curl -k -H "Content-Type: application/json" -X PUT -d '{"Customer":{"id":124,"name":"SMALS"}}' -H "Authorization: Bearer $TOKEN" https://127.0.0.1:8443/apiman-gateway/Newcastle/CustomerService/2.0/

echo ">>> DEL Customer : 124"

curl -k -X DELETE -H "Authorization: Bearer $TOKEN" https://127.0.0.1:8443/apiman-gateway/Newcastle/CustomerService/2.0/124

# echo ">>> GET Customer : 124"

# curl -k -H "Authorization: Bearer $TOKEN" https://127.0.0.1:8443/apiman-gateway/Newcastle/CustomerService/2.0/124