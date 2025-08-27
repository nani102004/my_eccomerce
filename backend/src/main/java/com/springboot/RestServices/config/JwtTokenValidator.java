package com.springboot.RestServices.config;



import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        
        // 🔹 Skip validation for public endpoints
        if (path.startsWith("/auth/signup") || path.startsWith("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = request.getHeader(JwtConstant.JWT_HEADER);
        System.out.println("jwt ------ " + jwt);

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // remove "Bearer "

            try {
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                // 👇 Adapt to your token payload
                String email = String.valueOf(claims.get("email")); // may be null
                String userId = String.valueOf(claims.get("userId"));
                String role = String.valueOf(claims.get("userRole")); // from your JWT log

                // If authorities claim is missing, fall back to role
                String authorities = claims.containsKey("authorities") 
                        ? String.valueOf(claims.get("authorities")) 
                        : "ROLE_" + role;

                List<GrantedAuthority> auths =
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(email != null ? email : userId, null, auths);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                throw new BadCredentialsException("invalid token...");
            }
        }

        filterChain.doFilter(request, response);
    }
}

