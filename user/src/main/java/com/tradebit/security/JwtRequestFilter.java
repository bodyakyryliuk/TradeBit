package com.tradebit.security;

import com.google.common.net.HttpHeaders;
import com.tradebit.exception.JwtNotValidException;
import com.tradebit.exception.UserNotFoundException;
import com.tradebit.user.models.User;
import com.tradebit.user.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private final UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
        }

        if (jwt != null) {
            // Validate the token
            try {
                validateToken(jwt);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public Boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            if(userRepository.findByEmail(claimsJws.getBody().getSubject()).isEmpty())
                throw new UserNotFoundException("User with given JWT doesn't exist");

            log.info("Token validated");
            saveUserInSecurityContextHolder(claimsJws);
            return true;
        } catch (Exception e) {
            throw new JwtNotValidException("JWT token validation failed: " + e.getMessage());
        }
    }

        private void saveUserInSecurityContextHolder(Jws<Claims> claimsJws){
        // Extract user details from the JWT claims
        String username = claimsJws.getBody().getSubject();
        List<Map<String, String>> roleMaps = claimsJws.getBody().get("roles", List.class);
        List<String> roles = roleMaps.stream()
                .map(roleMap -> roleMap.get("authority"))
                .toList();

        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Create authentication object
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                authorities
        );

        // Set the authentication object to SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Security context holder updated");
    }


    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
