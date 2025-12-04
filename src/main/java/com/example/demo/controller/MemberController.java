package com.example.demo.controller;

import com.example.demo.model.domain.Member;
import com.example.demo.model.service.AddMemberRequest;
import com.example.demo.model.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    // 회원 가입 페이지
    @GetMapping("/join_new")
    public String join_new(Model model) {
        model.addAttribute("request", new AddMemberRequest());
        return "join_new";
    }

    // 회원 가입 저장
    @PostMapping("/api/members")
    public String addmembers(@ModelAttribute("request") @Valid AddMemberRequest request,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            return "join_new";
        }

        memberService.saveMember(request);
        return "join_end";
    }

    // 로그인 페이지
    @GetMapping("/member_login")
    public String member_login() {
        return "login";
    }

    // 로그아웃
    @GetMapping("/api/logout")
    public String member_logout(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/member_login";
    }
    @GetMapping("/session-expired")
            public String sessionExpired() {
                return "session-expired";
    }

    // 로그인 체크
    @PostMapping("/api/login_check")
    public String checkMembers(@ModelAttribute AddMemberRequest request,
                               Model model,
                               HttpServletRequest httpRequest,
                               HttpServletResponse response) {
        try {
            // 기존 세션 제거
            HttpSession oldSession = httpRequest.getSession(false);
            if (oldSession != null) oldSession.invalidate();

            // 쿠키 삭제
            Cookie cookie = new Cookie("JSESSIONID", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            // 새 세션
            HttpSession session = httpRequest.getSession(true);

            // 로그인 검증
            Member member = memberService.loginCheck(request.getEmail(), request.getPassword());

            // ✅ 세션에 "저장"만 한다
            session.setAttribute("userId", member.getId());      // Long 타입
            session.setAttribute("email", member.getEmail());    // String 타입

            return "redirect:/board_list";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }
}
