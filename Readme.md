**Description**
This is a sample java [Duo](https://duo.com/) two factor authentication client that illustrates (hopefully in a straightforward manner) the proper usage of the [Auth REST API](https://duo.com/docs/authapi) . The official DUO client is somewhat confusing for actual developers to use (and I'm still not sure why they decided to concatenate body parameters instead of putting them in a map as is normally done). This wasn't mentioned clearly in the documentation and it took some detective work to figure out what was going on.


In this project, I've tried to illustrate the implementation of a few basic endpoints and extract their responses into POJOs (/ping, /check and /auth).

**Configuration**
Configure your endpoint and keys (available from the Duo admin interface) in the duo-client-java-spring-client/src/main/resources/application.properties file.

**Usage**
Write your application logic in the DuoClientApplication.java class. 

**Tests**
See DuoClientApplicationTests.java