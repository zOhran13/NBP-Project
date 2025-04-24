package ba.unsa.etf.nbp.DonationPlatform.security;

import ba.unsa.etf.nbp.DonationPlatform.enums.RoleName;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenHelper jwtTokenHelper;
    private final UserRepository userRepository;

    public JwtTokenFilter(JwtTokenHelper jwtTokenHelper, UserRepository userRepository) {
        this.jwtTokenHelper = jwtTokenHelper;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("HTTP Method: " + request.getMethod());

        String path = request.getRequestURI();
        if (isSwaggerPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                List<String> allowedRoles = List.of(
                        RoleName.VOLONTER.name(),
                        RoleName.Admin.name(),
                        RoleName.DONATOR.name()
                );

                if (jwtTokenHelper.validateTokenAndItsClaims(token, allowedRoles)) {
                    String email = jwtTokenHelper.getClaimsFromToken(token).get("email", String.class);
                    String role = jwtTokenHelper.getClaimsFromToken(token).get("role", String.class);

                    String mappedRole = "ROLE_" + role.toUpperCase();

                    User userDetails = userRepository.findByEmail(email)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

                    var authorities = List.of(new SimpleGrantedAuthority(mappedRole));

                    logger.info("Korisnik pronađen: " + userDetails.getEmail());
                    logger.info("Mapped Authorities: " + authorities);

                    var authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    logger.info("SecurityContext postavljen za korisnika: " + userDetails.getEmail());
                }

            } catch (Exception e) {
                logger.error("Could not set user authentication in security context", e);
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isSwaggerPath(String path) {
        return path.contains("/swagger-ui") ||
                path.contains("/v3/api-docs") ||
                path.contains("/swagger-resources") ||
                path.contains("/webjars");
    }
}
