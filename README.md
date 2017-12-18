
# Auth0 Progressive Profiling POC

## Getting started

This POC demonstrates how to use Auth0 to perform progressive profile. It is built using Spring Boot with the `mvc-auth-commons` library. Download or clone this repository and follow the instructions below to setup the POC.

### Auth0 Dashboard
1. On the [Auth0 Dashboard](https://manage.auth0.com/#/clients) create a new Client of type `Regular Web Application`.
1. Add the URL that will be called on an OAuth successful login to the Allowed Callback URLs. i.e.: `http://localhost:8080/callback`.
1. Add the URL that will be called on logout to the Allowed Logout URLs. i.e.: `http://localhost:8080/logout`.
1. Copy the `Domain`, `Client ID` and `Client Secret` values at the top of the page and use them to configure the Java Application.
1. Follow [Auth0 Management API Dashboard](https://auth0.com/docs/api/management/v2/tokens#get-a-token-manually) to create an API token.
1. On the [Auth0 Dashboard Rule](https://manage.auth0.com/#/rules) create a rule using the javascript content at src/src/main/resources/rules/enable_progressive_profile.js (you can name the rule as "enable progressive profile").

### Java Application
Set the client values in the `src/main/resources/auth0.properties` file. They are read by the `AppConfig` class.

```xml
com.auth0.domain: {YOUR_AUTH0_DOMAIN}
com.auth0.clientId: {YOUR_AUTH0_CLIENT_ID}
com.auth0.clientSecret: {YOUR_AUTH0_CLIENT_SECRET}
com.auth0.apiToken: {YOUR_AUTH0_MANAGEMENT_API_TOKEN}
```

### Running the sample

Open a terminal, go to the project root directory and run the following command:

```bash
./gradlew clean bootRun
```

The server will be accessible on https://localhost:8080/portal/home.

### Online Demo


## Author

[Hung Nguyen](https://www.linkedin.com/in/tronghungnguyen/)

## License

This project is licensed under the MIT license. See the [LICENSE](LICENSE.txt) file for more info.
