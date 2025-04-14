# 🗕️ Calendar Backend (Spring Boot + PostgreSQL)

React + Next.js 기반의 카드르 + 할 일 + 알림 앱의 백엔드입니다.  
Spring Boot, Spring Security, JWT, PostgreSQL, Swagger를 사용하여 구축되었습니다.

---

## ✅ 기술 스택

- Java 11
- Spring Boot 2.7
- Spring Security
- JPA (Hibernate)
- PostgreSQL
- Gradle
- JWT 인증
- Swagger (OpenAPI)
- JUnit5 테스트

---

## 📂️ 디렉토리 구조 (DDD + Hexagonal Architecture 기반)

```
calendar-backend/
├── adapter/         # 외부 I/O 계층 (Controller, API 등)
├── config/          # Security 설정 등 공통 설정
├── domain/          # 해수 도메인 목록 (Entity, VO 등)
├── query/           # 조회 전용 로직 (CQRS용)
├── service/         # Application Service 계층
├── spec/            # 검색 조건 등 정책 객체
└── store/           # Repository 및 구현처
```

---

## 🦾 주요 테이블 정의 (PostgreSQL)

### users 테이블
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    uuid UUID DEFAULT gen_random_uuid(), -- 외부 노출용 식별자
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100),
    delete_flag BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);
```

### todos 테이블
```sql
CREATE TABLE todos (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE,
    completed BOOLEAN DEFAULT FALSE,
    delete_flag BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);
```

> `gen_random_uuid()` 사용을 위해 다음 확장을 활성화해야 합니다:
```sql
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
```

---

## 🔐 보안 및 인증

- Spring Security 기반 로그인
- JWT 토큰 발급 및 인증 필터 적용 예정
- 각 사용자별로 개인화된 할 일 및 카드르 조회 가능

---

## 🔎 API 문서

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI Spec: `/v3/api-docs`

---

## 🧪 테스트

- JUnit 5 기반 통합 테스트 및 유니트 테스트 구성
- `@SpringBootTest`, `@WebMvcTest` 사용 예정

---

## ✨ 향후 계획

- [ ] JWT 로그인 구현
- [ ] 사용자 등록 / 인증 API
- [ ] 사용자별 할 일 CRUD
- [ ] 카드르 일별/월별 조회 API
- [ ] 알림 시스템 설계 및 구현 (이후 단계)

---

## 🙌 기타

- `id`: DB 내부용 식별자 (Join, 검색 최적화용)
- `uuid`: 외부 노출용 고유 식별자 (API 응답 및 URL용)

# Calendar Backend

스프링 부트 기반의 캘린더 백엔드 애플리케이션입니다.

## 주요 기능

- 사용자 인증 (회원가입, 로그인)
- Todo 관리 (CRUD)
- 알림 기능
  - Todo 마감일 알림
  - 이메일 알림 전송
- 소프트 삭제 및 복원 기능

## 기술 스택

- Java 17
- Spring Boot 3.2.3
- Spring Security
- Spring Data JPA
- MySQL
- JWT
- Lombok
- Maven

## 시작하기

### 필수 조건

- Java 17
- MySQL 8.0
- Maven

### 설정

1. MySQL 데이터베이스 생성
```sql
CREATE DATABASE calendar;
```

2. application.yml 설정
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/calendar
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: your_jwt_secret
  expiration: 86400000 # 24시간

mail:
  host: smtp.gmail.com
  port: 587
  username: your_email@gmail.com
  password: your_app_password
```

3. 애플리케이션 실행
```bash
mvn spring-boot:run
```

## API 문서

API 문서는 Swagger UI를 통해 확인할 수 있습니다:
- http://localhost:8080/swagger-ui.html

## 보안

- JWT 기반 인증
- 비밀번호 암호화
- UUID 기반 식별자
- 소프트 삭제 구현

## 알림 기능

- Todo 마감일 알림
- 이메일 알림 전송
- 알림 설정 관리

## 라이센스

이 프로젝트는 MIT 라이센스 하에 배포됩니다.

