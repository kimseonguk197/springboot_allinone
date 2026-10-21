package com.beyond.basic.p3_board.post.controller;

import com.beyond.basic.p3_board.post.dtos.*;
import com.beyond.basic.p3_board.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void postCreate(@ModelAttribute PostCreateDto dto){
        postService.save(dto);
    }

    @PatchMapping("/update")
    public void postUpdate(@RequestBody PostUpdateDto dto){
        postService.update(dto);
    }
    @GetMapping("/{id}")
    public PostDetailDto postDetailDto(@PathVariable Long id){
        return postService.findById(id);
    }


    @GetMapping("/list")
//    페이징처리를 위한 데이터 요청 형식 : localhost:8080/posts?page=0&size=5&sort=title,asc
//    검색+페이징처리를 위한 데이터 요청 형식 : localhost:8080/posts?page=0&size=5&sort=title,asc&title=hello&category=경제
    public List<PostListDto> postList(){
        return  postService.findAll();
    }

    @DeleteMapping("/{id}")
    public void postDelete(@PathVariable Long id){
        postService.delete(id);
    }


    @GetMapping("/paging")
//    페이징처리를 위한 데이터 요청 형식 : localhost:8080/post?page=0&size=5&sort=title,asc
//    검색+페이징처리를 위한 데이터 요청 형식 : localhost:8080/post?page=0&size=5&sort=title,asc&title=hello&category=경제
    public Page<PostListDto> postListPaging(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                         @ModelAttribute PostSearchDto searchDto){
//        return  postService.findAllPaging(pageable);
        return  postService.findAllPaging(pageable, searchDto);
    }
}
