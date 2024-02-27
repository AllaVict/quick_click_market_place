package quick.click.securityservice.commons.utils;

import quick.click.advertservice.core.enums.AuthProvider;
import quick.click.advertservice.security.commons.model.dto.GoogleOAuth2UserInfo;
import quick.click.advertservice.security.commons.model.dto.OAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
