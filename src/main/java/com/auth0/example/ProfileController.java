package com.auth0.example;

import com.auth0.SessionUtils;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.net.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Profile controller.
 *
 * @author hung.nguyen
 */
@SuppressWarnings("unused")
@Controller
public class ProfileController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ManagementAPI apiClient;

    @Value(value = "${com.auth0.clientSecret}")
    private String clientSecret;

    @Value(value = "${com.auth0.clientId}")
    private String clientId;

    @Value(value = "${com.auth0.domain}")
    private String domain;

    @Value(value = "${com.auth0.issuer}")
    private String issuer;

    @Autowired
    public ProfileController(ManagementAPI apiClient) {
        this.apiClient = apiClient;
    }

    @GetMapping("/profile")
    protected String view( @RequestParam("state") String state,
            @RequestParam("token") String token, Model model, HttpServletRequest req) {
        logger.debug("callback token for profile: " + token);
        model.addAttribute("profile", new Profile());
        SessionUtils.set(req, "profileToken", token);
        SessionUtils.set(req, "state", state);
        return "profile";
    }

    @PostMapping("/profile")
    protected String update(@ModelAttribute Profile profile,
                            HttpServletRequest req) {
        logger.debug("Update profile");
        String profileToken = (String) SessionUtils.get(req, "profileToken");
        String state = (String) SessionUtils.get(req, "state");
        try {
            DecodedJWT jwt = JWT.decode(profileToken);
            com.auth0.json.mgmt.users.User data = new com.auth0.json.mgmt.users.User();
            Map<String, Object> meta = new HashMap<>();
            meta.put("given_name", profile.getFirstName());
            meta.put("family_name", profile.getFamilyName());
            data.setUserMetadata(meta);
            Request updateReq = apiClient.users().update( jwt.getSubject(), data);
            updateReq.execute();
            Algorithm algorithm = Algorithm.HMAC256(clientSecret);
            String responseToken = JWT.create()
                    .withIssuer(issuer)
                    .withSubject(jwt.getSubject())
                    .withAudience(clientId)
                    .withClaim("profileCompleted", Boolean.TRUE)
                    .sign(algorithm);
            String url = "https://" + domain + "/continue?state=" + state + "&token=" + responseToken;
            logger.debug("rule callback url: +" + url);
            return "redirect:" + url;
        } catch (Exception e){
            //Invalid token
        }
        return "redirect:/portal/home";
    }


}
