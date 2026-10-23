package com.beyond.basic.p3_board.post.service;

import com.beyond.basic.p3_board.author.domain.Author;
import com.beyond.basic.p3_board.author.repository.AuthorRepository;
import com.beyond.basic.p3_board.common.sse.SseAlarmService;
import com.beyond.basic.p3_board.post.domain.AuthorPost;
import com.beyond.basic.p3_board.post.domain.Post;
import com.beyond.basic.p3_board.post.dtos.*;
import com.beyond.basic.p3_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    private final SseAlarmService sseAlarmService;
    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository, SseAlarmService sseAlarmService) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
        this.sseAlarmService = sseAlarmService;
    }

    public void save(PostCreateDto dto){
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Author author = authorRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("email is not found"));
        Post post = Post.builder()
                .title(dto.getTitle())
                .contents(dto.getContents())
                .build();
        AuthorPost authorPost = AuthorPost.builder()
                .author(author)
                .post(post)
                .build();
        post.getAuthorPostList().add(authorPost);
        postRepository.save(post);


//        주문성공시 admin 유저에게 알림메시지 전송
        String message = post.getId() + "게시글이 등록 되었습니다.";
        sseAlarmService.sendMessage("admin@naver.com", email, message);
    }


    public void update(PostUpdateDto dto){
        Post post = postRepository.findById(dto.getPostId()).orElseThrow(()->new EntityNotFoundException("email is not found"));
        post.updateContents(dto.getContents());
        Author author = authorRepository.findByEmail(dto.getAuthorEmail()).orElseThrow(()->new EntityNotFoundException("email is not found"));
        AuthorPost authorPost = AuthorPost.builder()
                .author(author)
                .post(post)
                .build();
        post.getAuthorPostList().add(authorPost);
    }


    public PostDetailDto findById(Long id){
        Post post = postRepository.findById(id).orElseThrow(()->new EntityNotFoundException("entity is not found"));
        List<String> emailList = new ArrayList<>();
        for (AuthorPost authorPost : post.getAuthorPostList()){
            emailList.add(authorPost.getAuthor().getEmail());
        }
        PostDetailDto postDetailDto = PostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .authorEmail(emailList)
                .build();
        return postDetailDto;
    }

    public List<PostListDto> findAll(){
        List<Post> postList =  postRepository.findAll();
        List<PostListDto> postListDtos = new ArrayList<>();
        for (Post p : postList){
            PostListDto dto = PostListDto.builder()
                    .id(p.getId())
                    .title(p.getTitle())
                    .build();
            postListDtos.add(dto);
        }
        return postListDtos;
    }

    public Page<PostListDto> findAllPaging(Pageable pageable, PostSearchDto searchDto){
//        Page<Post> postList = postRepository.findAll(pageable);
//    Page<Post> postList = postRepository.findAll("N", pageable);


//        검색을 위한 Specification 객체 조립
        Specification<Post> specification = new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("delYn"), "N"));
                predicateList.add(criteriaBuilder.equal(root.get("appointment"), "N"));
//                root : 엔티티의 컬럼명을 접근하기 위한 객체, criteriaBuilder:쿼리를 생성하기 위한 객체
                if(searchDto.getTitle() != null){
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%"+searchDto.getTitle()+"%"));
                }
                if(searchDto.getContents() != null){
                    predicateList.add(criteriaBuilder.like(root.get("contents"), "%"+searchDto.getContents()+"%"));
                }
                Predicate[] predicateArr = new Predicate[predicateList.size()];
                for (int i=0; i<predicateArr.length; i++){
                    predicateArr[i] = predicateList.get(i);
                }
//                Predicate에는 검색조건들이 담길것이고, 이 Predicate list를 한줄의 predicate조립
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };
        Page<Post> postList = postRepository.findAll(specification, pageable);

//        Page객체 안에 Entity->Dto로 쉽게 변환할수 있는 편의 제공
        Page<PostListDto> postListDtos =  postList.map(p -> PostListDto.builder()
                .id(p.getId())
                .title(p.getTitle())
                .build());
        return postListDtos;
    }

    public void delete(Long id){
        Post post = postRepository.findById(id).orElseThrow(()->new EntityNotFoundException("post is not found"));
        post.deletePost();
    }
}
