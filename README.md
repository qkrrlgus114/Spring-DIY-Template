# Spring Framework 직접 구현해보기 

## 강의 서비스

강의 등록 및 회원 가입 기능을 제공하는 RESTful API 서버입니다.

## 기능

- 회원 가입
- 강의 등록 / 수정 / 삭제 / 목록 조회

## API 명세

### 강의

| 메서드 | URL | 설명 |
|--------|-----|------|
| GET | /lectures | 강의 목록 조회 |
| POST | /lectures | 강의 등록 |
| PUT | /lectures | 강의 수정 |
| DELETE | /lectures | 강의 삭제 |

### 강의 등록

**POST** `/lectures`

```json
{
  "title": "강의 제목",
  "description": "강의 설명"
}
```

### 강의 수정

**PUT** `/lectures`

```json
{
  "id": 1,
  "title": "수정된 강의 제목",
  "description": "수정된 강의 설명"
}
```

### 강의 삭제

**DELETE** `/lectures`

```json
{
  "id": 1
}
```

### 강의 목록

**GET** `/lectures`

```json
[
  {
    "id": 1,
    "title": "강의 제목",
    "description": "강의 설명"
  }
]
```

## 기술 스택

- Java
- Spring (직접 구현)
- Tomcat (내장)

## 실행 방법

```bash
./gradlew build
./gradlew run
```

서버는 기본적으로 `http://localhost:8080` 에서 실행됩니다.
