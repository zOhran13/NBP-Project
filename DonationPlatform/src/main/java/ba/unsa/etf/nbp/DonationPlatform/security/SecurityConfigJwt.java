package ba.unsa.etf.nbp.DonationPlatform.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import static ba.unsa.etf.nbp.DonationPlatform.enums.RoleName.Admin;
import static org.springframework.http.HttpMethod.GET;

@Configuration
public class SecurityConfigJwt {

    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfigJwt(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Omogući CORS
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/v3/api-docs.yaml", "/swagger-resources/**", "/webjars/**").permitAll()
                        .requestMatchers("/api/users/login").permitAll()
                        .requestMatchers("/api/users/me").permitAll()
                        .requestMatchers("/api/users/register").permitAll()
                        .requestMatchers("/api/users/username/**").hasAnyRole("ADMIN","VOLONTER","DONATOR")
                        .requestMatchers(HttpMethod.GET, "/api/events/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/events/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/events/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/events/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/campaign/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/campaign/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/campaign/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/profile").hasAnyRole("ADMIN","VOLONTER","DONATOR")
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/reports/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/volunteers/count/**").permitAll()
                        .requestMatchers("/api/volunteers/**").hasAnyRole("DONATOR", "VOLONTER")
                        .requestMatchers("/api/address/**").permitAll()
                        .requestMatchers("/reports/user/**").hasAnyRole("ADMIN","VOLONTER","DONATOR")
                        .requestMatchers(GET,"/api/users/users").hasRole("ADMIN")
                        .requestMatchers("/api/user/logout").hasAnyRole("ADMIN","VOLONTER","DONATOR")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of("http://localhost:5173","http://localhost:8080"));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}