# 데이터베이스 정의서

## 테이블 정의

### users 테이블
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100),
    delete_flag BOOLEAN DEFAULT FALSE,
    updated_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);
```

### todos 테이블
```sql
CREATE TABLE todos (
    id UUID PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE,
    completed BOOLEAN DEFAULT FALSE,
    delete_flag BOOLEAN DEFAULT FALSE,
    updated_at TIMESTAMP
);
```

## 필수 확장 프로그램

PostgreSQL의 UUID 생성을 위해 다음 확장을 활성화해야 합니다:
```sql
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
```

## 인덱스

### users 테이블 인덱스
```sql
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_delete_flag ON users(delete_flag);
```

### todos 테이블 인덱스
```sql
CREATE INDEX idx_todos_user_id ON todos(user_id);
CREATE INDEX idx_todos_due_date ON todos(due_date);
CREATE INDEX idx_todos_delete_flag ON todos(delete_flag);
CREATE INDEX idx_todos_completed ON todos(completed);
```

## 제약 조건

### users 테이블 제약 조건
- `id`: 자동 증가하는 기본 키
- `email`: 고유한 이메일 주소
- `password`: 암호화된 비밀번호
- `delete_flag`: 소프트 삭제 플래그

### todos 테이블 제약 조건
- `id`: UUID 기본 키
- `user_id`: users 테이블의 외래 키
- `title`: 필수 입력 항목
- `delete_flag`: 소프트 삭제 플래그

## 설명

1. **식별자**
   - `users.id`: 자동 증가하는 기본 키
   - `todos.id`: UUID를 사용한 고유 식별자

2. **소프트 삭제**
   - 모든 테이블은 `delete_flag` 필드를 가짐
   - 실제 데이터는 삭제되지 않고 플래그만 변경됨
   - 삭제된 데이터는 복원 가능

3. **감사 필드**
   - `updated_at`: 레코드 생성/수정 시간 (Java에서 System.currentTimeMillis()로 설정)
   - `created_by`: 생성자
   - `updated_by`: 수정자 