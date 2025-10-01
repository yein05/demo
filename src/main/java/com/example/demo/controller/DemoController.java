package com.example.demo.controller;

import com.example.demo.model.domain.TestDB;
import com.example.demo.model.service.TestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 컨트롤러 어노테이션 명시
public class DemoController{

    @GetMapping("/")            //  추가
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
        return "hello2";}// hello2.html

        @GetMapping("/about_detailed")
public String about() {
return "about_detailed";
}

@GetMapping("/test1")
public String thymeleaf_test1(Model model) {
model.addAttribute("data1", "<h2> 방갑습니다 </h2>");
model.addAttribute("data2", "태그의 속성 값");
model.addAttribute("link", 01);
model.addAttribute("name", "홍길동");
model.addAttribute("para1", "001");
model.addAttribute("para2", 002);
return "thymeleaf_test1";
}
// 클래스 하단 작성
@Autowired
TestService testService; // DemoController 클래스 아래 객체 생성
// 하단에 맵핑 이어서 추가
@GetMapping("/testdb")
public String getAllTestDBs(Model model) {
TestDB test = testService.findByName("홍길동");
model.addAttribute("data4", test);
System.out.println("데이터 출력 디버그 : " + test);
return "testdb";
}

    
}
