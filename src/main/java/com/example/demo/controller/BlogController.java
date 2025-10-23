// package com.example.demo.controller;

// import com.example.demo.model.service.BlogService;
// import com.example.demo.model.service.AddArticleRequest;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// //import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;

// @RequiredArgsConstructor
// @Controller
// public class BlogController {

//     private final BlogService blogService;

    
//     @GetMapping({ "/article_list"})
//     public String list(Model model) {
//         model.addAttribute("articles", blogService.findAll());
//         return "article_list";  // templates/article_list.html 렌더링
//     }

  
//     @PostMapping("/article_write")
//     public String write(AddArticleRequest request) {
//         blogService.save(request);      // 게시글 저장
//         return "redirect:/article_list"; // 저장 후 목록으로 자동 이동
//     }

// }
// BlogController.java
package com.example.demo.controller;

import com.example.demo.model.service.BlogService;
import com.example.demo.model.service.AddArticleRequest;
//import com.example.demo.model.domain.Article; // 필요시
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class BlogController {

    private final BlogService blogService;

    // 목록
    @GetMapping("/article_list")
    public String list(Model model) {
        model.addAttribute("articles", blogService.findAll());
        return "article_list";  // templates/article_list.html
    }

    // ✅ 수정 폼 열기 (이게 없어서 404 났던 것)
    @GetMapping("/article_edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        var opt = blogService.findById(id);
        if (opt.isEmpty()) {
            return "error"; // 없는 글이면 에러 페이지로
        }
        model.addAttribute("article", opt.get());
        return "article_edit";  // templates/article_edit.html 로 렌더
    }

    // 수정 저장 (B안: 액션을 /article_update 로 고정)
    @PostMapping("/article_update")
    public String update(@RequestParam Long id, AddArticleRequest req) {
        blogService.update(id, req);
        return "redirect:/article_list";
    }

    // 글쓰기 저장
    @PostMapping("/article_write")
    public String write(AddArticleRequest req) {
        blogService.save(req);
        return "redirect:/article_list";
    }

    // 삭제
    @PostMapping("/article_delete/{id}")
    public String delete(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/article_list";
    }
}
