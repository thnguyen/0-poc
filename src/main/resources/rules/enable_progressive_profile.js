function (user, context, callback) {
  user.user_metadata = user.user_metadata || {};
  if (!user.given_name && !user.family_name && !user.user_metadata.given_name && !user.user_metadata.family_name) {
    context.idToken['https://hiddencharm.auth0.com/profile'] = 'incomplete';
  }
  callback(null, user, context);
}