package com.beyond.basic.p3_board.ai.tools;

import com.beyond.basic.p3_board.post.dtos.PostCreateDto;
import com.beyond.basic.p3_board.post.dtos.PostDetailDto;
import com.beyond.basic.p3_board.post.dtos.PostListDto;
import com.beyond.basic.p3_board.post.dtos.PostSearchDto;
import com.beyond.basic.p3_board.post.dtos.PostUpdateDto;
import com.beyond.basic.p3_board.post.service.PostService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

//LLM이 게시글 관련 API를 호출할 수 있도록 노출하는 tool 모음
@Component
public class PostAiTools {

    private final PostService postService;

    @Autowired
    public PostAiTools(PostService postService) {
        this.postService = postService;
    }

    @Tool(description = "제목과 내용으로 새 게시글을 작성한다. 작성자는 현재 로그인한 사용자로 자동 지정된다.")
    public String createPost(@ToolParam(description = "게시글 제목") String title,
                             @ToolParam(description = "게시글 내용") String contents) {
        PostCreateDto dto = PostCreateDto.builder()
                .title(title)
                .contents(contents)
                .build();
        postService.save(dto);
        return "게시글이 생성되었습니다. 제목: " + title;
    }

    @Tool(description = "게시글 id로 상세 내용(제목, 내용, 작성자 이메일 목록)을 조회한다.")
    public PostDetailDto getPost(@ToolParam(description = "조회할 게시글 id") Long postId) {
        return postService.findById(postId);
    }

    @Tool(description = "삭제되지 않은 전체 게시글 목록(id, 제목)을 조회한다.")
    public List<PostListDto> listPosts() {
        return postService.findAll();
    }

    @Tool(description = "게시글을 삭제한다. 실제로는 delYn 플래그만 변경되는 소프트 삭제이다.")
    public String deletePost(@ToolParam(description = "삭제할 게시글 id") Long postId) {
        postService.delete(postId);
        return "게시글(id=" + postId + ")이 삭제되었습니다.";
    }
}
