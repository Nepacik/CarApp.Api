package com.nepath.carapp.security;

import com.nepath.carapp.security.filters.NepathAuthenticationFailureHandler;
import com.nepath.carapp.security.filters.NepathAuthenticationFilter;
import com.nepath.carapp.security.filters.NepathAuthenticationSuccessHandler;
import com.nepath.carapp.security.filters.NepathAuthorizationFilter;
import com.nepath.carapp.security.properties.SecurityRoles;
import lombok.RequiredArgsConstructor;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        securedEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService securityService;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final NepathAuthenticationSuccessHandler successHandler;
    private final NepathAuthenticationFailureHandler failureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
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
                .formLogin().loginProcessingUrl("/authorization/login")
                .and()

                // configure rest
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(nepathAuthenticationFilter())
                .addFilter(new NepathAuthorizationFilter(userDetailsService(), authenticationManager()))
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .headers().frameOptions().disable()
                .and();
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