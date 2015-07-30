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

The table `SARAPPD` has a composite primary key constraint on the columns `SARAPPD_PIDM`, `SARAPPD_TERM_CODE_ENTRY`, `SARAPPD_APPL_NO`, and `SARAPPD_SEQ_NO`. Valid values may be obtained with an SQL query like this:

    SELECT SARAPPD_PIDM, SARAPPD_TERM_CODE_ENTRY, SARAPPD_APPL_NO, SARAPPD_SEQ_NO
    FROM SARAPPD
    WHERE SARAPPD_TERM_CODE_ENTRY LIKE '2016__'
    AND ROWNUM <= 10
    ORDER BY SARAPPD_TERM_CODE_ENTRY;

Examples of valid values:

    SARAPPD_PIDM SARAPPD_TERM_CODE_ENTRY SARAPPD_APPL_NO SARAPPD_SEQ_NO
    ------------ ----------------------- --------------- --------------
         1311112 201601                                2              1 
         1320251 201601                                1              1 
         1320254 201601                                1              1 
         1320255 201601                                1              1 
         1320263 201601                                1              1 
         1320256 201601                                1              1 
         1320257 201601                                1              1 
         1320261 201601                                1              1 
         1320255 201601                                1              2 
         1320262 201602                                1              1 
    
     10 rows selected 

### GET /{pidm}

Return one student application decision for a given pidm, term code, application number, and sequence number:

    $ nc localhost 8888 << HERE
    > GET /api/v0/1320366?termCodeEntry=201600&applNo=1&seqNo=1 HTTP/1.0
    > 
    > HERE
    HTTP/1.1 200 OK
    Date: Thu, 30 Jul 2015 00:56:01 GMT
    Content-Type: application/json
    Content-Length: 156
    
    {"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":1,"apdcDate":"2014-11-26","apdcCode":"OQ","maintInd":"S","user":"SAISPRD","dataOrigin":"Banner"}

### GET /{pidm}?termCodeEntry={tce}&applNo={an}&seqNo={seqNo}

Return all student application decisions for a given pidm:

    $ nc localhost 8888 << HERE
    > GET /api/v0/1320366 HTTP/1.0
    > 
    > HERE
    HTTP/1.1 200 OK
    Date: Thu, 30 Jul 2015 00:53:24 GMT
    Content-Type: application/json
    Vary: Accept-Encoding
    Content-Length: 953
    
    [{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":1,"apdcDate":"2014-11-26","apdcCode":"OQ","maintInd":"S","user":"SAISPRD","dataOrigin":"Banner"},{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":3,"apdcDate":"2015-07-23","apdcCode":"AA","maintInd":"U","user":"SAISAPIS","dataOrigin":"WEB_API"},{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":4,"apdcDate":"2015-07-23","apdcCode":"AA","maintInd":"U","user":"SAISAPIS","dataOrigin":"WEB_API"},{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":5,"apdcDate":"2015-07-23","apdcCode":"AA","maintInd":"U","user":"SAISAPIS","dataOrigin":"WEB_API"},{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":6,"apdcDate":"2015-07-23","apdcCode":"AA","maintInd":"U","user":"SAISAPIS","dataOrigin":"WEB_API"},{"pidm":1320366,"termCodeEntry":"201600","applNo":1,"seqNo":7,"apdcDate":"2015-07-27","apdcCode":"AA","maintInd":"U","user":"SAISAPIS","dataOrigin":"WEB_API"}]

### POST

### PUT

### DELETE
