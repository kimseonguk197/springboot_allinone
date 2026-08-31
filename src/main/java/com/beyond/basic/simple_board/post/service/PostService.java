package com.beyond.basic.simple_board.post.service;

import com.beyond.basic.simple_board.author.domain.Author;
import com.beyond.basic.simple_board.author.repository.AuthorRepository;
import com.beyond.basic.simple_board.post.domain.Post;
import com.beyond.basic.simple_board.post.dtos.PostCreateDto;
import com.beyond.basic.simple_board.post.dtos.PostDetailDto;
import com.beyond.basic.simple_board.post.dtos.PostListDto;
import com.beyond.basic.simple_board.post.dtos.PostSearchDto;
import com.beyond.basic.simple_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public void save(PostCreateDto dto){
//        Author author = authorRepository.findByEmail(dto.getAuthorEmail()).orElseThrow(()->new EntityNotFoundException("email is not found"));
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        System.out.println(email);
        Author author = authorRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("email is not found"));

        postRepository.save(dto.toEntity(author));
    }


    public PostDetailDto findById(Long id){
        Post post = postRepository.findById(id).orElseThrow(()->new EntityNotFoundException("entity is not found"));
//        Author author = authorRepository.findById(post.getAuthorId()).orElseThrow(()->new EntityNotFoundException("entity is not found"));
//        PostDetailDto postDetailDto = PostDetailDto.fromEntity(post, author);
        PostDetailDto postDetailDto = PostDetailDto.fromEntity(post);
        return postDetailDto;
    }

    public Page<PostListDto> findAll(Pageable pageable, PostSearchDto searchDto){
//        List<Post> postList = postRepository.findAllByDelYn("N");
//        List<Post> postList = postRepository.findAllFetchInnerJoin();
//        Page<Post> postList = postRepository.findAll(pageable);

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
                if(searchDto.getCategory() != null){
                    predicateList.add(criteriaBuilder.equal(root.get("category"), searchDto.getCategory()));
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
        return postList.map(p->PostListDto.fromEntity(p));
    }

    public void delete(Long id){
        Post post = postRepository.findById(id).orElseThrow(()->new EntityNotFoundException("post is not found"));
        post.deletePost();
    }
}
