package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import service.MemberService;
import vo.Member;

@Controller
@RequestMapping("login")  
public class MemberController {

    @Autowired
    private MemberService service;

    // 회원가입 폼
    @GetMapping("register")
    public String registerForm() {
        return "login/register";  // 바로 회원가입 JSP 경로
    }

    // 회원가입 처리
    @PostMapping("register")
    public String register(@ModelAttribute Member member, RedirectAttributes redirectAttributes) {
        boolean registered = service.registerMember(member);
        String result = registered
                ? "회원가입이 완료되었습니다. 로그인 후 이용해주세요."
                : "이미 존재하는 아이디입니다. 다시 시도해주세요.";
        redirectAttributes.addFlashAttribute("result", result);
        return "redirect:/" + (registered ? "login/login" : "member/register");
    }
}