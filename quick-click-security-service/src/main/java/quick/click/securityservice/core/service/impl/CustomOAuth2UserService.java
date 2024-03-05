package quick.click.securityservice.core.service.impl;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import quick.click.advertservice.commons.exceptions.ResourceNotFoundException;
import quick.click.advertservice.core.domain.model.User;
import quick.click.advertservice.core.enums.AuthProvider;
import quick.click.advertservice.core.repository.FileReferenceRepository;
import quick.click.advertservice.core.repository.UserRepository;
import quick.click.securityservice.commons.exceptions.OAuth2AuthenticationProcessingException;
import quick.click.securityservice.commons.model.AuthenticatedUser;
import quick.click.securityservice.commons.model.dto.OAuth2UserInfo;
import quick.click.securityservice.commons.utils.OAuth2UserInfoFactory;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final FileReferenceRepository fileReferenceRepository;

    public CustomOAuth2UserService(final UserRepository userRepository,
                                   final FileReferenceRepository fileReferenceRepository) {
        this.userRepository = userRepository;
        this.fileReferenceRepository = fileReferenceRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(final OAuth2UserRequest oAuth2UserRequest, final OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
                                .getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                                oAuth2User.getAttributes());
        if(StringUtils.hasLength(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        /**=======================================================*/
        Optional<User> userOptional = userRepository.findUserByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return AuthenticatedUser.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();
        /**=======================================================*/
        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setFirstName(oAuth2UserInfo.getName());
        user.setAvatar(fileReferenceRepository
                .findByFileUrl(oAuth2UserInfo.getImageUrl()).orElseThrow(
                        () -> new ResourceNotFoundException("fileReference", "url", oAuth2UserInfo.getImageUrl()) ));
        /**=======================================================*/
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFirstName(oAuth2UserInfo.getName());
        existingUser.setAvatar(fileReferenceRepository
                .findByFileUrl(oAuth2UserInfo.getImageUrl()).orElseThrow(
                        () -> new ResourceNotFoundException("fileReference", "url", oAuth2UserInfo.getImageUrl()) ));
        /**=======================================================*/
        return userRepository.save(existingUser);
    }

}
