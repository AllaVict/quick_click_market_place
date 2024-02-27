package quick.click.securityservice.commons.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import quick.click.advertservice.commons.constants.Constants;
import quick.click.securityservice.commons.utils.*;
import quick.click.securityservice.core.service.UserLoginService;
import quick.click.securityservice.core.service.impl.CustomOAuth2UserService;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserLoginService userLoginService;

    private final TokenProvider tokenProvider;

    private final CustomOAuth2UserService customOAuth2UserService;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    public SecurityConfiguration(final UserLoginService userLoginService,
                                 final TokenProvider tokenProvider,
                                 final CustomOAuth2UserService customOAuth2UserService,
                                 final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                                 final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler,
                                 final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.userLoginService = userLoginService;
        this.tokenProvider = tokenProvider;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userLoginService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, userLoginService);
    }

    /*
      By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
      the authorization request. But, since our service is stateless, we can't save it in
      the session. We'll save the request in a Base64 encoded cookie instead.
    */
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http, final HandlerMappingIntrospector introspector) throws Exception {
        final MvcRequestMatcher.Builder matcher = new MvcRequestMatcher.Builder(introspector);

        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .authenticationProvider(daoAuthenticationProvider())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations(),
                                matcher.pattern("/"),
                                matcher.pattern("/error"),
                                matcher.pattern("/favicon.ico"),
                                matcher.pattern("/*/*.png"),
                                matcher.pattern("/*/*.gif"),
                                matcher.pattern("/*/*.svg"),
                                matcher.pattern("/*/*.jpg"),
                                matcher.pattern("/*/*.html"),
                                matcher.pattern("/*/*.css"),
                                matcher.pattern("/*/*.js"))
                        .permitAll()
                        .requestMatchers(
                                matcher.pattern("/home"),
                                matcher.pattern("/v1.0/auth/login"),
                                matcher.pattern("/v1.0/auth/logout"),
                                matcher.pattern("/v1.0/auth/signup"),
                                matcher.pattern("/v1.0/adverts"),
                                matcher.pattern("/v1.0/adverts/*"),
                                matcher.pattern("/auth/login"),
                                matcher.pattern("/auth/logout"),
                                matcher.pattern("/auth/signup"),
                                matcher.pattern("/oauth2/*"),
                                matcher.pattern("/swagger-ui/*")
                        ).permitAll()
                        // .requestMatchers("/v1.0/**").hasAuthority("ADMIN")
                        .requestMatchers("/auth/admin").hasRole("ADMIN")
                        .requestMatchers("/auth/user").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin.disable())
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/oauth2/authorize")
                                .authorizationRequestRepository(cookieAuthorizationRequestRepository()))
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/oauth2/callback/*"))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage(Constants.Endpoints.LOGIN_URL)
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

}