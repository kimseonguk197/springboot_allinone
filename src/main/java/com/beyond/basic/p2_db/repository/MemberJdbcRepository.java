package com.beyond.basic.p2_db.repository;

import com.beyond.basic.p2_db.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

//    jdbc의 단점
//    1.쿼리 직접 작성 : 1)raw쿼리에서 오타가 나도 컴파일에러X 2)데이터 추가시, 컬럼의 매핑을 수작업
//    2.데이터 조회후 객체조립을 수작업
@Repository
public class MemberJdbcRepository {
    @Autowired
    private DataSource dataSource;
    public String save(Member member){
        try {
//            데이터베이스 연동 작업
            Connection connection = dataSource.getConnection();
            String sql = "insert into member(name, email, created_time) values(?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setTimestamp(3, Timestamp.valueOf(member.getCreated_Time()));
//            excuteUpdate : 추가/수정, excuteQuery:조회
            ps.executeUpdate();
            System.out.println("db 저장 완료");
        } catch (SQLException e) {
            System.out.println("db 저장 실패");
            throw new RuntimeException(e);
        }
        return "ok";
    }

    public Optional<Member> findByEmail(String inputEmail){
        Member member = null;
        try {
            Connection connection = dataSource.getConnection();
            String sql = "select * from member where email = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, inputEmail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                String name = rs.getString("name");
                String email = rs.getString("email");
                LocalDateTime createdTime = rs.getTimestamp("created_time").toLocalDateTime();
//                member = new Member(name, email, createdTime);
                member = Member.builder().name(name).email(email).created_Time(createdTime).build();
                System.out.println("db 조회 완료");
            }
        } catch (SQLException e) {
            System.out.println("db 조회 실패");
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(member);
    }

}
