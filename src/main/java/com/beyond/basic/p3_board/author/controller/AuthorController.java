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
//    PreAuthorize : Authentcation객체 안의 권한정보를 확인하는 어노테이션
//    2개 이상의 Role을 허용하는 경우 : "hasRole('ADMIN') or hasRole('SELLER') "
//    @PreAuthorize("hasRole('ADMIN')")
    public List<AuthorListDto> findAll(){
        List<AuthorListDto> dtoList = authorService.findAll();
        return dtoList;
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
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
    public String login(@RequestBody AuthorLoginDto dto){
        Author author = authorService.login(dto);
//        토큰 생성 및 리턴
        String token = jwtTokenProvider.createToken(author);
        return token;
    }


//    @GetMapping("/myinfo")
//    public ResponseEntity<?> myinfo(@AuthenticationPrincipal String principal){
//        System.out.println(principal);
//        AuthorDetailDto dto = authorService.myinfo();
//        return ResponseEntity.status(HttpStatus.OK).body(dto);
//    }

}
