package com.example.hotelres.auth;

import com.example.hotelres.auth.dto.*;
import com.example.hotelres.security.JwtUtil;
import com.example.hotelres.user.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserRepository users;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwt;

    // ★ 추가: 이메일 코드 저장/검증 컴포넌트
    private final EmailCodeStore emailCodeStore;

    @GetMapping("/check-username")
    public Map<String, Boolean> checkUsername(@RequestParam String loginId) {
        return Map.of("available", !users.existsByLoginId(loginId));
    }

    @GetMapping("/check-email")
    public Map<String, Boolean> checkEmail(@RequestParam String email) {
        return Map.of("available", !users.existsByEmail(email));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody SignupRequest req,
            @RequestParam(value = "verificationCode", required = false) String verificationCode,
            @RequestHeader(value = "X-Verification-Code", required = false) String verificationHeader) {

        String code = (verificationCode != null && !verificationCode.isBlank())
                ? verificationCode : verificationHeader;

        if (code == null || !emailCodeStore.consume(req.getEmail(), code)) { // ✅ 최종 소모
            return ResponseEntity.badRequest().body(Map.of("error", "INVALID_OR_EXPIRED_CODE"));
        }

        authService.signup(req);
        return ResponseEntity.ok(Map.of("success", true));
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getLoginId(), req.getPassword()));
            var u = users.findByLoginId(req.getLoginId()).orElseThrow();
            String access  = jwt.generateAccess(u.getLoginId(), u.getRole().name());
            String refresh = jwt.generateRefresh(u.getLoginId());

            var cookie = ResponseCookie.from("refreshToken", refresh)
                    .httpOnly(true).secure(false)      // dev: false, prod: true (HTTPS)
                    .sameSite("Lax")
                    .path("/api/auth")                 // refresh/logout 경로에만 전송
                    .maxAge(jwt.getRefreshExpMs()/1000)
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(Map.of("token", access));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "INVALID_CREDENTIALS"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name="refreshToken", required=false) String rt) {
        if (rt == null) return ResponseEntity.status(401).body(Map.of("error","NO_REFRESH"));
        try {
            var claims = jwt.parse(rt).getBody();
            if (!"refresh".equals(claims.get("type"))) return ResponseEntity.status(401).build();

            var loginId = claims.getSubject();
            var u = users.findByLoginId(loginId).orElseThrow();

            String newAccess  = jwt.generateAccess(u.getLoginId(), u.getRole().name());
            String newRefresh = jwt.generateRefresh(u.getLoginId());

            var cookie = ResponseCookie.from("refreshToken", newRefresh)
                    .httpOnly(true).secure(false)  // prod: true
                    .sameSite("Lax")
                    .path("/api/auth")
                    .maxAge(jwt.getRefreshExpMs()/1000)
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(Map.of("token", newAccess));
        } catch (JwtException e) {
            return ResponseEntity.status(401).body(Map.of("error","INVALID_REFRESH"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        var cookie = ResponseCookie.from("refreshToken","")
                .httpOnly(true).secure(false)   // prod: true
                .sameSite("Lax")
                .path("/api/auth")
                .maxAge(0)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("success", true));
    }
}
