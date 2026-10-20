package com.beyond.basic.p3_board.author.controller;

import com.beyond.basic.p3_board.author.domain.Author;
import com.beyond.basic.p3_board.author.dtos.*;
import com.beyond.basic.p3_board.author.service.AuthorService;
import com.beyond.basic.p3_board.common.auth.JwtTokenProvider;
import com.beyond.basic.p3_board.common.dtos.CommonErrorDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> create(@RequestBody @Valid AuthorCreateDto dto){
        System.out.println(dto);
//    public ResponseEntity<?> create(@RequestBody("author") @Valid AuthorCreateDto dto){
//                                    @RequestPart("profileImage") MultipartFile profileImage){

//        아래 예외처리는 ExceptionHandler에서 전역적으로 예외처리
        authorService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }

    @GetMapping("/list")
    public List<AuthorListDto> findAll(){
        List<AuthorListDto> dtoList = authorService.findAll();
        return dtoList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        try {
            AuthorDetailDto dto = authorService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        }catch (NoSuchElementException e){
            CommonErrorDto dto = CommonErrorDto.builder()
                    .status_code(404)
                    .error_message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
        }
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        authorService.delete(id);
        return "ok";
    }

    @PatchMapping("/{authorId}/password")
    public void updatePw(@RequestBody AuthorUpdatePwDto updatePwDto){
        authorService.updatePw(updatePwDto);
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthorLoginDto dto){
        Author author = authorService.login(dto);
//        토큰 생성 및 리턴
        String token = jwtTokenProvider.createToken(author);
        String tokenRt = jwtTokenProvider.createTokenRt(author);

        AuthorLoginResDto authorLoginResDto = AuthorLoginResDto.builder()
                .accessToken(token)
                .refreshToken(tokenRt)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(authorLoginResDto);
    }


    @GetMapping("/myinfo")
    public ResponseEntity<?> myinfo(@AuthenticationPrincipal String email){
        AuthorDetailDto dto = authorService.myinfo(email);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }


    @PostMapping("/refresh-at")
    public ResponseEntity<?>  refreshAt(@RequestBody RefreshTokenDto dto){
//        rt검증(1.토큰 자체 검증 2.redis조회 검증)
        Author author = jwtTokenProvider.validateRt(dto.getRefreshToken());
//        at신규 생성
        String accessToken = jwtTokenProvider.createToken(author);
//        refresh생성 및 저장
        AuthorLoginResDto authorLoginResDto = AuthorLoginResDto.builder()
                .accessToken(accessToken)
                .refreshToken(dto.getRefreshToken())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(authorLoginResDto);
    }


}
