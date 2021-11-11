package com.nepath.carapp.security;

import com.nepath.carapp.security.filters.CustomAuthenticationFilter;
import com.nepath.carapp.security.filters.CustomAuthorizationFilter;
import com.nepath.carapp.security.properties.JWTProperties;
import com.nepath.carapp.security.properties.SecurityRoles;
import com.nepath.carapp.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityService securityService;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/admin/**").hasAnyAuthority(SecurityRoles.ADMIN);
        http.authorizeRequests().antMatchers("/car/**").authenticated();
        http.authorizeRequests().antMatchers("/authorization/delete").authenticated();
        http.authorizeRequests().antMatchers("/js/**", "/css/**").permitAll();
        http.authorizeRequests().antMatchers(JWTProperties.LOGIN_PATH).permitAll();
        http.authorizeRequests().antMatchers(JWTProperties.REGISTRATION_PATH).permitAll();


        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean(), securityService));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        configureSwagger(web);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private void configureSwagger(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/swagger-ui/",
                "/swagger-ui",
                "/swagger-ui/**",
                "/webjars/**");
    }
}
