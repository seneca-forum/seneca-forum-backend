package com.seneca.senecaforum.security.jwt;

import com.seneca.senecaforum.domain.Role;
import com.seneca.senecaforum.repository.RoleRepository;
import com.seneca.senecaforum.repository.UserRepository;
import com.seneca.senecaforum.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenProvider {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public static final String JWT_SECRET="fPJZz!YkEmUhdmvvhw7y*6K*0$7vco!pfiOtq6N&cgnLNT$I4u";
    public static final int JWT_EXPIRATION_REMEMBER = 1209600000; //14 days
    public static final int JWT_EXPIRATION_WITHOUT_REMEMBER = 300000; //5 mins
    private static final String AUTHORITIES_KEY = "role";


    public boolean isValidated(String authToken) {
        // true if extracted expiry Date is after today
        boolean isTokenValid = extractExpiration(authToken).after(new Date());
        return isTokenValid;
    }


    public String createToken(Authentication authentication,boolean rememberMe) {
        Date validity;
        if(rememberMe){
            validity = new Date(System.currentTimeMillis()+JWT_EXPIRATION_REMEMBER);
        }
        else{
            validity = new Date(System.currentTimeMillis()+JWT_EXPIRATION_WITHOUT_REMEMBER);
        }
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        String authorities = roleRepository.findRoleNameByEmail(user.getUsername()).getRoleName();
        String username = user.getUser().getUsername();
        return  Jwts.builder().setSubject(username)
                .claim(AUTHORITIES_KEY,authorities)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512,JWT_SECRET)
                .compact();
    }

    public Date extractExpiration(String token) {
        Date expiredDate = extractClaim(token, Claims::getExpiration);
        return expiredDate;
    }
    public String extractUserId(String token) {
        String userId = extractClaim(token, Claims::getSubject);
        return userId;
    }

    public <T> T extractClaim(String token, Function<Claims,T>claimsResolver){
        // extract all claims
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        // extract a single claim from claims
        T claimExtracted = claimsResolver.apply( claims);
        return claimExtracted;
    }

}