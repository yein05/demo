package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 컨트롤러 어노테이션 명시
public class DemoController{

    @GetMapping("/")            // ✅ 추가
    public String index() {
        return "index";  
    }
@GetMapping("/hello") // 전송 방식 GET
public String hello(Model model){
model.addAttribute("data", " 반갑습니다."); // model 설정
return "hello"; // hello.html 연결
}

@GetMapping("/hello2")
    public String hello2(Model model) {
        model.addAttribute("name", "정예인님");
        model.addAttribute("greeting", "반갑습니다.");
        model.addAttribute("today", "오늘");
        model.addAttribute("weather", "날씨는");
        model.addAttribute("status", "매우 좋습니다.");
        return "hello2"; // hello2.html
    }
}
