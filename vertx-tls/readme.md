# Info

http://ruchirawageesha.blogspot.ch/2010/07/how-to-create-clientserver-keystores.html

# Generate Public & Private keys for client & server
keytool -genkey -alias server -keyalg RSA -keystore server.jks -storepass dabou456
keytool -genkey -alias client -keyalg RSA -keystore client.jks -storepass dabou456

# Export certificate associated to yhe keys
keytool -export -file server.cert -keystore server.jks -storepass dabou456 -alias server
keytool -export -file client.cert -keystore client.jks -storepass dabou456 -alias client

# Add server certificate to the client store
keytool -importcert -file server.cert -keystore client.jks -storepass dabou456 -alias server

# Review certificate info
keytool -list -v -keystore server.jks -storepass dabou456
keytool -list -v -keystore client.jks -storepass dabou456
keytool -printcert -v -file server.cert
keytool -printcert -v -file client.cert

# Create a self signed certificate
keytool -import -v -trustcacerts -alias server-alias -file server.cer -keystore cacerts.jks -keypass dabou456 -storepass dabou456
