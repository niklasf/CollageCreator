Collage creator
===============
Homework. See task.pdf.

Build
-----

  export JAVA_HOME=/usr/lib/jvm/java-6-openjdk-amd64/ # or similar, if needed
  ant build

Run
---
  cd build/
  rmiregistry 1234 &
  java -Djava.security.policy=security.policy -jar server.jar &
  java -jar client.jar
