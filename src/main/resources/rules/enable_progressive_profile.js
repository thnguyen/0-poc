function (user, context, callback) {
  user.user_metadata = user.user_metadata || {};
  if (context.protocol !== "redirect-callback") {
    if (!user.given_name && !user.family_name && !user.user_metadata.given_name && !user.user_metadata.family_name) {
      // User has initiated a login and is forced to update profile
      // Send user's information in a JWT to avoid tampering
      function createToken(clientId, clientSecret, issuer, user) {
        var options = {
          expiresInMinutes: 5,
          audience: clientId,
          issuer: issuer
        };
        return jwt.sign(user, clientSecret, options);
      }
      var token = createToken(
        configuration.CLIENT_ID,
        configuration.CLIENT_SECRET,
        configuration.ISSUER, {
          sub: user.user_id,
          email: user.email
        }
      );
      context.redirect = {
        url: configuration.PROFILE_URL + token // http://localhost:8080/profile?token=
      };
    }
    return callback(null, user, context);
  } else {
    // User has been redirected to /continue?token=..., profile completion must be validated
    // The generated token must include a `profileCompleted` claim to confirm the password change
    function verifyToken(clientId, clientSecret, issuer, token, cb) {
      jwt.verify(
        token,
        clientSecret, {
          audience: clientId,
          issuer: issuer
        },
        cb
      );
    }
    function postVerify(err, decoded) {
      if (err) {
        return callback(new UnauthorizedError("Profile change failed"));
      } else if (decoded.sub !== user.user_id) {
        return callback(new UnauthorizedError("Token does not match the current user"));
      } else if (!decoded.profileCompleted) {
        return callback(new UnauthorizedError("Password change was not confirmed"));
      } else {
        // User's password has been changed successfully
        return callback(null, user, context);
      }
    }
    verifyToken(
      configuration.CLIENT_ID,
      configuration.CLIENT_SECRET,
      configuration.ISSUER,
      context.request.query.token,
      postVerify
    );
  }
}