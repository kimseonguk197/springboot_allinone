package com.beyond.basic.simple_board.author.controller;

import com.beyond.basic.simple_board.author.domain.Author;
import com.beyond.basic.simple_board.author.dtos.*;
import com.beyond.basic.simple_board.author.service.AuthorService;
import com.beyond.basic.simple_board.common.auth.JwtTokenProvider;
import com.beyond.basic.simple_board.common.dtos.CommonErrorDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public AuthorController(AuthorService authorService, JwtTokenProvider jwtTokenProvider) {
        this.authorService = authorService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/create")
//    dto에 있는 validation어노테이션과 @Valid가 한쌍

    public ResponseEntity<?> create(@RequestPart("author") @Valid AuthorCreateDto dto,
                                    @RequestPart("profileImage") MultipartFile profileImage){

//        아래 예외처리는 ExceptionHandler에서 전역적으로 예외처리
//        try {
//            authorService.save(dto);
//            return ResponseEntity.status(HttpStatus.CREATED).body("OK");
//        }catch (IllegalArgumentException e){
//            e.printStackTrace();
//            CommonErrorDto commonErrorDto = CommonErrorDto.builder()
//                    .status_code(400)
//                    .error_message(e.getMessage())
//                    .build();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonErrorDto);
//        }
        authorService.save(dto, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }

    @GetMapping("/list")
//    PreAuthorize : Authentcation객체 안의 권한정보를 확인하는 어노테이션
//    2개 이상의 Role을 허용하는 경우 : "hasRole('ADMIN') or hasRole('SELLER') "
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuthorListDto> findAll(){
        List<AuthorListDto> dtoList = authorService.findAll();
        return dtoList;
    }


//    아래와 같이 http응답 body를 분기처리한다하더라도 상태코드는 200으로 고정.
//    @GetMapping("/{id}")
//    public Object findById(@PathVariable Long id){
//        try {
//            AuthorDetailDto dto = authorService.findById(id);
//            return dto;
//        }catch (NoSuchElementException e){
//            e.printStackTrace();
//            return CommonErrorDto.builder()
//                    .status_code(404)
//                    .error_message(e.getMessage())
//                    .build();
//        }
//    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findById(@PathVariable Long id){
        try {
            AuthorDetailDto dto = authorService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        }catch (NoSuchElementException e){
            e.printStackTrace();
            CommonErrorDto dto = CommonErrorDto.builder()
                    .status_code(404)
                    .error_message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
        }
    }

    @GetMapping("/myinfo")
    public ResponseEntity<?> myinfo(@AuthenticationPrincipal String principal){
        System.out.println(principal);
        AuthorDetailDto dto = authorService.myinfo();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }




    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        authorService.delete(id);
        return "ok";
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthorLoginDto dto){
        Author author = authorService.login(dto);
//        토큰 생성 및 리턴
        String token = jwtTokenProvider.createToken(author);
        return token;
    }

    @PatchMapping("/update/password")
    public void updatePw(@RequestBody AuthorUpdatePwDto updatePwDto){
        authorService.updatePw(updatePwDto);
    }
}
