package com.usermanagement.core.application.config;

import com.usermanagement.core.application.config.exceptions.BaseException;
import com.usermanagement.core.infrastructure.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret-key}")
    private String base64SecretKey;

    private final UserRepository userRepository;

    /**
     * Processes an HTTP request, extracts the token, validates it, and sets the authentication in the security context.
     * It then passes control to the next filter in the filter chain.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during request processing
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = extractToken(request);
        if (Boolean.TRUE.equals(validateToken(token))) {
            setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Attempts to extract the JWT token from the Authorization header of the incoming request.
     * If the token is successfully extracted and has the correct Bearer prefix, it returns the token.
     *
     * @param request The incoming HttpServletRequest.
     * @return The JWT token without the Bearer prefix.
     * @throws BaseException if the Authorization header is not present or doesn't start with "Bearer ".
     */
    private String extractToken(HttpServletRequest request) {
        var bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Validates the provided JWT token using the HMAC SHA key.
     * The token is considered valid if it's properly signed and not expired or malformed.
     *
     * @param token The JWT token to be validated.
     * @return Boolean.TRUE if the token is valid, Boolean.FALSE otherwise.
     */
    private Boolean validateToken(String token) {
        try {
            byte[] decodedKey = Decoders.BASE64.decode(base64SecretKey);
            var key = Keys.hmacShaKeyFor(decodedKey);
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    /**
     * Validates the provided JWT token's signature and structure, returning true if it is valid.
     * If the token is invalid, false is returned. Uses the base64SecretKey for verification.
     *
     * @param token the JWT token to validate.
     */
    private void setAuthentication(String token) {
        var email = extractUsernameFromToken(token);
        userRepository.findByEmail(email).ifPresent(user -> {
            var authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });
    }

    /**
     * Extracts the username (subject) from the given JWT token using the provided base64SecretKey.
     *
     * @param token the JWT token from which to extract the username.
     * @return the username (subject) contained within the token.
     */
    private String extractUsernameFromToken(String token) {
        byte[] decodedKey = Decoders.BASE64.decode(base64SecretKey);
        var key = Keys.hmacShaKeyFor(decodedKey);
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }

}