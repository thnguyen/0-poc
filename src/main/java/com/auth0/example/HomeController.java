package com.auth0.example;

import com.auth0.SessionUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("unused")
@Controller
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/portal/home", method = RequestMethod.GET)
    protected String home(final Model model, final HttpServletRequest req) {
        logger.info("Home page");
        String idToken = (String) SessionUtils.get(req, "idToken");
        try {
            DecodedJWT jwt = JWT.decode(idToken);
            model.addAttribute("name", jwt.getClaim("name").asString());
            model.addAttribute("payload", jwt.getPayload());
        }
        catch (Exception e) { }
        return "home";
    }

}
