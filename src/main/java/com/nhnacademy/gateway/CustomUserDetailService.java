package com.nhnacademy.gateway;

import com.nhnacademy.gateway.domain.user.StateName;
import com.nhnacademy.gateway.response.UserResponse;
import com.nhnacademy.gateway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserResponse userResponse = userService.getUser(username).orElseThrow(
                () -> new UsernameNotFoundException(username+" not found")
        );

        if(userResponse.getStatus().equals("DORMANT")){
            User user = new User(userResponse.getUserId(), userResponse.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_DORMANT")));
            return new SecurityUser(user);
        }
        else if (userResponse.getStatus().equals("CANCELED")){

            throw new UsernameNotFoundException(username+" not found");
        }
        else {
            User user = new User(userResponse.getUserId(), userResponse.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_JOINED")));
            return new SecurityUser(user);
        }
    }
}
