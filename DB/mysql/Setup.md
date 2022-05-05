# MySQL 삽질기

## 1. 설치

<img width="82" alt="스크린샷 2022-05-05 오후 10 54 01" src="https://user-images.githubusercontent.com/97823928/166938503-829b99dd-e2c8-459e-9a84-75fd787e473e.png">

oracle에 로그인하고, 각자의 운영체제에 맞는 MySQL을 설치해주면 됨  
그러면 <시스템 환경설정>에 들어가면 다음과 같이 고래가 등장함 ㅎㅎ (MacOS 기준)

## 2. 실행

```
$ cd /usr/local/mysql/bin
$ ./mysql -u root -p
```

위와 같이 터미널에 입력하면 

```
mysql >
```

이렇게 변함 ㅎㅎ

## 3. 명령어

간단한 테이블을 생성해보자

```
$ create database 원하는db이름;
```

현재 데이터 베이스를 보여준다 ㅎㅎ

```
$ show databases;
```

## 4. 스프링부트 연동하기

#### application.properties

```java
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/example?serverTimezone=UTC&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=1234
```

#### 적용하기

* 우측의 Database > + 버튼 > Data Source > MySQL 클릭
* 커넥션 정보를 작성하고 Test Connection
