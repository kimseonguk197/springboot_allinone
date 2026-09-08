package com.beyond.basic.p2_db.controller;

import com.beyond.basic.p2_db.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RestController
@RequestMapping("/member")
public class MemberAsisController {

    @Autowired
    private DataSource dataSource;

    @PostMapping("/create")
//            http메시지를 요청받고, 응답을 주는 기늠
    public String jsonDb(@RequestBody Member member){

        try {

//           이 부분에서 email 중복여부, 비밀번호 길이 검증 등 다양한 유효성검증 로직 및 복잡한 로직 수행 가능

//            데이터베이스 연동 작업
            Connection connection = dataSource.getConnection();
            String sql = "insert into member(name, email) values(?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.executeUpdate();
            System.out.println("db 저장 완료");
        } catch (SQLException e) {
            System.out.println("db 저장 실패");
            throw new RuntimeException(e);
        }

        return "ok";
    }

}
