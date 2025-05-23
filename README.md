# 👨‍🦲 Delivery - Outsourcing Project

대용량 데이터를 안정적이고 빠르게 처리하기 위한 구조로 설계된 배달 플랫폼 프로젝트입니다.
QueryDSL 기반의 JPAQuery로 복잡한 조건의 동적 쿼리를 효율적으로 처리하며,
Redis를 활용해 로그인 세션 관리와 함께 일부 데이터에 대한 캐시 전략을 도입해 성능을 최적화했습니다.
회원, 가게, 메뉴, 주문, 즐겨찾기, 리뷰 등 실제 배달 서비스의 핵심 기능을 구현했습니다.

---

## 🤖 설정

### 🛠 작업 환경

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- MySQL (환경변수 설정)
- Redis (Docker 사용)
- JWT
- Spring Security

### 🤝 협업 Tool

- GitHub
- Notion

### ⚙️ 환경 변수

- **DB**: MySQL 접속 정보 환경변수 관리
- **JWT**: Secret Key를 환경변수로 관리하여 보안 강화
- **Redis**: Docker 컨테이너 사용

---

## 💡 프로젝트 특징

- JPAQuery(QueryDSL) 기반으로 복잡한 검색 조건도 타입 안전하고 가독성 있게 구현
  → 예: 메뉴, 리뷰, 가게 등 다양한 조건에 따라 페이징 및 정렬 처리
- Redis 기반 캐시 처리를 통해 자주 조회되는 데이터의 응답 속도 향상
- JWT + Spring Security로 인증/인가 처리 → Access + Refresh Token 전략
- Spring Data JPA + DTO 매핑 전략으로 API 응답 최적화

---

## 🔗 링크(깃허브, 명세서)

- [팀 프로젝트 GitHub Repository](https://github.com/suj9810/Delivery.git)
- [API 명세서 (Notion 링크)](https://www.notion.so/teamsparta/19-API-1dd2dc3ef514809798b6dca67b18e0e9)

### API Endpoints

1. 회원/로그인 API

| HTTP   | URI                         | 설명       |
|:-------|:----------------------------|:---------|
| POST   | /auths/signup               | 회원가입     |
| POST   | /auths/login                | 로그인      |
| POST   | /auths/logout               | 로그아웃     |
| DELETE | /auths/me                   | 회원 탈퇴    |
| GET    | /users/{id}                 | 회원 조회    |
| PUT    | /users/me                   | 회원 정보 수정 |
| PATCH  | /users/{id}/update-password | 비밀번호 수정  |

2. 가게 API

| HTTP  | URI                | 설명         |
|:------|:-------------------|:-----------|
| POST  | /srotes            | 가게 등록      |
| GET   | /srotes            | 가게명 리스트 조회 |
| GET   | /srotes/{srotesId} | 단일 가게 조회   |
| PUT   | /srotes/{srotesId} | 가게 수정      |
| PATCH | /srotes/{srotesId} | 가게 폐업      |

3. 즐겨찾기 API

| HTTP   | URI                                      | 설명         |
|:-------|:-----------------------------------------|:-----------|
| POST   | /users/{userId}/favorites/{storeId}      | 즐겨찾기 추가    |
| GET    | /users/{userId}/favorites                | 즐겨찾기 목록 조회 |
| DELETE | /users/{userId}/favorites/{storeId}      | 즐겨찾기 삭제    |

4. 메뉴 API

| HTTP   | URI             | 설명       |
|:-------|:----------------|:---------|
| POST   | /menus          | 메뉴 등록    |
| GET    | /menus          | 메뉴 목록 조회 |
| PUT    | /menus/{menuId} | 메뉴 수정    |
| DELETE | /menus/{menuId} | 메뉴 삭제    |

5. 리뷰 API

| HTTP   | URI                                                                                                    | 설명      |
|:-------|:-------------------------------------------------------------------------------------------------------|:--------|
| POST   | /reviews/{storeId}                                                                                     | 리뷰생성    |
| GET    | /reviews?storeId={storeId}&minRating={minRating}&maxRating={maxRating}&page={page}&size={size}         | 리뷰 조회   |
| PUT    | /reviews/{reviewId}                                                                                    | 리뷰 수정   |
| DELETE | /reviews/{reviewId}                                                                                    | 리뷰 삭제   |

6. 검색 API

| HTTP   | URI                                                  | 설명                   |
|:-------|:-----------------------------------------------------|:---------------------|
| GET    | /v1/keywords/search?keyword={keyword}&page=0&size=10 | 키워드로 가게 이름 검색 v1     |
| GET    | /v2/keywords/search?keyword=피자&page=0&size=10        | 키워드로 가게 이름 검색 v2     |
| GET    | /v1/keywords/popular?page=0&size=10                  | 가게 인기 키워드 조회 v1      |
| GET    | /v2/keywords/popular?page=0&size=10                  | 가게 인기 키워드 조회 v2      |

### API Status Codes

1. 회원/로그인

| API      | 성공          | 실패 (예시)                                              |
|:---------|:------------|:-----------------------------------------------------|
| 회원가입     | 201 CREATED | 400 BAD_REQUEST(형식 오류) ,409 CONFLICT(이메일 중복)         |
| 로그인      | 200 OK      | 401 UNAUTHORIZED(비밀번호 불일치), 404 NOT_FOUND(유저 없음)     |
| 로그아웃     | 200 OK      | 401 UNAUTHORIZED(로그인 필요)                             |
| 회원탈퇴     | 200 OK      | 400 UNAUTHORIZED(비밀번호 불일치)                           |
| 회원 조회    | 200 OK      | 404 NOT_FOUND(유저 없음)                                 |
| 회원 정보 수정 | 200 OK      | -                                                    |
| 비밀번호 수정  | 200 OK      | 400 BAD_REQUEST(비밀번호 불일치), 401 UNAUTHORIZED(동일 비밀번호) |

2. 가게

| API          | 성공          | 실패 (예시)              |
|:-------------|:------------|:---------------------|
| 가게 등록        | 201 CREATED | 403 FORBIDDEN(권한 없음) |
| 가게 수정        | 200 OK      | 404 NOT_FOUND(가게 없음) |
| 전체 가게 조회     | 200 OK      | 404 NOT_FOUND(가게 없음) |
| 단일 가게 조회     | 200 OK      | 404 NOT_FOUND(가게 없음) |
| 가게 폐업        | 200 OK      | 403 FORBIDDEN(권한 없음) |

3. 즐겨찾기

| API     | 성공     | 실패 (예시) |
|:--------|:-------|:--------|
| 즐겨찾기 추가 | 200 OK | -       |
| 즐겨찾기 삭제 | 200 OK | -       |

4. 메뉴

| HTTP  | URI         | 설명                                                                                                  |
|:------|:------------|:----------------------------------------------------------------------------------------------------|
| 메뉴 등록 | 201 CREATED | 400 BAD_REQUEST(요청 오류), 401 UNAUTHORIZED(사용자 인증 x), 403 FORBIDDEN(사장님 권한 x), 404 NOT_FOUN(가게, 메뉴 x) |
| 메뉴 조회 | 200 OK      | 400 BAD_REQUEST(요청 오류), 401 UNAUTHORIZED(사용자 인증 x), 403 FORBIDDEN(사장님 권한 x)                         |
| 메뉴 수정 | 200 OK      | 400 BAD_REQUEST(요청 오류), 401 UNAUTHORIZED(사용자 인증 x), 403 FORBIDDEN(사장님 권한 x), 404 NOT_FOUN(가게, 메뉴 x) |
| 메뉴 삭제 | 204 OK      | 400 BAD_REQUEST(요청 오류), 401 UNAUTHORIZED(사용자 인증 x), 403 FORBIDDEN(사장님 권한 x), 404 NOT_FOUN(가게, 메뉴 x) |

5. 리뷰

| HTTP    | URI         | 설명                                                                 |
|:--------|:------------|:-------------------------------------------------------------------|
| 리뷰생성    | 201 CREATED | 400 BAD_REQUEST(완료 상태 x), 403 FORBIDDEN(내 주문 x) 409 CONFLICT(리뷰중복) |
| 리뷰 조회   | 200 OK      | 400 BAD_REQUEST(요청 오류)                                             |
| 리뷰 삭제   | 200 OK      | 400 BAD_REQUEST(요청 오류)                                             |
| 리뷰 삭제   | 200 OK      | 400 BAD_REQUEST(요청 오류)                                             |

6. 검색

| API              | 성공     | 실패 (예시)        |
|:-----------------|:-------|:---------------|
| 키워드로 가게 이름 검색 v1 | 200 OK | 204 NO_CONTENT | 
| 키워드로 가게 이름 검색 v2 | 200 OK | 204 NO_CONTENT |
| 가게 인기 키워드 조회 v1  | 200 OK | 204 NO_CONTENT |
| 가게 인기 키워드 조회 v2  | 200 OK | 204 NO_CONTENT |

---

## 📂 폴더 구조

```
├─main
│  ├─generated
│  │  └─com
│  │      └─example
│  │          └─delivery
│  │              ├─common
│  │              │  └─entity
│  │              └─domain
│  │                  ├─keyword
│  │                  │  └─entity
│  │                  ├─menu
│  │                  │  ├─dto
│  │                  │  │  └─response
│  │                  │  └─entity
│  │                  ├─reviews
│  │                  │  ├─dto
│  │                  │  │  └─response
│  │                  │  └─entity
│  │                  ├─store
│  │                  │  └─entity
│  │                  └─user
│  │                      └─entity
│  ├─java
│  │  └─com
│  │      └─example
│  │          └─delivery
│  │              ├─common
│  │              │  ├─config
│  │              │  ├─entity
│  │              │  ├─exception
│  │              │  │  └─enums
│  │              │  └─response
│  │              └─domain
│  │                  ├─auth
│  │                  │  ├─controller
│  │                  │  ├─dto
│  │                  │  ├─jwt
│  │                  │  ├─repository
│  │                  │  └─service
│  │                  ├─favorites
│  │                  │  ├─controller
│  │                  │  └─repository
│  │                  ├─keyword
│  │                  │  ├─controller
│  │                  │  ├─dto
│  │                  │  │  └─response
│  │                  │  ├─entity
│  │                  │  ├─repository
│  │                  │  └─service
│  │                  ├─menu
│  │                  │  ├─controller
│  │                  │  ├─dto
│  │                  │  │  ├─request
│  │                  │  │  └─response
│  │                  │  ├─entity
│  │                  │  ├─repository
│  │                  │  └─service
│  │                  ├─reviews
│  │                  │  ├─controller
│  │                  │  ├─dto
│  │                  │  │  ├─request
│  │                  │  │  └─response
│  │                  │  ├─entity
│  │                  │  ├─repository
│  │                  │  └─service
│  │                  ├─store
│  │                  │  ├─controller
│  │                  │  ├─dto
│  │                  │  │  ├─request
│  │                  │  │  └─response
│  │                  │  ├─entity
│  │                  │  ├─repository
│  │                  │  └─service
│  │                  └─user
│  │                      ├─controller
│  │                      ├─dto
│  │                      │  ├─request
│  │                      │  └─response
│  │                      ├─entity
│  │                      ├─repository
│  │                      └─service
│  └─resources
└─test
```

---

## 👥 작성자

- Developed by **7조 : 신은주, 김하늘, 박형우, 조혁준, 이한나**

---
