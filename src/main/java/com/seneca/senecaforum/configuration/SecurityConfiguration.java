package com.seneca.senecaforum.configuration;

import com.seneca.senecaforum.security.jwt.JwtConfigurer;
import com.seneca.senecaforum.security.jwt.TokenProvider;
import com.seneca.senecaforum.service.DomainUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String[] USER_ALLOW_URLS = new String[]{
            "/api/posts/user/**"
    };

    @Autowired
    @Qualifier("userDetailsService")
    DomainUserDetailsService domainUserDetailsService;

    @Autowired
    private TokenProvider tokenProvider;

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(domainUserDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and().authorizeRequests()
                .antMatchers("/api/auth").permitAll()
                .antMatchers(HttpMethod.POST,"/api/users/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/posts/**","/api/topics/**").permitAll()
                .antMatchers(USER_ALLOW_URLS).hasRole("USER")
                .antMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().apply(securityConfigureAdapter());

    }

    private JwtConfigurer securityConfigureAdapter(){
        return new JwtConfigurer(tokenProvider);
    }

}
