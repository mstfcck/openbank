# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.5/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.5/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.5/reference/web/servlet.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

### Generate Certificate

> keytool -genkeypair -alias accountservice -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore src/main/resources/keystore.p12 -validity 3650 -storepass changeit -dname "CN=localhost,OU=OpenBank,O=OpenBank,L=Local,ST=NA,C=US"

**Export the certificate from your keystore:**

> keytool -exportcert -alias accountservice -keystore src/main/resources/keystore.p12 -storetype PKCS12 -storepass changeit -rfc -file accountservice.crt

**Import the certificate into your system trust store:**

On macOS, double-click accountservice.crt and add it to the "System" keychain, then set it to "Always Trust".

On Windows, right-click and install to "Trusted Root Certification Authorities".

On Linux, use your distro's certificate management tools.

**Restart Chrome and revisit <https://localhost:8092/swagger-ui.html>.**

### Clean

> ./mvnw clean package

### Run

> ./mvnw spring-boot:run

> ./mvnw clean spring-boot:run

> cd account-service && ./mvnw clean spring-boot:run

### Kill the Port

> lsof -i :8443
> kill -9 79825
