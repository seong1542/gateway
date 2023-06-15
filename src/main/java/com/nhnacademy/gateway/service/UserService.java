package com.nhnacademy.gateway.service;

import com.nhnacademy.gateway.domain.user.Status;
import com.nhnacademy.gateway.request.UserRegisterRequest;
import com.nhnacademy.gateway.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RestTemplate restTemplate;
    @Value("${account.server.url}")
    private String serverUrl;
    private final PasswordEncoder passwordEncoder;

    private static HttpHeaders httpHeaders = new HttpHeaders();
    static {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
    }

    public void registerUser(UserRegisterRequest request){
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        String requestUrl = serverUrl+"/users/join";
        HttpEntity<UserRegisterRequest> httpEntity = new HttpEntity<>(request, httpHeaders);
        restTemplate.exchange(requestUrl,
                        HttpMethod.POST,
                        httpEntity,
                        new ParameterizedTypeReference<>() {
                        });
    }
    public Optional<UserResponse> getUser(String id){
        String requestUrl = serverUrl+"/users/"+id;
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<UserResponse> response = restTemplate.exchange(requestUrl,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<UserResponse>() {
                }, id);
        return Optional.ofNullable(response.getBody());
    }


    public void updateUserStatus(String id, Status status){
        String requestUrl = serverUrl+"/users/change/state/"+id;

        HttpEntity<Status> httpEntity = new HttpEntity<>(status, httpHeaders);

        restTemplate.exchange(requestUrl,
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }, id);

    }

    public Optional<UserResponse> getUserByEmail(String email){
        String requestUrl = serverUrl+"/users/email/"+email;

        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<UserResponse> response = restTemplate.exchange(requestUrl,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<UserResponse>() {
                }, email);
        return Optional.ofNullable(response.getBody());
    }

}
