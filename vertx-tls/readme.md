# Info

http://ruchirawageesha.blogspot.ch/2010/07/how-to-create-clientserver-keystores.html

keytool -genkey -alias server -keyalg RSA -keystore server.jks
keytool -genkey -alias client -keyalg RSA -keystore client.jks
keytool -list -v -keystore server.jks -storepass dabou456
keytool -list -v -keystore client.jks -storepass dabou456
keytool -export -file server.cert -keystore server.jks -storepass dabou456 -alias server
keytool -export -file client.cert -keystore client.jks -storepass dabou456 -alias client
keytool -printcert -v -file server.cert
keytool -printcert -v -file client.cert

2nd TRy

https://docs.oracle.com/cd/E19798-01/821-1841/gjrgy/index.html

Remak : username encoded = "localhost"

keytool -genkey -alias server-alias -keyalg RSA -keypass dabou456 -storepass dabou456 -keystore keystore.jks
keytool -export -alias server-alias -storepass dabou456 -file server.cer -keystore keystore.jks

keytool -printcert -v -file server.cer
Owner: CN=localhost, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown
Issuer: CN=localhost, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown
Serial number: 255d2774
Valid from: Tue Nov 10 22:13:53 CET 2015 until: Mon Feb 08 22:13:53 CET 2016
Certificate fingerprints:
         MD5:  A9:8C:54:AF:CE:E5:30:6E:81:90:FF:C2:0F:AB:04:01
         SHA1: 48:A0:4A:03:29:E5:8F:5C:75:E9:FD:1B:85:8F:D0:81:0A:21:52:C3
         SHA256: 58:0B:00:88:45:F6:91:20:94:AF:04:A5:CC:73:D7:67:F6:68:6B:0F:2B:B6:4A:30:5C:1D:97:4C:77:F3:CB:F9
         Signature algorithm name: SHA256withRSA
         Version: 3

Extensions: 

#1: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 2A 05 8F 27 C1 0B BB BC   3D 90 8C C0 86 F8 8A C2  *..'....=.......
0010: D1 11 F5 98                                        ....
]
]
keytool -import -v -trustcacerts -alias server-alias -file server.cer -keystore cacerts.jks -keypass dabou456 -storepass dabou456
