package com.auth0.example;

import com.auth0.SessionUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Auth0Filter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String accessToken = (String) SessionUtils.get(req, "accessToken");
        DecodedJWT idToken = (DecodedJWT) SessionUtils.get(req, "decodedIdToken"); // responseType = code id_token
        Boolean profileCompleted = (Boolean) SessionUtils.get(req, "profileCompleted");

        if (idToken == null) {
            res.sendRedirect("/login");
            return;
        } else if (profileCompleted != null) {
            next.doFilter(request, response);
            return;
        } else {
            String profileStatus = idToken.getClaim("https://hiddencharm.auth0.com/profile").asString();
            if ("incomplete".equals(profileStatus)) {
                res.sendRedirect("/profile");
                return;
            }
        }
        next.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
