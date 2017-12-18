package com.auth0.example;

import com.auth0.SessionUtils;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.net.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    public ProfileController(ManagementAPI apiClient) {
        this.apiClient = apiClient;
    }

    @GetMapping("/profile")
    protected String view(Model model) {
        logger.debug("Profile");
        model.addAttribute("profile", new Profile());
        return "profile";
    }

    @PostMapping("/profile")
    protected String update(@ModelAttribute Profile profile,
                            HttpServletRequest req) {
        logger.debug("Update profile");
        DecodedJWT idToken = (DecodedJWT) SessionUtils.get(req, "decodedIdToken");
        com.auth0.json.mgmt.users.User data = new com.auth0.json.mgmt.users.User();
        Map<String, Object> meta = new HashMap<>();
        meta.put("given_name", profile.getFirstName());
        meta.put("family_name", profile.getFamilyName());
        data.setUserMetadata(meta);
        Request updateReq = apiClient.users().update( idToken.getSubject(), data);
        try {
            updateReq.execute();
        } catch (Exception e) {
            logger.error("Problem updating User Profile " + e.getMessage());
        }
        SessionUtils.set(req, "profileCompleted", Boolean.TRUE);
        return "redirect:/portal/home";
    }


}
