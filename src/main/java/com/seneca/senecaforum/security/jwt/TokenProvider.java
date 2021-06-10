package com.seneca.senecaforum.security.jwt;

import com.seneca.senecaforum.repository.RoleRepository;
import com.seneca.senecaforum.repository.UserRepository;
import com.seneca.senecaforum.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class TokenProvider {
    @Autowired
    UserService userService;


    @Value("${secretKey}")
    public String JWT_SECRET;

    public static final int JWT_EXPIRATION_REMEMBER = 1209600000; //14 days
    public static final int JWT_EXPIRATION_WITHOUT_REMEMBER = 300000; //5 mins
    private static final String AUTHORITIES_KEY = "role";

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims,T>claimsResolver){
        // extract all claims
        Claims claims = extractAllClaims(token);
        // extract a single claim from claims
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
    }

    public boolean isValidated(String authToken) {
        // true if extracted expiry Date is after today
        return extractExpiration(authToken).after(new Date());
    }

    public String createToken(Authentication authentication,boolean rememberMe) {
        Date validity;
        if(rememberMe){
            validity = new Date(System.currentTimeMillis()+JWT_EXPIRATION_REMEMBER);
        }
        else{
            validity = new Date(System.currentTimeMillis()+JWT_EXPIRATION_WITHOUT_REMEMBER);
        }
        return  Jwts.builder().setSubject(authentication.getName())
                .claim("userId",userService.getUserByEmail(authentication.getName()).getUserId())
                .claim(AUTHORITIES_KEY,authentication.getAuthorities())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512,JWT_SECRET)
                .compact();
    }

    public Authentication getAuthentication(String token){
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(extractAllClaims(token).get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        User principal = new User(extractUsername(token),"",authorities);
        return new UsernamePasswordAuthenticationToken(principal,token,authorities);
    }

    public String resolveToken(HttpServletRequest request){// decrypt token
        String bearerToken = request.getHeader("Authorization");
        if(!Objects.isNull(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
