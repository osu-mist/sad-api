# Student Application Decision Web API

Web API for creating, reading, updating, and deleting student application decision codes.


## Tasks

List all tasks runnable from root project:

    $ gradle tasks

### IntelliJ IDEA

Generate IntelliJ IDEA project:

    $ gradle idea

Open with `File` -> `Open Project`.

### Dependencies

#### Oracle Driver

Download `ojdbc6_g.jar` from [Oracle](http://www.oracle.com/technetwork/apps-tech/jdbc-112010-090769.html) and save in `bin/` directory.

#### Private Files

Add private repository `sad-api-contrib` as a remote:

    $ git remote add contrib /path/to/sad-api-contrib.git
    $ git fetch contrib

Overlay files:

    $ git checkout feature/abc-123-xyz
    $ git merge --no-commit contrib/feature/abc-123-xyz

See [readme-contrib.md](readme-contrib.md) for more details.

### Build

Build the project:

    $ gradle build

JARs [will be saved](https://github.com/johnrengelman/shadow#using-the-default-plugin-task) into the directory `build/libs/`.

### Run

Run the project:

    $ gradle run

or

    $ java -jar build/libs/sad-api-all.jar server configuration.yaml

### Clean

Remove private files:

    $ git merge --abort

Remove generated files:

    $ gradle clean

## Resources

The Web API definition is contained in the [Swagger specification](swagger.yaml).

### GET /{pidm}

Return all student application decisions for a given pidm:

    $ nc localhost 8888 << HERE
    > GET /api/v0/1320366 HTTP/1.0
    > 
    > HERE
    HTTP/1.1 200 OK
    Date: Mon, 10 Aug 2015 22:29:45 GMT
    Content-Type: application/json
    Vary: Accept-Encoding
    Content-Length: 709
    
    [{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":1,"apdcDate":"2014-11-26","apdcCode":"OQ","maintInd":"S"},{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":3,"apdcDate":"2015-07-23","apdcCode":"AA","maintInd":"U"},{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":4,"apdcDate":"2015-07-23","apdcCode":"AA","maintInd":"U"},{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":5,"apdcDate":"2015-07-23","apdcCode":"AA","maintInd":"U"},{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":6,"apdcDate":"2015-07-23","apdcCode":"AA","maintInd":"U"},{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":7,"apdcDate":"2015-07-27","apdcCode":"AA","maintInd":"U"}]

### GET /{pidm}?termCodeEntry={tce}&applNo={an}&seqNo={sn}

Return one student application decision for a given pidm, term code, application number, and sequence number:

    $ nc localhost 8888 << HERE
    > GET /api/v0/1320366?termCodeEntry=201600&applNo=1&seqNo=1 HTTP/1.0
    > 
    > HERE
    HTTP/1.1 200 OK
    Date: Mon, 10 Aug 2015 22:31:23 GMT
    Content-Type: application/json
    Content-Length: 117
    
    {"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":1,"apdcDate":"2014-11-26","apdcCode":"OQ","maintInd":"S"}

### POST

### PUT

Update student application decision:

    $ nc localhost 8888 << HERE
    > PUT /api/v0/1320366 HTTP/1.0
    > Content-Type: application/json
    > Content-Length: 117
    > 
    > {"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":1,"apdcDate":"2015-08-13","apdcCode":"AT","maintInd":"S"}
    > 
    > HERE
    HTTP/1.1 200 OK
    Date: Wed, 12 Aug 2015 22:34:26 GMT
    Content-Type: application/json
    Content-Length: 117
    
    {"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":1,"apdcDate":"2015-08-13","apdcCode":"AT","maintInd":"S"}

### DELETE
