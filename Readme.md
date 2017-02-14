**Description**
This is a sample java [Duo](https://duo.com/) two factor authentication client that illustrates (hopefully in a straightforward manner) the proper usage of the [Auth REST API](https://duo.com/docs/authapi) . The official DUO client is somewhat confusing for actual developers to use (and I'm still not sure why they decided to concatenate body parameters instead of putting them in a map as is normally done). This wasn't mentioned clearly in the documentation and it took some detective work to figure out what was going on.


In this project, I've tried to extract a few basic methods and responses (/ping, /check and /auth).

**Configuration**
Configure your endpoint and keys (available from the Duo admin interface) in the application.properties file.

**Usage**
Write your application logic in the DuoClientApplication.java class. 

**Tests**
See DuoClientApplicationTests.java