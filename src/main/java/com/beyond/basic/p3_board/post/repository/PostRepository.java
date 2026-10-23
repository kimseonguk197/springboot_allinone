package com.beyond.basic.p3_board.post.repository;

import com.beyond.basic.p3_board.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository  extends JpaRepository<Post, Long> {


//    //    Page객체 안에는 content(List<Post>), totalPages, totalElement등의 정보 포함
//    Page<Post> findAll(Pageable pageable);

//    Page<Post> findAll(String delYn, Pageable pageable);

//    검색 + 페이징처리까지 할경우, 아래와 같이 매개변수 선언.(Specification, Pageable 순서 - SimpleJpaRepository에서 정의)
    Page<Post> findAll(Specification<Post> specification, Pageable pageable);

}
