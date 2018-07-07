package com.example.demo.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.Base64Utils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT authentication filter.
 * Retrieves JWT token from request header, parses it and sets security context
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            log.debug("Authorization token does not exists or has wrong format.");
            SecurityContextHolder.clearContext();
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    /**
     * Retrieves information about the user from JWT token
     *
     * @param request --- contains Authorization header
     * @return UsernamePasswordAuthenticationToken with user info and details
     * @throws IOException
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws IOException {
        String token = request.getHeader(HEADER_STRING).replace(TOKEN_PREFIX, "");
        log.debug("Token {}", token);
        String json = new String(Base64Utils.decodeFromString(token.split("\\.")[1]));
        JwtAuthorizationInfo info = new ObjectMapper().readValue(json, JwtAuthorizationInfo.class);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(info.getName(), null, new ArrayList<>());
        authentication.setDetails(new UserDetails("like", "summer"));

        return authentication;
    }
}
