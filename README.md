## 요구사항
- 사용자는 텍스트로 된 할일을 추가할 수 있다.
- 할일 추가 시 다른 할일들을 참조 걸 수 있다.
- 참조는 다른 할일의 id를 명시하는 형태로 구현한다.
- 사용자는 할일을 수정할 수 있다.
- 사용자는 할일 목록을 조회할 수 있다.
- 조회시 작성일, 최종수정일, 내용이 조회 가능하다.
- 할일 목록은 페이징 기능이 있다.
- 사용자는 할일을 완료처리 할 수 있다.
- 완료처리 시 참조가 걸린 완료되지 않은 할일이 있다면 완료처리할 수 없다.

## 설치 및 실행
- 소스코드 빌드 및 실행
  - http://localhost:8080
  - http://localhost:8080/h2-console (db 확인)

```
git clone https://github.com/ggulmool/todo.github
cd todo
mvn clean test spring-boot:run 또는 ./mvnw clean test spring-boot:run
```

- 등록된 사용자 정보 (아이디/패스워드/인증키)
  - REST API 요청시 인증키 필요
    - 회원가입/로그인 기능이 없으므로 미리 등록된 사용자 정보로 테스트
    - 화면단 사용자 변경
       - src/main/resources/static/js/todo.authInterceptor.js
       - var encodedString = btoa("user2:abcd0987"); // id:패스워드 변경
  - user1/test1234 (dXNlcjE6dGVzdDEyMzQ=)
  - user2/abcd0987 (dXNlcjI6YWJjZDA5ODc=)

- 실행화면
![todo](./images/todo.png)
![todo_h2_console](./images/todo_h2_console.png)

## 구현설명
- 백엔드(RestAPI)
  - SpringBoot, JPA기반 구현
  - 도메인, 레포지토리, 서비스(Mockito사용) 단위테스트
  - Todo도메인 설계
    - Todo엔티티를 ManyToMany관계의 self참조로 설계
    - 참목록(parentTodo), 참조되고있는목록(refTodo)
  - HTTP Basic 인증 처리
    - 스프링의 HandlerInterceptor를 구현하여 Controller를 호출하기 전에 http 헤더의 Authorization값을 확인하여 유효한 사용자인지 확인
    - 세션을 사용하지 않고 REST API의 특징인 Stateless로 구현.
  - 예외처리
    - 예외 발생시 RestResponseExceptionHandler를 통해 에러코드와 에러 메시지 전달
  - 페이징 처리
- 프론트엔드
  - AngularJS 기반 구현
  - Ajax 처리
    - Ajax 요청하기전 http 헤더에 Authorization값 생성하여 전달
    - Ajax 페이징 처리
    - 로딩바 처리
  - 참조할일 입력시 자동완성 기능

## Rest API 구현목록
### HTTP Basic Authenticatoin 인증 처리
사용자id와 비밀번호를 http헤더에 Base64인코딩 형태로 넣어서 인증 요청하는 방식
- "사용자id:비밀번호"문자열을 Base64 인코딩을 해서 'Authorization'헤더 값에 담겨 서버에 인증 요청
ex) "user1:abcd1234" -> Authorization : Basic dXNlcjE6dGVzdDEyMzQ=

```
user1/test1234 (Basic dXNlcjE6dGVzdDEyMzQ=)
user2/abcd0987 (Basic dXNlcjI6YWJjZDA5ODc=)
```

### 할일목록 조회
사용자의 할일목록을 조회(Basic인증키에 해당하는 사용자 정보를 참조)
```
GET /api/todos HTTP/1.1
Host: localhost:8080/api/todos
Authorization: Basic {인증키값}
```

> 실행
```
curl -v \
GET 'http://localhost:8080/api/todos?page=1&size=10' \
-H 'Authorization: Basic dXNlcjE6dGVzdDEyMzQ='
```
- page : 페이지 번호(기본값:1)
- size : 한페이지에 보여줄 데이터 갯수(기본값:10)

### 참조할일 목록조회
특정할일의 참조할일 목록을 조회
```
GET /api/todos/{todoId}/parents HTTP/1.1
Host: localhost:8080
Authorization: Basic {인증키값}
```

> 실행
```
curl -v \
GET 'http://localhost:8080/api/todos/4/parents' \
-H 'Authorization: Basic dXNlcjE6dGVzdDEyMzQ='
```
- todoId : 조회하고자 하는 기등록된 id값

### 할일등록
사용자의 할일을 등록
RequestBody에 등록할 데이터를 담아서 보낸다.
```
POST /api/todos HTTP/1.1
Host: localhost:8080
Authorization: Basic {인증키값}
Content-Type: application/json; charset=UTF-8
```

> 실행
```
curl -v -X \
POST 'http://localhost:8080/api/todos' \
-H 'Authorization: Basic dXNlcjE6dGVzdDEyMzQ=' \
-H 'Content-Type: application/json; charset=UTF-8' \
--data '{"contents":"거실청소","parentIds":[1,3]}'
```
- contents : 등록할 할일
- parentIds : 참조할일의 id배열

### 할일수정
사용자의 할일을 수정
RequestBody에 수정할 데이터를 담아서 보낸다.
```
PUT /api/todos/api/todos/{todoId} HTTP/1.1
Host: localhost:8080
Authorization: Basic {인증키값}
Content-Type: application/json; charset=UTF-8
```

> 실행
```
curl -v -X \
PUT 'http://localhost:8080/api/todos/4' \
-H 'Authorization: Basic dXNlcjE6dGVzdDEyMzQ=' \
-H 'Content-Type: application/json; charset=UTF-8' \
--data '{"contents":"거실청소","parentIds":[1,2]}'
```

- todoId : 수정하고자 하는 기등록된 id값
- contents : 수정할 할일
- parentIds : 참조할일의 id배열


### 할일완료
```
PUT /api/todos/{todoId}/done HTTP/1.1
Host: localhost:8080
Authorization: Basic {인증키값}
```

> 실행
```
curl -v -X\
PUT 'http://localhost:8080/api/todos/4/done' \
-H 'Authorization: Basic dXNlcjE6dGVzdDEyMzQ='
```
- todoId : 완료하고자 하는 기등록된 id값

## 사용한 라이브러리 및 오픈소스
- Java8
- SpringBoot
- JPA
- H2DB
- Thymeleaf
- Lombok
- Maven
- Angularjs
- Bootstrap