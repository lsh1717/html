package com.example.hotelres.auth;

import com.example.hotelres.auth.dto.SignupRequest;
import com.example.hotelres.user.User;
import com.example.hotelres.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signup(SignupRequest req) {
        if (users.existsByLoginId(req.getLoginId())) throw new IllegalArgumentException("DUP_LOGIN_ID");
        if (users.existsByEmail(req.getEmail()))     throw new IllegalArgumentException("DUP_EMAIL");

        User u = new User();
        u.setLoginId(req.getLoginId());
        u.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        u.setName(req.getName());
        u.setEmail(req.getEmail());
        u.setPhone(req.getPhone());
     

        u.setGender(req.getGender());
        u.setBirthDate(req.getBirthDate());
        return users.save(u);
    }
}
