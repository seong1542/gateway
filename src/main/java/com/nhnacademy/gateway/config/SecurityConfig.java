package com.nhnacademy.gateway.config;

import com.nhnacademy.gateway.CustomUserDetailService;
import com.nhnacademy.gateway.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity(debug = true)
@Configuration
public class SecurityConfig {

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .headers().defaultsDisabled()
                    .contentTypeOptions() // ContentType만 보고 결정하는 경우가 있는데, sniffing발생을 막고자 사용
                .and()
                .frameOptions().sameOrigin()
                .xssProtection().and()
                .and()
                .csrf().disable();
        httpSecurity
                .authorizeRequests()
                .antMatchers("/projects","/users/change/state/*").permitAll()
                .antMatchers("/", "/users/login", "/users/join").permitAll()
                .anyRequest().permitAll();
//                    .antMatchers("/projects/*", "/users/logout").hasAuthority("ROLE_JOINED")
//                    .antMatchers("/users/change/state/*").hasAuthority("ROLE_DORMANT")
//                    .antMatchers("/", "/users/login", "/users/join").permitAll()
//                    .anyRequest().hasAuthority("ROLE_JOINED");
        httpSecurity
                .formLogin()
                .successHandler(loginSuccessHandler(redisTemplate));
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(CustomUserDetailService customUserDetailService,
                                                         PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler(RedisTemplate<String, String> redisTemplate){
        return new LoginSuccessHandler(redisTemplate);
    }
}
