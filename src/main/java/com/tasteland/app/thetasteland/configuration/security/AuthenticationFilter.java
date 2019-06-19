package com.tasteland.app.thetasteland.configuration.security;


import com.tasteland.app.thetasteland.service.AuthenticationService;
import com.tasteland.app.thetasteland.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationFilter(JwtUtils jwtUtils, AuthenticationService authenticationService) {
        this.jwtUtils = jwtUtils;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String username = null;
        String token = null;
        String header = request.getHeader(jwtUtils.getTokenHeader());
        if (jwtUtils.containsToken(header)) {
            token = header.replace(jwtUtils.getJwtPrefix(), "");
            username = getUsername(token);
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user = null;
            try {
                user = authenticationService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            }
            if (jwtUtils.isValidToken(token, user)) {
                authenticate(request, user);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getUsername(String token) {
        return jwtUtils.getUsernameFromToken(token);
    }

    private void authenticate(HttpServletRequest req, UserDetails user) {
        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
