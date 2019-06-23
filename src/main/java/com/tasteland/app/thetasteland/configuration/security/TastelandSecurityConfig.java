package com.tasteland.app.thetasteland.configuration.security;

import com.tasteland.app.thetasteland.service.AuthenticationService;
import com.tasteland.app.thetasteland.utils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@PropertySource("classpath:security-auth.properties")
public class TastelandSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PropertyUtils prop;
    private final AuthenticationService authenticationService;
    private final AuthenticationEntryPoint entryPoint;
    private final AuthenticationFilter filter;

    @Autowired
    public TastelandSecurityConfig(PropertyUtils prop, AuthenticationService authenticationService,
                                   AuthenticationEntryPoint entryPoint, AuthenticationFilter filter) {
        this.prop = prop;
        this.authenticationService = authenticationService;
        this.entryPoint = entryPoint;
        this.filter = filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        Integer strength = prop.getInt("bcrypt.strength");
        return new BCryptPasswordEncoder(strength);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.POST, "/auth");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(entryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().authenticated();
        http
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
