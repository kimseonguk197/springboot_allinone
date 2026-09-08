package com.beyond.basic.p2_db.controller;

import com.beyond.basic.p2_db.domain.Member;
import com.beyond.basic.p2_db.dto.MemberReqDto;
import com.beyond.basic.p2_db.dto.MemberResDto;
import com.beyond.basic.p2_db.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> save(@RequestBody @Valid MemberReqDto member) {
//        MemberService memberService = new MemberService(); -> 싱글톤 주입으로 대체
        try {
            memberService.save(member);
//            200번 상태코드와 데이터 응답
            return ResponseEntity
                    .status(HttpStatus.CREATED)
//                    .header("Content-Type", "text/plain")
                    .body("OK1");
        }catch (IllegalAccessError e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> findByEmail(@RequestParam String email){
        try {
            MemberResDto memberResDto = memberService.findByEmail(email);
//            200번 상태코드와 데이터 응답
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(memberResDto);
        }catch (EntityNotFoundException e){
//            404번 상태코드 응답
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        try {
            MemberResDto memberResDto = memberService.findById(id);
//            200번 상태코드와 데이터 응답
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(memberResDto);
        }catch (EntityNotFoundException e){
//            404번 상태코드 응답
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){

        try {
            memberService.delete(id);
//            200번 상태코드와 데이터 응답
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("OK");
        }catch (EntityNotFoundException e){
//            404번 상태코드 응답
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

}
