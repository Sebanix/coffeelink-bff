package com.coffeelink.bff.security;

import com.coffeelink.bff.model.User;
import com.coffeelink.bff.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends  OncePerRequestFilter{

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = this.userRepository.findByEmail(userEmail)
                    .orElse(null);
            if (user != null && jwtService.isTokenValid(jwt, user)) {

                // ---------- ¡AÑADE ESTAS LÍNEAS AQUÍ! ----------
                System.out.println("--- FILTRO JWT DEBUG ---");
                System.out.println("Usuario encontrado en DB: " + user.getEmail());
                System.out.println("Rol (raw) desde la DB: " + user.getRol());
                System.out.println("--- FIN FILTRO DEBUG ---");
                // ------------------------------------------------

                if (user.getRol() != null) {
                    GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRol().toUpperCase());

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            Collections.singletonList(authority)
                    );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.err.println("¡ERROR DE MAPEO! El ROL del usuario " + user.getEmail() + " es NULL en la base de datos.");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
