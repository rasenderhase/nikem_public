package de.nikem.playground.oauth;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthHelper;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthHttpClient;

@WebServlet(description = "Handles OAuth requests", urlPatterns = {"/oauthservlet/"})
public class OAuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CONSUMER_KEY = "anonymous";
	private static final String CONSUMER_SECRET = "anonymous";

	/**
	 * Google's OAuth endpoints
	 */
	private static String host = "www.google.com";
	private static String requestTokenUrl = "https://" + host + "/accounts/OAuthGetRequestToken";
	private static String userAuthorizationUrl = "https://" + host + "/accounts/OAuthAuthorizeToken";
	private static String accessTokenUrl = "https://" + host + "/accounts/OAuthGetAccessToken";
	private static String revokeTokenUrl = "https://" + host + "/accounts/AuthSubRevokeToken";

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

		String oauth_verifier = req.getParameter("oauth_verifier");

		if (oauth_verifier == null) {

			try {
				GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
				oauthParameters.setOAuthConsumerKey(CONSUMER_KEY);
				oauthParameters.setOAuthConsumerSecret(CONSUMER_SECRET);
				oauthParameters.setScope("https://www.googleapis.com/auth/userinfo.profile");
				oauthParameters.setOAuthCallback("http://localhost:8080/playground/oauthservlet/");
				oauthParameters.addExtraParameter("xoauth_displayname", "Andi's OAuth Test Application");

				OAuthHelper oauthHelper = createOauthHelper();
				oauthHelper.getUnauthorizedRequestToken(oauthParameters);

				System.out.println("oauth_token " + oauthParameters.getOAuthToken());
				System.out.println("oauth_token_secret " + oauthParameters.getOAuthTokenSecret());

				req.getSession().setAttribute("oauth_token", oauthParameters.getOAuthToken());
				req.getSession().setAttribute("oauth_token_secret", oauthParameters.getOAuthTokenSecret());

				String approvalPageUrl = oauthHelper.createUserAuthorizationUrl(oauthParameters);
				resp.sendRedirect(approvalPageUrl);

			} catch (OAuthException e) {
				e.printStackTrace();
			}
		} else {

			try {
				GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
				oauthParameters.setOAuthConsumerKey(CONSUMER_KEY);
				oauthParameters.setOAuthConsumerSecret(CONSUMER_SECRET);
				oauthParameters.setOAuthTokenSecret((String) req.getSession().getAttribute("oauth_token_secret")); // TODO: Session!

				OAuthHelper oauthHelper = createOauthHelper();
				oauthHelper.getOAuthParametersFromCallback(req.getQueryString(), oauthParameters);

				String accessToken = oauthHelper.getAccessToken(oauthParameters);
				// You can also pull the OAuth token string from the oauthParameters:
				// String accessToken = oauthParameters.getOAuthToken();
				System.out.println("OAuth Access Token: " + accessToken);

				String accessTokenSecret = oauthParameters.getOAuthTokenSecret();
				System.out.println("OAuth Access Token's Secret: " + accessTokenSecret);

				// Use of access token
				oauthParameters = new GoogleOAuthParameters();
				oauthParameters.setOAuthConsumerKey(CONSUMER_KEY);
				oauthParameters.setOAuthConsumerSecret(CONSUMER_SECRET);
				oauthParameters.setOAuthToken(accessToken);
				oauthParameters.setOAuthTokenSecret(accessTokenSecret);

				OAuthHelper helper = createOauthHelper();
				URL url = helper.getOAuthUrl("https://www.googleapis.com/oauth2/v1/userinfo?alt=json", "GET", oauthParameters);
				String response = new OAuthHttpClient().getResponse(url);
				System.out.println(response);

				req.getSession().setAttribute("user", response);

				resp.sendRedirect("/playground/oauth/");
			} catch (OAuthException e) {
				e.printStackTrace();
			}
		}
	}

	private OAuthHelper createOauthHelper() {
		OAuthHttpClient httpClient = new OAuthHttpClient();
		OAuthHelper oauthHelper = new OAuthHelper(requestTokenUrl, userAuthorizationUrl, accessTokenUrl, revokeTokenUrl, new OAuthHmacSha1Signer(), httpClient);
		return oauthHelper;
	}
}
