package ru.feryafox.carrental.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.feryafox.carrental.services.CustomAuthenticationSuccessHandler;
import ru.feryafox.carrental.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;


    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/public/**", "/login", "/registration").permitAll()
                        .requestMatchers("/manager/**").hasRole("MANAGER")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/washer/**").hasRole("WASHER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/header/**").hasRole("HEADER")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL для запроса выхода (по умолчанию /logout)
                        .logoutSuccessUrl("/login") // URL для редиректа после выхода
                        .invalidateHttpSession(true) // Аннулировать сессию
                        .deleteCookies("JSESSIONID") // Удалить cookie сессии
                )
                .authenticationProvider(daoAuthenticationProvider()); // Используем authenticationProvider

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}