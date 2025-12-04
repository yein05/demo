package com.example.demo.model.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.demo.model.domain.Board;
import com.example.demo.model.repository.BoardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogService {

    @Autowired
    private final BoardRepository boardRepository2;

    public Board save(AddArticleRequest request) {
        return boardRepository2.save(request.toEntity());
    }

    public Optional<Board> findById(Long id) {
        return boardRepository2.findById(id);
    }

    public void update(Long id, AddArticleRequest request) {
        Optional<Board> optionalBoard = boardRepository2.findById(id);
        optionalBoard.ifPresent(board -> {
            board.update(
                    request.getTitle(),
                    request.getContent(),
                    board.getUser(),
                    board.getNewdate(),
                    board.getCount(),
                    board.getLikec()
            );
            boardRepository2.save(board);
        });
    }

    public void delete(Long id) {
        boardRepository2.deleteById(id);
    }

    public Page<Board> findAll(Pageable pageable) {
        return boardRepository2.findAll(pageable);
    }

    public Page<Board> searchByKeyword(String keyword, Pageable pageable) {
        return boardRepository2.findByTitleContainingIgnoreCase(keyword, pageable);
    }
}
