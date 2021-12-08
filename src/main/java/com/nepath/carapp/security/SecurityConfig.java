package com.nepath.carapp.security;

import com.nepath.carapp.security.extensions.JWTExtensions;
import com.nepath.carapp.security.filters.*;
import com.nepath.carapp.security.properties.SecurityRoles;
import com.nepath.carapp.services.SecurityUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        securedEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.profiles.active}")
    String buildType;

    private final SecurityUserDetailsService securityService;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final NepathAuthenticationSuccessHandler successHandler;
    private final NepathAuthenticationFailureHandler failureHandler;
    private final JWTExtensions jwtExtensions;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        if(buildType.equals("dev")) {
            configureDev(http);
        } else {
            configureProd(http);
        }
    }

    private void configureDev(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(nepathAuthenticationFilter())
                .addFilter(new NepathAuthorizationFilter(userDetailsService(), authenticationManager(), jwtExtensions, buildType.equals("dev")))
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                // configure swagger
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()

                //configure end-points permission
                .antMatchers("/admin/**").hasAnyAuthority(SecurityRoles.ADMIN)
                .antMatchers("/authorization/register").permitAll()
                .antMatchers("/authorization/refreshToken").permitAll()
                .anyRequest().hasAnyAuthority(SecurityRoles.ADMIN, SecurityRoles.USER)

                //configure login
                .and()
                .formLogin().loginProcessingUrl("/authorization/login");

                // configure rest

    }

    private void configureProd(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.authorizeRequests()
                //configure end-points permission
                .antMatchers("/admin/**").hasAnyAuthority(SecurityRoles.ADMIN)
                .antMatchers("/authorization/register").permitAll()
                .antMatchers("/authorization/refreshToken").permitAll()
                .anyRequest().hasAnyAuthority(SecurityRoles.ADMIN, SecurityRoles.USER)

                //configure login
                .and()
                .formLogin().loginProcessingUrl("/authorization/login")
                .and()

                // configure rest
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterAt(new NepathApiKeyFilter(jwtExtensions), nepathAuthenticationFilter().getClass())
                .addFilter(nepathAuthenticationFilter())
                .addFilter(new NepathAuthorizationFilter(userDetailsService(), authenticationManager(), jwtExtensions, buildType.equals("dev")))
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .headers().frameOptions().disable()
                .and();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(1500000L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public NepathAuthenticationFilter nepathAuthenticationFilter() throws Exception {
        NepathAuthenticationFilter nepathAuthenticationFilter = new NepathAuthenticationFilter();
        nepathAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        nepathAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);
        nepathAuthenticationFilter.setAuthenticationManager(super.authenticationManager());
        nepathAuthenticationFilter.setFilterProcessesUrl("/authorization/login");
        return nepathAuthenticationFilter;
    }
}