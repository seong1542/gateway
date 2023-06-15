package com.nhnacademy.gateway;

import com.nhnacademy.gateway.response.UserResponse;
import com.nhnacademy.gateway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OauthUserDetailService extends DefaultOAuth2UserService {
    private final UserService userService;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User auth2User = super.loadUser(userRequest);
        String email = auth2User.getAttribute("email");

        if(Objects.isNull(email)){
            throw new OAuth2AuthenticationException("private을 해제해주세요.");
        }
        UserResponse response = userService.getUserByEmail(email).orElseThrow(
                () -> new OAuth2AuthenticationException("해당하는 회원이 없습니다. 회원가입부터 해주세요.")
        );
        if(response.getStatus().equals("CANCELED")){
            throw new OAuth2AuthenticationException("해당회원은 탈퇴 회원입니다. 다시 가입하세요.");
        }
        else if(response.getStatus().equals("DORMANT")){
            User user = new User(response.getUserId(),
                    response.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_DORMANT")));
            return new SecurityUser(user);
        }else{
            User user = new User(response.getUserId(),
                    response.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_JOINED")));
            return new SecurityUser(user);
        }
    }



}
