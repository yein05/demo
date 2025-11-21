package com.example.demo.controller;

import com.example.demo.model.domain.Member;
import com.example.demo.model.service.AddMemberRequest;
import com.example.demo.model.service.MemberService;
import jakarta.validation.Valid;//
import org.springframework.validation.BindingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;//
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class MemberController {

    // @Autowired 대신 생성자 주입 (@RequiredArgsConstructor)
    private final MemberService memberService;

    // 회원 가입 페이지
    @GetMapping("/join_new")
    public String join_new(Model model) {
        model.addAttribute("request", new AddMemberRequest());
        return "join_new"; // templates/join_new.html
    }

    // 회원 가입 저장
    @PostMapping("/api/members")
    public String addmembers(@ModelAttribute("request") @Valid AddMemberRequest request,
                             BindingResult bindingResult,
                             Model model) {

        // 검증 실패 시 다시 가입 페이지로
        if (bindingResult.hasErrors()) {
            return "join_new";
        }

        memberService.saveMember(request);
        return "join_end"; // 가입 완료 페이지 (또는 redirect:/member_login)
    }

    // 로그인 페이지
    @GetMapping("/member_login")
    public String member_login() {
        return "login"; // templates/login.html
    }

    // 로그인 체크
    @PostMapping("/api/login_check")
    public String checkMembers(@ModelAttribute AddMemberRequest request, Model model) {
        try {
            Member member = memberService.loginCheck(request.getEmail(), request.getPassword());
            model.addAttribute("member", member); // 로그인 성공 시 회원 정보 전달
            return "redirect:/board_list";        // 로그인 성공 후 이동
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); // 에러 메시지 전달
            return "login"; // 로그인 실패 시 다시 로그인 페이지
        }
    }
}

