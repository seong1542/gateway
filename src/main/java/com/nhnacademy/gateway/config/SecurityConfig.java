package com.nhnacademy.gateway.config;

import com.nhnacademy.gateway.CustomUserDetailService;
import com.nhnacademy.gateway.OauthUserDetailService;
import com.nhnacademy.gateway.handler.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity(debug = true)
@Configuration
public class SecurityConfig {

    private final RedisTemplate<String, String> redisTemplate;
    private final OauthUserDetailService oauthUserDetailService;

    public SecurityConfig(@Lazy RedisTemplate redisTemplate, @Lazy OauthUserDetailService oauthUserDetailService){
        this.redisTemplate = redisTemplate;
        this.oauthUserDetailService = oauthUserDetailService;
    }
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
                .antMatchers("/projects/**").hasAuthority("ROLE_JOINED")
                .antMatchers("/users/change/**").hasAuthority("ROLE_DORMANT")
                .antMatchers("/logout").authenticated()
                .antMatchers("/", "/users/login", "/users/join").permitAll()
                .anyRequest().permitAll();
//                    .antMatchers("/projects/*", "/users/logout").hasAuthority("ROLE_JOINED")
//                    .antMatchers("/users/change/state/*").hasAuthority("ROLE_DORMANT")
//                    .antMatchers("/", "/users/login", "/users/join").permitAll()
//                    .anyRequest().hasAuthority("ROLE_JOINED");

        httpSecurity
                .formLogin()
                .successHandler(loginSuccessHandler(redisTemplate));
        httpSecurity.oauth2Login()
                .successHandler(loginSuccessHandler(redisTemplate))
                .userInfoEndpoint()
                .userService(oauthUserDetailService);
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
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(ClientRegistration.withClientRegistration(github()).build());
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }
    private ClientRegistration github(){
        return CommonOAuth2Provider.GITHUB.getBuilder("github")
                .userNameAttributeName("email")
                .clientId("c3bdf4e1cdbe666f7c11")
                .clientSecret("74b9e0054b91215bda590862a4bff1b8d923b0a2")
                .build();
    }
}
