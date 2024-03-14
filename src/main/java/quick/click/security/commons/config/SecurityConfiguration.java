package quick.click.security.commons.config;

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
import quick.click.commons.constants.Constants;
import quick.click.security.commons.utils.*;
import quick.click.security.core.service.UserLoginService;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserLoginService userLoginService;

    private final TokenProvider tokenProvider;

    @Autowired
    public SecurityConfiguration(final UserLoginService userLoginService,
                                 final TokenProvider tokenProvider){
        this.userLoginService = userLoginService;
        this.tokenProvider = tokenProvider;
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

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http, final HandlerMappingIntrospector introspector) throws Exception {
        final MvcRequestMatcher.Builder matcher = new MvcRequestMatcher.Builder(introspector);

        http
                //.csrf(csrf -> csrf.disable())
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .headers(httpSecurityHeadersConfigurer -> {
                    httpSecurityHeadersConfigurer.frameOptions(frameOptionsConfig -> {
                        frameOptionsConfig.disable();
                    });
                })
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
                                matcher.pattern("/*/*.js")
                        )
                        .permitAll()
                        .requestMatchers(
                                toH2Console(),
                                matcher.pattern("/home"),
                                matcher.pattern("/v1.0/auth/login"),
                                matcher.pattern("/v1.0/auth/logout"),
                                matcher.pattern("/v1.0/auth/signup"),
                                matcher.pattern("/v1.0/adverts"),
                                matcher.pattern("/v1.0/adverts/*"),
                                matcher.pattern("/v1.0/comments"),
                                matcher.pattern("/v1.0/comments/*"),
                                matcher.pattern("/v1.0/images"),
                                matcher.pattern("/v1.0/images/*"),
                                matcher.pattern("/oauth2/*"),
                                matcher.pattern("/swagger-ui/*")
                        ).permitAll()
                            // .requestMatchers("/v1.0/**").hasAuthority("ADMIN")
                        .requestMatchers("/auth/admin").hasRole("ADMIN")
                        .requestMatchers("/auth/user").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin.disable())
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