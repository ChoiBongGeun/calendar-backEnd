# Calendar Backend

Spring Boot와 PostgreSQL을 사용한 캘린더 애플리케이션의 백엔드입니다.

## 기술 스택

- Java 17
- Spring Boot 3.2.3
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT
- Swagger/OpenAPI
- Gradle

## 주요 기능

- JWT 기반 사용자 인증
- 사용자 등록 및 로그인
- Todo CRUD 기능
- 날짜별 Todo 조회
- Todo 소프트 삭제 및 복구
- Swagger/OpenAPI 문서화

## 프로젝트 구조

```
src/main/java/com/example/calendar/
├── adapter/          # API 엔드포인트 (Controller)
├── domain/           # 도메인 모델
├── service/          # 비즈니스 로직
├── store/            # 데이터 액세스 (Repository)
├── dto/              # 데이터 전송 객체
├── exception/        # 예외 처리
├── util/             # 유틸리티 클래스
└── config/           # 설정 클래스
```

## 데이터베이스

데이터베이스 스키마는 [DATABASE.md](src/main/resources/document/DATABASE.md)에서 확인할 수 있습니다.

## API 문서

API 문서는 다음 위치에서 확인할 수 있습니다:
- [API.md](src/main/resources/document/API.md)
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI 명세: http://localhost:8080/v3/api-docs

## 보안 및 인증

- JWT 기반 인증 구현
- 비밀번호 암호화 (BCrypt)
- 사용자별 Todo 접근 제어
- 소프트 삭제 구현

## 테스트

- 단위 테스트: JUnit 5, Mockito
- 통합 테스트: Spring Test, TestContainers
- 테스트 커버리지: JaCoCo

## 실행 방법

1. PostgreSQL 데이터베이스 생성
2. `application.yml` 설정
3. 프로젝트 빌드: `./gradlew build`
4. 실행: `./gradlew bootRun`

## 추가 참고사항

- 모든 API는 JWT 토큰 인증 필요
- 날짜 형식: ISO-8601 (YYYY-MM-DD)
- 시간대: UTC
- 에러 응답은 일관된 형식 제공

