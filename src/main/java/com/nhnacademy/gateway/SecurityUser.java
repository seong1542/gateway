package com.nhnacademy.gateway;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@ToString
public class SecurityUser implements UserDetails, OAuth2User {
    private User user;
    public SecurityUser(User user){
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    /**
     * UserDetails 구현
     *
     * @return 해당 유저의 권한목록 리턴
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    /**
     * UserDetails 구현
     *
     * @return 비밀번호 리턴
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     *
     * @return UserId 반환
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    /**
     * 계정 비밀번호 만료 여부
     *
     * @return true : 만료안됨.
     */

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠김 여부
     *
     * @return true: 잠기지 않음
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 계정 비밀번호 만료 여부
     * @return true : 만료 안됨
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 여부
     * @return true : 활성화됨.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
