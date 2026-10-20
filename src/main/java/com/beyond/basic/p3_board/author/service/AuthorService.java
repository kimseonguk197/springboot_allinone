package com.beyond.basic.p3_board.author.service;

import com.beyond.basic.p3_board.author.domain.Address;
import com.beyond.basic.p3_board.author.domain.Author;
import com.beyond.basic.p3_board.author.dtos.*;
import com.beyond.basic.p3_board.author.repository.AddressRepository;
import com.beyond.basic.p3_board.author.repository.AuthorRepository;
import com.beyond.basic.p3_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.retries.AdaptiveRetryStrategy;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AddressRepository addressRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Client s3Client;
    @Value("${aws.s3.bucket1}")
    private String bucket;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, AddressRepository addressRepository, PostRepository postRepository, PasswordEncoder passwordEncoder, S3Client s3Client){
        this.authorRepository = authorRepository;
        this.addressRepository = addressRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3Client = s3Client;
    }

    public void save(AuthorCreateDto dto){

//        email중복여부 검증
        if(authorRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 email입니다.");
        }
        Author author = Author.builder().name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
//                .role(dto.getRole())
                .build();
////        cascade persist를 활용한 예시
//        author.getPostList().add(Post.builder().title("안녕하세요").author(author).build());
        authorRepository.save(author);
//        인위적인 예외 코드를 통한 트랜잭션 보장 여부 확인
//        if(author.getName().equals("hongildong")){
//            throw new IllegalArgumentException("test");
//        }
        Address address = Address.builder()
                .city(dto.getAddress().getCity())
                .street(dto.getAddress().getStreet())
                .zipCode(dto.getAddress().getZipCode())
                .author(author)
                .build();
        addressRepository.save(address);

////       파일을 업로드를 위한 저장 객체 구성
//        if(profileImage != null){
//            String fileName = "user-"+author.getId()+"-profileimage-"+profileImage.getOriginalFilename();
//            PutObjectRequest request = PutObjectRequest.builder()
//                    .bucket(bucket)
//                    .key(fileName)
//                    .contentType(profileImage.getContentType()) //image/jpeg, video/mp4, ...
//                    .build();
////        aws에 이미지 업로드(byte형태로)
//            try {
//                s3Client.putObject(request, RequestBody.fromBytes(profileImage.getBytes()));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
////        aws의 이미지 url 추출
//            String imgUrl = s3Client.utilities().getUrl(a->a.bucket(bucket).key(fileName)).toExternalForm();
//            author.updateProfileImageUrl(imgUrl);
    }

//    트랜잭션 처리가 필요없는 조회만 있는 메서드의 경우 성능향상을 위해 readOnly처리
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public AuthorDetailDto findById(Long id){
        Optional<Author> optAuthor = authorRepository.findById(id);
        Author author = optAuthor.orElseThrow(()-> new EntityNotFoundException("entity is not found"));
//        List<Post> postList = postRepository.findAllByAuthorIdAndDelYn(author.getId(), "N");

        AuthorDetailDto dto = AuthorDetailDto.builder()
                .id(author.getId()).name(author.getName()).email(author.getEmail())
                .build();
        return dto;
    }

//    PreAuthorize : Authentcation객체 안의권한정보를 확인하는 어노테이션
//    2개 이상의 Role을 허용하는 경우 : "hasRole('ADMIN') or hasRole('SELLER') "
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll(){
        List<Author> authorList = authorRepository.findAll();
        List<AuthorListDto> authorListDtos = new ArrayList<>();
        for (Author a : authorList){
            AuthorListDto dto = new AuthorListDto(a.getId(), a.getName(), a.getEmail());
            authorListDtos.add(dto);
        }
        return authorListDtos;
    }

    public void delete(Long id){
//        데이터 조회 후 없다면 예외처리
        Author author = authorRepository.findById(id).orElseThrow(()-> new NoSuchElementException("entity is not found"));

//        삭제작업
        authorRepository.delete(author);
    }

    public void updatePw(AuthorUpdatePwDto dto){
        Author author = authorRepository.findByEmail(dto.getEmail()).orElseThrow(()->new EntityNotFoundException("entity is not found"));
        author.updatePassword(dto.getPassword());
    }

    public Author login(AuthorLoginDto dto){
        Optional<Author> opt_author = authorRepository.findByEmail(dto.getEmail());
        boolean check = true;
        if(!opt_author.isPresent()){
            check=false;
        }else {
            if(!passwordEncoder.matches(dto.getPassword(), opt_author.get().getPassword())){
                check =false;
            }
        }
        if(!check){
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
        }
        return opt_author.get();
    }


    @Transactional(readOnly = true)
    public AuthorDetailDto myinfo(String email){
        Optional<Author> optAuthor = authorRepository.findByEmail(email);
        Author author = optAuthor.orElseThrow(()-> new NoSuchElementException("entity is not found"));
        AuthorDetailDto dto = AuthorDetailDto.builder()
                .id(author.getId()).name(author.getName()).email(author.getEmail())
                .build();
        return dto;
    }
}
