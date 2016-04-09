# directory-observer
Directory monitor and notifier using RabbitMQ. Whenever the application detects a change in the *specified* directory, it will produce a JSON message on RabbitMQ that can be consumed by any application, written in any language, that can handle JSON files.
You can run the application (or wrap it in a service) on a server and monitor a folder.

[![Build Status](https://travis-ci.org/iulianbalan/directory-observer.svg?branch=master)](https://travis-ci.org/iulianbalan/directory-observer)

# Dependencies

The application depends on the RabbitMQ server.
You can run RabbitMQ locally or on a server.

# Install
```
mvn clean install
java -jar <target\jar-file-with-dependencies> --path=C:\\logs\\error
```

