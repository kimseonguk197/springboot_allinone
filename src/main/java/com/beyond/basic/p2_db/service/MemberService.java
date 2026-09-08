package com.beyond.basic.p2_db.service;

import com.beyond.basic.p2_db.domain.Member;
import com.beyond.basic.p2_db.dto.MemberReqDto;
import com.beyond.basic.p2_db.dto.MemberResDto;
import com.beyond.basic.p2_db.repository.MemberJdbcRepository;
import com.beyond.basic.p2_db.repository.MemberJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

//내부의 Component어노테이션을 통해 싱글톤(단하나의)객체가 생성되고, 스프링에 의해 스프링컨텍스트에서 관리
@Service
public class MemberService {

//    의존성주입(DI)방법1. 필드주입 - Autowired 어노테이션사용 (간편방식)
//    @Autowired
//    private MemberRepository memberRepository;

//    의존성주입(DI)방법2. 생성자주입방식(가장많이 사용)

//    의존성주입방법3. 클래스 상단에 @RequiredArgsConstructor 사용
    private final MemberJpaRepository memberRepository;

    @Autowired
    public MemberService(MemberJpaRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String save(MemberReqDto memberReqDto){
//        Member member = new Member(memberReqDto.getName(), memberReqDto.getEmail());
        Member member = Member.builder().name(memberReqDto.getName()).email(memberReqDto.getEmail()).build();
        Optional<Member> optMember =  memberRepository.findByEmail(memberReqDto.getEmail());
        if(optMember.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
//        MemberRepository memberRepository = new MemberRepository();
        memberRepository.save(member);
        return "ok";
    }

    public MemberResDto findByEmail(String email) throws EntityNotFoundException{
//        MemberRepository memberRepository = new MemberRepository();
        Member member =  memberRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        MemberResDto dto = MemberResDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail()).createdTime (member.getCreated_Time())
                .build();
        return dto;
    }

    public MemberResDto findById(Long id) throws EntityNotFoundException{
        Member member =  memberRepository.findById(id).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        MemberResDto dto = MemberResDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail()).createdTime (member.getCreated_Time())
                .build();
        return dto;
    }


    public void delete(Long id) throws EntityNotFoundException{
//        memberRepository.deleteById(id);
        Member member =  memberRepository.findById(id).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        memberRepository.delete(member);
    }
}
