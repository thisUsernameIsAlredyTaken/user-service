package com.example.userservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/authenticated").authenticated()
                .antMatchers("/me/**", "/me").hasRole("USER")
//                .antMatchers()
                .antMatchers("/user/**", "/user").not().hasRole("USER")
                .anyRequest().hasRole("ADMIN")
//                .antMatchers(HttpMethod.POST, "/register/user").permitAll()
//                .antMatchers(HttpMethod.GET, "/authenticated-info").authenticated()
//                .antMatchers(HttpMethod.POST, "/me**").authenticated()
//                .antMatchers(HttpMethod.POST).hasRole("ADMIN")
//                .antMatchers(HttpMethod.POST).permitAll()
//                .antMatchers(HttpMethod.GET).permitAll()
        ;
    }
}
