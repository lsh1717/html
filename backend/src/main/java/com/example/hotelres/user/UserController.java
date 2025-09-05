package com.example.hotelres.user;

import com.example.hotelres.auth.EmailCodeStore;
import com.example.hotelres.auth.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final EmailService emailService;
    private final EmailCodeStore emailCodeStore;

    /** 이메일 인증 코드 발송: POST /api/auth/email/send  { "email": "..." } */
    @PostMapping("/auth/email/send")
    public ResponseEntity<?> sendVerificationCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        try {
            String code = emailService.generate6Digit();
            emailCodeStore.save(email, code, 300);      // 5분 TTL
            emailService.sendVerificationCode(email, code);
            return ResponseEntity.ok(Map.of("sent", true));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", e.getClass().getSimpleName(), "message", e.getMessage()));
        }
    }

    /** (선택) 코드만 별도로 검증: POST /api/auth/email/verify  { "email":"...","code":"123456" } */
    @PostMapping("/auth/email/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String, String> body) {
        boolean ok = emailCodeStore.check(body.get("email"), body.get("code")); // ✅ 소모 X
        return ResponseEntity.ok(Map.of("verified", ok));
    }
}
