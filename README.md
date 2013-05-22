Collage creator
===============

Homework for learning RMI as described in `exercise.pdf`.

![Screenshot of a created collage](/screenshot.png)

Build
-----
```
export JAVA_HOME=/usr/lib/jvm/java-6-openjdk-amd64/ # or similar, if needed
ant
```

Running
-------
In the `build/` directory:

  1. Start the RMI registry (`./run-rmiregistry.sh` or `run-rmiregistry.bat`)
  2. Start server.jar (`java -jar server.jar` or `run-server.bat`)
  3. Start client.jar (`java -jar client.jar` or `run-client.bat`)
