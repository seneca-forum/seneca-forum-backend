package com.seneca.senecaforum.security.jwt;

import com.seneca.senecaforum.domain.UserEntity;
import com.seneca.senecaforum.repository.RoleRepository;
import com.seneca.senecaforum.repository.UserRepository;
import com.seneca.senecaforum.service.UserService;
import com.seneca.senecaforum.service.dto.UserDto;
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
    private String JWT_SECRET;

    public static final int JWT_EXPIRATION_REMEMBER = 1209600000; //14 days
    public static final int JWT_EXPIRATION_WITHOUT_REMEMBER = 300000; //5 mins
    private static final String AUTHORITIES_KEY = "role";
    private static final String USERNAME = "username";
    private static final String USER_ID = "userId";

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public String extractUsername(String token) {
        return extractAllClaims(token).get(USERNAME).toString();
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
//        authentication.getAuthorities().stream().findFirst().get()
        UserDto userDto = userService.getUserByEmail(authentication.getName());
        return  Jwts.builder()
                .claim(USERNAME,userDto.getUsername())
                .claim(USER_ID,userDto.getUserId())
                .claim(AUTHORITIES_KEY,authentication.getAuthorities().stream().collect(Collectors.toList()).get(0).getAuthority()
                )
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512,JWT_SECRET)
                .compact();
    }

    public Authentication getAuthentication(String token){
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{extractAllClaims(token).get(AUTHORITIES_KEY).toString()})
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
