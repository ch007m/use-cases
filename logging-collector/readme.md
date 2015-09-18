# Instructions

- Edit the file etc/org.ops4j.pax.logging.cfg and change the line containing log4j:rootLogger to use our OSGI 
custom appender

```
log4j.rootLogger=INFO, out, osgi:cool
```

- Save the file
- Start Apache Karaf or JBoss Fuse

```
./bin/fuse or ./bin/karaf
```

- Deploy the bundle

```
intall -s mvn:my.cool.demo/logging-collector/1.0
```

- Verify that you receive the logs 

```
APPENDER :: INFO: 'Generating command ACL config org.apache.karaf.command.acl.ssh into service ACL configs [org.apache.karaf.service.acl.command.ssh.ssh, org.apache.karaf.service.acl.command.ssh.sshd]') - Local info : org.ops4j.pax.logging.service.internal.PaxLocationInfoImpl@e25d8ab
```
