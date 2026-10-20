# springboot_basic

Spring Boot 기반 게시판(Board) 서비스. 회원가입/로그인/JWT 인증·인가, 게시글 CRUD/검색/페이징을 핵심으로 하는 학습용 프로젝트

## 기술 스택

- Java 17
- Spring Boot 3.5.9 (Web, Validation, Thymeleaf)
- Spring Data JPA (Hibernate) + MySQL
- Spring Security + JWT (`jjwt`)
- Redis (refresh token 저장)
- AWS S3 (프로필 이미지 업로드)
- Gradle, JUnit5 + Mockito

## 패키지 구조

```
com.beyond.basic
├─ p1_http, p2_db          # 초반 학습용 예제 (HTTP, JDBC/JPA 기초)
└─ p3_board                # 실질적인 게시판 서비스
   ├─ author                # 회원(작성자) 도메인
   ├─ post                  # 게시글 도메인
   └─ common
      ├─ auth                 JwtTokenProvider, JwtTokenFilter
      ├─ configs              SecurityConfig, RedisConfig, AwsS3Config
      ├─ exception            CommonExceptionHandler, JwtAuthenticationHandler
      └─ init                 InitialDataLoad (서버 기동 시 관리자 계정 생성)
```

## 핵심 흐름

### 1. 회원가입 — `POST /author/create`

`AuthorController.create` → `AuthorService.save`

1. email 중복 체크 (`AuthorRepository.findByEmail`) — 중복 시 `IllegalArgumentException`
2. `PasswordEncoder`로 비밀번호 암호화 후 `Author` 저장
3. 같은 트랜잭션 안에서 `Address`(주소) 저장
4. 이름/이메일/비밀번호는 DTO에 `@NotBlank`, `@Size`로 검증 (`@Valid`) → 실패 시 `MethodArgumentNotValidException`

### 2. 로그인 & 토큰 발급 — `POST /author/login`

`AuthorController.login` → `AuthorService.login` → `JwtTokenProvider`

1. email로 `Author` 조회 후 `passwordEncoder.matches`로 비밀번호 검증 (불일치 시 `IllegalArgumentException`)
2. Access Token(`createToken`, 기본 30분) / Refresh Token(`createTokenRt`, 기본 12시간) 발급
3. Refresh Token은 Redis에 `email : token` 형태로 TTL과 함께 저장
4. 둘 다 `AuthorLoginResDto`로 응답

### 3. 인증 필터 & 인가

`JwtTokenFilter` (모든 요청에 대해 `UsernamePasswordAuthenticationFilter` 이전에 동작)

1. `Authorization: Bearer {token}` 헤더에서 토큰 추출
2. JWT 파싱/서명 검증 → claims(email, role) 추출
3. `SecurityContext`에 `Authentication`(principal = email, authority = `ROLE_{role}`) 등록

`SecurityConfig`

- `/author/create`, `/author/login`만 인증 없이 허용, 나머지는 인증 필요
- `@EnableMethodSecurity` + `@PreAuthorize("hasRole('ADMIN')")`로 메서드 단위 인가 (예: `AuthorService.findAll`, `findById`)

### 4. Refresh Token 재발급 — `POST /author/refresh-at`

`JwtTokenProvider.validateRt`

1. Refresh Token 자체 서명/만료 검증
2. 토큰의 email로 Redis에 저장된 토큰과 일치 여부 재검증
3. 검증 통과 시 새 Access Token만 발급 (Refresh Token은 그대로 재사용)

### 5. 게시글 작성/수정/조회 — `/post/**`

`PostController` → `PostService`

- **작성(`save`)**: `SecurityContextHolder`에서 현재 로그인한 email을 꺼내 작성자를 조회하고, `Post`-`Author`를 `AuthorPost`(N:M 연결 엔티티)로 묶어 저장
- **수정(`update`)**: `postId`로 게시글 조회 후 내용 변경, 공동 작성자(`AuthorPost`) 추가 가능
- **상세조회(`findById`)**: 게시글 + 연관된 모든 작성자 email 목록을 `PostDetailDto`로 반환
- **목록조회(`findAll`)**: `Specification`으로 `delYn=N`, `appointment=N` 조건에 제목/카테고리/내용 동적 검색 조건을 조립하고, `Pageable`로 페이징 처리 (`GET /post/list?page=0&size=10&title=...`)
- **삭제(`delete`)**: 실제 row를 삭제하지 않고 `delYn`을 `Y`로 변경하는 soft delete

### 6. 예외 처리

`CommonExceptionHandler` (`@RestControllerAdvice`)가 모든 컨트롤러의 예외를 공통 포맷(`CommonErrorDto`: status_code, error_message)으로 변환:

| 예외 | HTTP 상태 |
|---|---|
| `IllegalArgumentException` | 400 |
| `MethodArgumentNotValidException` | 400 |
| `EntityNotFoundException` | 404 |
| `AuthorizationDeniedException` (`@PreAuthorize` 실패) | 403 |
| 그 외 `Exception` | 500 |

### 7. 서버 기동 시 초기 데이터

`InitialDataLoad`(`CommandLineRunner`)가 기동 시 `admin@naver.com` / `12341234` 관리자 계정이 없으면 생성.

## 로컬 실행

1. MySQL 구동, `board` 스키마 생성 (계정/비번은 `application-local.yml` 참고: `root` / `test1234`)
2. Redis 구동 (`localhost:6379`)
3. `spring.profiles.active=local`로 실행 (JPA `ddl-auto: update`로 테이블 자동 생성)
