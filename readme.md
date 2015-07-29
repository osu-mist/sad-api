# Student Application Decision Web API

Web API for creating, reading, updating, and deleting student application decision codes.


## Tasks

List all tasks runnable from root project:

    $ gradle tasks

### IntelliJ IDEA

Generate IntelliJ IDEA project:

    $ gradle idea

Open with `File` -> `Open Project`.

### Oracle Driver

Download `ojdbc6_g.jar` from [Oracle](http://www.oracle.com/technetwork/apps-tech/jdbc-112010-090769.html) and save in `bin/` directory.

### Build

Build the project:

    $ gradle build

JARs [will be saved](https://github.com/johnrengelman/shadow#using-the-default-plugin-task) into the directory `build/libs/`.

### Run

Run the project:

    $ gradle run

or

    $ java -classpath build/libs/sad-api-all.jar edu.oregonstate.mist.sadapi.SadApplication server configuration.yaml


## Resources

The Web API definition is contained in the [Swagger specification](swagger.yaml).

### GET /

This sample resource returns a short message:

    $ curl --ipv4 localhost:8888
    goodbye world

### GET /{pidm}

Returns all student application decisions for a given pidm:

    $ curl --ipv4 localhost:8888/1320366
    [{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":1,"apdcDate":"2014-11-26","apdcCode":"OQ","maintInd":"S","user":"SAISPRD","dataOrigin":"Banner"},{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":3,"apdcDate":"2015-07-23","apdcCode":"AA","maintInd":"U","user":"SAISAPIS","dataOrigin":"WEB_API"},{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":4,"apdcDate":"2015-07-23","apdcCode":"AA","maintInd":"U","user":"SAISAPIS","dataOrigin":"WEB_API"},{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":5,"apdcDate":"2015-07-23","apdcCode":"AA","maintInd":"U","user":"SAISAPIS","dataOrigin":"WEB_API"},{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":6,"apdcDate":"2015-07-23","apdcCode":"AA","maintInd":"U","user":"SAISAPIS","dataOrigin":"WEB_API"},{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":7,"apdcDate":"2015-07-27","apdcCode":"AA","maintInd":"U","user":"SAISAPIS","dataOrigin":"WEB_API"}]

### POST

### PUT

### DELETE
