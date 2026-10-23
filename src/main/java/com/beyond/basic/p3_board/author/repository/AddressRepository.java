package com.beyond.basic.p3_board.author.repository;

import com.beyond.basic.p3_board.author.domain.Address;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("select a from Address a inner join fetch a.author")
    List<Address> findAllFetchJoin();

    @EntityGraph(attributePaths = {"author"})
    List<Address> findAll();
}
