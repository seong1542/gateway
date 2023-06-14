package com.nhnacademy.gateway.handler;

import com.nhnacademy.gateway.SecurityUser;
import com.nhnacademy.gateway.domain.user.StateName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final RedisTemplate<String, String> redisTemplate;
    private static final int DAY = 60*60*24;
    public LoginSuccessHandler(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
//        super.onAuthenticationSuccess(request, response, authentication);
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        List<String> roleNames = new ArrayList<>();



        securityUser.getAuthorities().forEach(grantedAuthority -> {
            roleNames.add(grantedAuthority.getAuthority());
        });
        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(3*DAY);

        redisTemplate.opsForHash().put(session.getId(), "username", securityUser.getUsername());
        redisTemplate.opsForHash().put(session.getId(), "authority"+securityUser.getUsername(), roleNames.get(0));
        redisTemplate.boundHashOps(session.getId()).expire(Duration.ofDays(3L*DAY));

        log.error(roleNames.get(0));
        session.setAttribute("username", securityUser.getUsername());
        session.setAttribute("authority", roleNames.get(0));

        if(roleNames.contains("ROLE_DORMANT")){
            response.sendRedirect("/users/change/state/"+securityUser.getUsername());
            return;
        }
        response.sendRedirect("/projects");
    }
}
