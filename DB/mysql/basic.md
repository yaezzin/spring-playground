# MySQL

```by 생활코딩```

## 1. MySQL의 구조

<img width="800" alt="스크린샷 2022-06-26 오전 12 39 02" src="https://user-images.githubusercontent.com/97823928/175780939-d917547b-bfea-44d0-bd69-dc6ff3487a67.png">

MySQL의 구조는 크게 ```표```, ```데이터베이스(스키마)```, ```데이터베이스 서버```로 구성됨


|유형|설명|
|:---:|-------|
|표|- 데이터를 저장하는 곳|
|데이터베이스|- 표의 수가 증가하게 되면 이것들을 정리, 그룹핑할 필요성이 증대됨 </br> - 연관된 표들을 그룹핑하여 연관되어있지 않은 표들과 분리할 떄 사용하는 폴더 같은 것 </br> - 스키마로도 불림|
|데이터베이스 서버|- MySQL을 설치했다는 것은 데이터베이스 서버를 설치한 것! </br> - 스키마들이 많아지면 데이터베이스 서버에 저장하게 된다|

## 2. 스키마의 사용

1. 데이터베이스 ```생성```

```sql
CREATE DATABASE 데이터베이스이름;
```

2. 데이터베이스 ```삭제```

```sql
DROP DATABASE 데이터베이스이름;
```

3. 데이터베이스 ```조회```

```sql
SHOW DATABASES:
```

4. 데이터베이스 ```사용```

```sql
USE 데이터베이스이름;
```

## 3. SQL과 테이블 구조

<img width="700" alt="스크린샷 2022-06-26 오전 1 34 39" src="https://user-images.githubusercontent.com/97823928/175782657-3247a2fc-4ae7-44ca-b918-e9fe75b24461.png">

```sql
CREATE TABLE 테이블명(
    -> 컬럼이름 데이터타입(길이) 
    -> id INT(11) NOT NULL AUTO_INCREMNET,
    -> title VARCHAR(100) NOT NULL,
    -> description TEXT NULL,
    -> created DATETIME NOT NULL,
    -> author VARCHAR(30) NULL,
    -> profile VARCHAR(100) NULL
    -> PRIMARY KEY(id)
)
```

## 4. INSERT

```sql
INSERT INTO 테이블명 (컬럼명1, 컬럼명2, ...) VALUES(값1, 값2, ...);
INSERT INTO topic (title, description, created, author, profile) VALUES('MySQL', 'MySQL is ...', NOW(), 'yejin', 'developer');
```
topic 테이블로부터 insert한 데이터를 조회하고 싶으면?

```sql
SELECT * FROM 테이블명;
```

## 5. SELECT

1. 해당 테이블의 모든 행(row)을 출력

```sql
SELECT * FROM 테이블명;
```

2. 설정한 열을 기준으로 데이터를 보고싶다면?

```sql
SELECT 컬럼들의 목록 FROM 테이블명;
SELECT id, title, created, author FROM 테이블명;
```

3. author가 yejin인 데이터만 가져오고 싶다면?

```sql
SELECT id, title, created, autor FROM 테이블명 WHERE author = 'yejin'; 
```

4. 정렬 by 아이디 (내림차순 DESC, 오름차순 ASC) 

```sql
SELECT id, title, created, autor FROM 테이블명 WHERE author = 'yejin' ORDER BY DESC; 
```

5. 데이터를 2건만 조회하고 싶다면?

```sql
SELECT id, title, created, autor FROM 테이블명 WHERE author = 'yejin' ORDER BY DESC LIMIT 2; 
```


## 6. UPDATE

update문(+ delete문)은 WHERE문을 무조건 써줘야 한다!!! id값을 지정해주지 않으면 모두 다 수정이 됨..

```sql
UPDATE 테이블명 SET 컬럼명1 = '수정하려는 값', 컬럼명2 = '수정하려는 값2' WHERE id = 아이디값;
```

## 7. DELETE

```sql
DELETE FROM 테이블명 WHERE id = 2;
```





