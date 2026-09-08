package com.beyond.basic.p3_board.post.repository;

import com.beyond.basic.p3_board.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository  extends JpaRepository<Post, Long> {


////    Page객체 안에는 content(List<Post>), totalPages, totalElement등의 정보 포함
//    Page<Post> findAllByDelYn(Pageable pageable, String delYn);

//    검색 + 페이징처리까지 할경우, 아래와 같이 매개변수 선언.(Specification, Pageable 순서 - SimpleJpaRepository에서 정의)
//    @EntityGraph(attributePaths = "author") -> fetch join 어노테이션
    Page<Post> findAll(Specification<Post> specification, Pageable pageable);

//    예약글쓰기를 위한 findAllByAppointment 별도추가
    List<Post> findAllByAppointment(String appointment);

}
