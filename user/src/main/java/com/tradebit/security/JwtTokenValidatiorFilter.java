//package com.tradebit.security;
//
//import com.google.common.net.HttpHeaders;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.security.Key;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class JwtTokenValidatiorFilter extends OncePerRequestFilter {
//    @Value("${jwt.secret}")
//    private String SECRET_KEY;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String jwtToken = authHeader.substring(7);
//            try {
//                if (validateToken(jwtToken)) {
//
//                }
//            } catch (Exception e) {
//                // Log and handle error
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private boolean validateToken(String jwtToken) {
//        try {
//            Jws<Claims> claimsJws = Jwts.parserBuilder()
//                    .setSigningKey(getSigningKey())
//                    .build()
//                    .parseClaimsJws(jwtToken);
//
//            return true;
//        } catch (Exception e) {
//            throw new RuntimeException("JWT token validation failed: " + e.getMessage());
//        }
//    }
//
//    private Key getSigningKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    private void saveUserInSecurityContextHolder(Jws<Claims> claimsJws){
//        // Extract user details from the JWT claims
//        String username = claimsJws.getBody().getSubject();
//        List<String> roles = claimsJws.getBody().get("roles", List.class); // Assuming roles are stored as a list in your JWT payload
//
//        // Convert roles to GrantedAuthority list
//        List<GrantedAuthority> authorities = roles.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//
//        // Create authentication object
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                username,
//                null,
//                authorities
//        );
//
//        // Set the authentication object to SecurityContextHolder
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//    }
//}
