package com.auth0.example;

import com.auth0.client.mgmt.ManagementAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.management.MXBean;
import javax.servlet.Filter;

@SuppressWarnings("unused")
@Component
@Configuration
public class AppConfig {
    /**
     * This is your auth0 domain (tenant you have created when registering with auth0 - account name)
     */
    @Value(value = "${com.auth0.domain}")
    private String domain;

    /**
     * This is the client id of your auth0 application (see Settings page on auth0 dashboard)
     */
    @Value(value = "${com.auth0.clientId}")
    private String clientId;

    /**
     * This is the client secret of your auth0 application (see Settings page on auth0 dashboard)
     */
    @Value(value = "${com.auth0.clientSecret}")
    private String clientSecret;

    /**
     * This is the management API token of your auth0 application (see Settings page on auth0 dashboard)
     */
    @Value(value = "${com.auth0.apiToken}")
    private String apiToken;

    @Bean
    public FilterRegistrationBean loginFilter() {
        return createFilterRegistration(new Auth0Filter());
    }

    @Bean
    public ManagementAPI apiClient() {
        return new ManagementAPI(getDomain(), getApiToken());
    }

    private FilterRegistrationBean createFilterRegistration(Filter filter) {
        final FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.addUrlPatterns("/portal/*");
        registration.setName(filter.getClass().getSimpleName());
        return registration;
    }

    public String getDomain() {
        return domain;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getApiToken() {
        return apiToken;
    }
}
