package com.tasteland.app.thetasteland.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:security-auth.properties")
public class TastelandSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment environment;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(getIntProp("bcrypt.strength").get());
    }

    private Optional<Integer> getIntProp(String property) {
        try {
            String prop = environment.getProperty(property);
            return Optional.of(Integer.parseInt(prop));
        } catch (NumberFormatException nfe) {
            return Optional.empty();
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
    }


}
