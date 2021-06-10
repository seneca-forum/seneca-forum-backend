package com.seneca.senecaforum.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private final TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = tokenProvider.resolveToken(request);
        try{
            //check if jwtToken is valid
            if(Objects.nonNull(jwtToken) && tokenProvider.isValidated(jwtToken)) {
                // authentication
                Authentication authentication = tokenProvider.getAuthentication(jwtToken);
                // if user is valid, set it to the Security Context
                if (Objects.nonNull(authentication)) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch(Exception e){
            log.error("failed on set user authentication", e);
        }

    }

}
