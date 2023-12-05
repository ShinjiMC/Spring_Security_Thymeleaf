package com.tecsup.ferreteria.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.tecsup.ferreteria.security.UserAccountService;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SpringSecurityConfig {
    private final UserAccountService userAccountService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        String loginUrl = "/login";
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(httpRequest -> httpRequest.requestMatchers("/",
                "register",
                "/profile**",
                "/agregateProduct**",
                "/newProduct**",
                "/editProduct**")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/customers/*")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/products/**")
                .permitAll()
                .anyRequest()
                .authenticated())
                .formLogin(formLogin -> formLogin.loginPage(loginUrl)
                        .loginProcessingUrl(loginUrl)
                        .defaultSuccessUrl("/profile")
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl(loginUrl)
                        .permitAll());
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userAccountService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http)
            throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.authenticationProvider(daoAuthenticationProvider());
        return auth.build();
    }
}
