package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/join")
    public HttpStatus join(@RequestBody Map<String, String> user) {
        userRepository.save(User.builder()
                .email(user.get("email"))
                .username(user.get("username"))
                .password(passwordEncoder.encode(user.get("password")))
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build());
         return HttpStatus.OK;
    }

    // 로그인
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> user) {
        Map<String, Object> result = new HashMap<>();
        User member = userRepository.findByUserName(user.get("username"));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            result.put("msg", "fail");
            return result;
        }
        result.put("data",jwtTokenProvider.createToken(member.getUsername(), member.getRoles()));
        result.put("msg", "success");
        return result;
    }

}
