package com.example.telproject.security.config;

import com.example.telproject.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final ClientService clientService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**

     Configures the Spring Security filter chain to handle authentication and authorization for API requests.
     @param http the HttpSecurity object to configure
     @param auth the AuthenticationManagerBuilder used for authentication
     @return the configured SecurityFilterChain
     @throws Exception if there is an error in the authentication configuration
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManagerBuilder auth) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v*/client/registration/**")
                .permitAll()
                .anyRequest()
                .permitAll().and()
                .formLogin();
        auth.authenticationProvider(daoAuthenticationProvider());
        return http.build();
    }

    /**

     Creates a DaoAuthenticationProvider that uses the BCryptPasswordEncoder to encode passwords and the ClientService for user details.
     @return the configured DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(clientService);
        return provider;
    }
}
