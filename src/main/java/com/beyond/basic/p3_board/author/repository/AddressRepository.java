package com.beyond.basic.p3_board.author.repository;

import com.beyond.basic.p3_board.author.domain.Address;
import com.beyond.basic.p3_board.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("select a from Address a inner join fetch a.author")
    List<Address> findAllFetchJoin();
}
