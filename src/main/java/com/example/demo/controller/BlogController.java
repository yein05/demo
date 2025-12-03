package com.example.demo.controller;

import com.example.demo.model.domain.Board;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class BlogController {

    private final BlogService blogService;

    @GetMapping("/board_write")
    public String boardWrite() {
        return "board_write";
    }

    @PostMapping("/api/boards")
    public String addboards(@ModelAttribute AddArticleRequest request) {
        blogService.save(request);
        return "redirect:/board_list";
    }

    @GetMapping("/board_list")
    public String board_list(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String keyword,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        String email = (String) session.getAttribute("email");

        if (userId == null) {
            return "redirect:/member_login";
        }

        int pageSize = 3;
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Board> list;
        if (keyword.isEmpty()) {
            list = blogService.findAll(pageable);
        } else {
            list = blogService.searchByKeyword(keyword, pageable);
        }

        int startNum = page * pageSize + 1;

        model.addAttribute("startNum", startNum);
        model.addAttribute("email", email);

        model.addAttribute("boards", list.getContent());
        model.addAttribute("totalPages", list.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);

        return "board_list";
    }

    @GetMapping("/board_view/{id}")
    public String boardView(Model model, @PathVariable Long id) {
        Optional<Board> board = blogService.findById(id);
        if (board.isPresent()) {
            model.addAttribute("boards", board.get());
            return "board_view";
        } else {
            return "/error_page/article_error";
        }
    }

    @PostMapping("/api/board_delete/{id}")
    public String deleteBoard(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/board_list";
    }

    @GetMapping("/board_edit/{id}")
    public String boardEdit(@PathVariable Long id, Model model) {

        Optional<Board> opt = blogService.findById(id);
        if (opt.isEmpty()) {
            return "/error_page/article_error";
        }

        Board board = opt.get();

        AddArticleRequest request = new AddArticleRequest(
                board.getTitle(),
                board.getContent(),
                board.getUser(),
                board.getNewdate(),
                board.getCount(),
                board.getLikec()
        );

        model.addAttribute("id", id);
        model.addAttribute("article", request);

        return "board_edit";
    }

    @PostMapping("/board_edit/{id}")
    public String updateArticle(@PathVariable Long id,
                                @ModelAttribute("article") AddArticleRequest request) {

        blogService.update(id, request);
        return "redirect:/board_list";
    }

}
