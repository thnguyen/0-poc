
# Auth0 Progressive Profiling POC

## Getting started

This POC demonstrates how to use Auth0 to perform progressive profile. It is built using Spring Boot with the `mvc-auth-commons` library. Download or clone this repository and follow the instructions below to setup the POC.

### Auth0 Dashboard
1. On the [Auth0 Dashboard](https://manage.auth0.com/#/clients) create a new Client of type `Regular Web Application`.
1. Add the URL that will be called on an OAuth successful login to the Allowed Callback URLs. i.e.: `http://localhost:8080/callback`.
1. Add the URL that will be called on logout to the Allowed Logout URLs. i.e.: `http://localhost:8080/logout`.
1. Copy the `Domain`, `Client ID` and `Client Secret` values at the top of the page and use them to configure the Java Application.
1. Follow [Auth0 Management API Dashboard](https://auth0.com/docs/api/management/v2/tokens#get-a-token-manually) to create an API token.
1. On the [Auth0 Dashboard Rule](https://manage.auth0.com/#/rules) create a rule using the javascript content at src/main/resources/rules/enable_progressive_profile.js (you can name the rule as "enable progressive profile").
1. (Optional) on the [Auth0 Dashboard Connections -> Social](https://manage.auth0.com/#/connections/social), enable Google and Facebook and follow the prompt to add social login/signup.

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

### Demo steps
1. Disable Auth0 rule created above.
1. Visit https://localhost:8080/portal/home and sign up using normal email / password.
1. Click Logout and follow the screen to Login again. You should be able to see the portal home page.
1. Enable Auth0 rule.
1. Click Logout and follow the screen to Login again. Now a profile update page show up asking you to fill in First Name and Family Name.
1. After update First Name and Family Name, you should be able to go to portal homepage.

### Online Demo

A personal online demo at: http://91.204.208.170:8080/portal/home

### Limitations

For demonstration purpose, many checks have been ignored such as:
1. No required check for both First Name and Family Name on profile update page. The assumption is that both fields will be filled.
1. Management API Token has expiration time. In real world cases, token should be programmatically acquired.
1. Rule logic assumes all or nothing approach that either all given_name, familyname, user_metadata.given_name, user_metadata.family_name are filled. So as long as name pair is available in root or in user_metadata.
1. Management API doesn't allow modification of root given_name and family_name. Therefore, user.user_metadata is used.

## Author

[Hung Nguyen](https://www.linkedin.com/in/tronghungnguyen/)

## License

This project is licensed under the MIT license. See the [LICENSE](LICENSE.txt) file for more info.
