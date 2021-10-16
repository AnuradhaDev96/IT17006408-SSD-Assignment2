package com.billboard.IT17006408_oAuth.config;

import com.billboard.IT17006408_oAuth.service.BillboardUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    //@Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService billboardUserDetailService;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/index.html", "/createFolder/{folderName}")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.parentAuthenticationManager(authenticationManager)
                .userDetailsService(billboardUserDetailService);
    }
}

//httpSecurity
//        .antMatcher("/**").authorizeRequests()
//        .antMatchers("").permitAll()
//        .anyRequest().authenticated()
//        .and()
//        .oauth2Login();

//httpSecurity
//        .csrf()
//        .disable()
//        .antMatcher("/**")
//        .authorizeRequests()
//        .antMatchers("/", "/index.html")
//        .permitAll()
//        .anyRequest()
//        .authenticated();

//httpSecurity.antMatcher("/**").authorizeRequests()
//        .antMatchers("/", "/login", "/logout").permitAll()
//        .anyRequest().authenticated()
//        .and()
//        .formLogin().loginPage("/login")
//        .permitAll()
//        .and()
//        .logout()
//        .permitAll()
//        .and()
//        .oauth2Login();