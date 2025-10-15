package com.example.demo.controller;

import com.example.demo.model.domain.Article;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController // @Controller + @ResponseBody
public class BlogRestController {

    private final BlogService blogService;

    @PostMapping("/api/articles") // POST 요청
    public ResponseEntity<Article> addArticle(@ModelAttribute AddArticleRequest request) {
        Article savedArticle = blogService.save(request); // 게시글 저장
        return ResponseEntity
                .status(HttpStatus.CREATED) 
                .body(savedArticle);        
    
    }
}
