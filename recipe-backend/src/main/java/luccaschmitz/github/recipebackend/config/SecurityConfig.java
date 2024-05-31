package luccaschmitz.github.recipebackend.config;

import luccaschmitz.github.recipebackend.security.JwtAuthenticationEntryPoint;
import luccaschmitz.github.recipebackend.security.JwtAuthenticationFilter;
import luccaschmitz.github.recipebackend.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
public class SecurityConfig {

  private final JwtAuthenticationEntryPoint unauthorizedHandler;
  private final JwtTokenProvider tokenProvider;
  private final UserDetailsService userDetailsService;

  public SecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler, JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
    this.unauthorizedHandler = unauthorizedHandler;
    this.tokenProvider = tokenProvider;
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(exceptionHandling ->
            exceptionHandling.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated())
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(tokenProvider, userDetailsService);
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}