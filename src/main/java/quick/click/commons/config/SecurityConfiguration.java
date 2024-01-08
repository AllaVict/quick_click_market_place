package quick.click.commons.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import quick.click.commons.constants.Constants;
import quick.click.core.service.UserLoginService;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final UserLoginService userLoginService;

    @Autowired
    public SecurityConfiguration(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userLoginService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http, final HandlerMappingIntrospector introspector) throws Exception {
        final MvcRequestMatcher.Builder matcher = new MvcRequestMatcher.Builder(introspector);

        http
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(daoAuthenticationProvider())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations(),
                                matcher.pattern("/css/**"))
                        .permitAll()
                        .requestMatchers(
                                matcher.pattern("/home"),
                                matcher.pattern("/login"),
                                matcher.pattern("/v1.0/login"),
                                matcher.pattern("/swagger-ui/**")
                        ).permitAll()
                        // .requestMatchers("/v1.0/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage(Constants.Endpoints.LOGIN_URL)
                        .usernameParameter("username").passwordParameter("password")
                        .defaultSuccessUrl("/for-signed-users")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage(Constants.Endpoints.LOGIN_URL)
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                ).httpBasic(withDefaults()); // must be

        return http.build();
    }

}