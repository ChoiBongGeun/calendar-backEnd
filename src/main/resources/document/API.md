# API 명세서

## 인증 API

### 회원가입
- **URL**: `/api/auth/register`
- **Method**: `POST`
- **Description**: 새로운 사용자를 등록합니다.
- **Request Body**:
  ```json
  {
    "email": "string",
    "password": "string"
  }
  ```
- **Response**: `204 No Content`

### 로그인
- **URL**: `/api/auth/login`
- **Method**: `POST`
- **Description**: 이메일과 비밀번호로 로그인합니다.
- **Request Body**:
  ```json
  {
    "email": "string",
    "password": "string"
  }
  ```
- **Response**:
  ```json
  {
    "token": "string",
    "email": "string"
  }
  ```

## 할 일 API

### 할 일 생성
- **URL**: `/api/todos`
- **Method**: `POST`
- **Description**: 새로운 할 일을 생성합니다.
- **Headers**: `Authorization: Bearer {token}`
- **Request Body**:
  ```json
  {
    "title": "string",
    "description": "string",
    "dueDate": "yyyy-MM-dd",
    "completed": boolean
  }
  ```
- **Response**:
  ```json
  {
    "id": number,
    "uuid": "string",
    "title": "string",
    "description": "string",
    "dueDate": "yyyy-MM-dd",
    "completed": boolean,
    "createdAt": "yyyy-MM-dd'T'HH:mm:ss",
    "updatedAt": "yyyy-MM-dd'T'HH:mm:ss"
  }
  ```

### 할 일 목록 조회
- **URL**: `/api/todos`
- **Method**: `GET`
- **Description**: 사용자의 모든 할 일을 조회합니다.
- **Headers**: `Authorization: Bearer {token}`
- **Response**:
  ```json
  [
    {
      "id": number,
      "uuid": "string",
      "title": "string",
      "description": "string",
      "dueDate": "yyyy-MM-dd",
      "completed": boolean,
      "createdAt": "yyyy-MM-dd'T'HH:mm:ss",
      "updatedAt": "yyyy-MM-dd'T'HH:mm:ss"
    }
  ]
  ```

### 할 일 상세 조회
- **URL**: `/api/todos/{id}`
- **Method**: `GET`
- **Description**: 특정 할 일의 상세 정보를 조회합니다.
- **Headers**: `Authorization: Bearer {token}`
- **Response**:
  ```json
  {
    "id": number,
    "uuid": "string",
    "title": "string",
    "description": "string",
    "dueDate": "yyyy-MM-dd",
    "completed": boolean,
    "createdAt": "yyyy-MM-dd'T'HH:mm:ss",
    "updatedAt": "yyyy-MM-dd'T'HH:mm:ss"
  }
  ```

### 할 일 수정
- **URL**: `/api/todos/{id}`
- **Method**: `PUT`
- **Description**: 할 일 정보를 수정합니다.
- **Headers**: `Authorization: Bearer {token}`
- **Request Body**:
  ```json
  {
    "title": "string",
    "description": "string",
    "dueDate": "yyyy-MM-dd",
    "completed": boolean
  }
  ```
- **Response**:
  ```json
  {
    "id": number,
    "uuid": "string",
    "title": "string",
    "description": "string",
    "dueDate": "yyyy-MM-dd",
    "completed": boolean,
    "createdAt": "yyyy-MM-dd'T'HH:mm:ss",
    "updatedAt": "yyyy-MM-dd'T'HH:mm:ss"
  }
  ```

### 할 일 삭제
- **URL**: `/api/todos/{id}`
- **Method**: `DELETE`
- **Description**: 할 일을 소프트 삭제합니다.
- **Headers**: `Authorization: Bearer {token}`
- **Response**: `204 No Content`

### 삭제된 할 일 복원
- **URL**: `/api/todos/{id}/restore`
- **Method**: `POST`
- **Description**: 소프트 삭제된 할 일을 복원합니다.
- **Headers**: `Authorization: Bearer {token}`
- **Response**: `200 OK`

### 삭제된 할 일 목록 조회
- **URL**: `/api/todos/deleted`
- **Method**: `GET`
- **Description**: 소프트 삭제된 할 일 목록을 조회합니다.
- **Headers**: `Authorization: Bearer {token}`
- **Response**:
  ```json
  [
    {
      "id": number,
      "uuid": "string",
      "title": "string",
      "description": "string",
      "dueDate": "yyyy-MM-dd",
      "completed": boolean,
      "createdAt": "yyyy-MM-dd'T'HH:mm:ss",
      "updatedAt": "yyyy-MM-dd'T'HH:mm:ss"
    }
  ]
  ```

### 일별 할 일 조회
- **URL**: `/api/todos/date/{date}`
- **Method**: `GET`
- **Description**: 특정 날짜의 할 일을 조회합니다.
- **Headers**: `Authorization: Bearer {token}`
- **Response**:
  ```json
  [
    {
      "id": number,
      "uuid": "string",
      "title": "string",
      "description": "string",
      "dueDate": "yyyy-MM-dd",
      "completed": boolean,
      "createdAt": "yyyy-MM-dd'T'HH:mm:ss",
      "updatedAt": "yyyy-MM-dd'T'HH:mm:ss"
    }
  ]
  ```

### 월별 할 일 조회
- **URL**: `/api/todos/month/{yearMonth}`
- **Method**: `GET`
- **Description**: 특정 월의 할 일을 조회합니다.
- **Headers**: `Authorization: Bearer {token}`
- **Response**:
  ```json
  [
    {
      "id": number,
      "uuid": "string",
      "title": "string",
      "description": "string",
      "dueDate": "yyyy-MM-dd",
      "completed": boolean,
      "createdAt": "yyyy-MM-dd'T'HH:mm:ss",
      "updatedAt": "yyyy-MM-dd'T'HH:mm:ss"
    }
  ]
  ```

## 에러 응답

모든 API는 에러 발생 시 다음과 같은 형식으로 응답합니다:

```json
{
  "timestamp": "yyyy-MM-dd'T'HH:mm:ss",
  "status": number,
  "error": "string",
  "message": "string",
  "path": "string"
}
```

### 주요 에러 코드
- `400 Bad Request`: 잘못된 요청
- `401 Unauthorized`: 인증되지 않은 요청
- `403 Forbidden`: 권한이 없는 요청
- `404 Not Found`: 리소스를 찾을 수 없음
- `409 Conflict`: 리소스 충돌 (예: 이메일 중복)
- `500 Internal Server Error`: 서버 내부 오류 