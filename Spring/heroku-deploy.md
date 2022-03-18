# Spring 프로젝트 Heroku 배포하기

### 1. 헤로쿠 회원가입

### 2. 헤로쿠 CLI 설치

```terminal
brew install heroku/brew/heroku
```

### 3. 헤로쿠 로그인 
```
heroku login
``` 

### 4. 해당 경로 이동

```
cd Desktop/heroku
``` 
스프링 프로젝트를 담을 폴더 생성 후, 프로젝트를 복사하여 붙여넣음

### 5. 깃 초기화
```
git init
```

### 6. 헤로쿠 깃과 연결
```
heroku git:remote -a <app-name>
```

### 7. add commit push!
```git add .```  
```git commit -am "commit comment" ```  
```git push heroku main```

### 8. 화면을 열자!
```heroku open```


## 배포하면서 발생한 오류들

### JDK 버전

스프링 프로젝트는 Java 11을 쓰고 있고, 헤로쿠는 1.8 버전을 사용하기 때문에 발생하는 오류  
✨```루트 디렉토리```✨에 ```system.properties```를 만든 후 ```java.runtime.version=11```를 넣어주자!  
루트 디렉토리가 아니라 application.properties에 넣어서 몇 시간을 삽질했다^^ 하하..     

### Procfile
Procfile을 루트 디렉토리에 생성하고
```web: java -jar build/libs/나의프로젝트이름-0.0.1-SNAPSHOT.jar```을 넣어준다.  
이번에도 루트 디렉토리에 생성해야함을 주의하고, Procfile의 p는 무조건 대문자로..! 안그러면 에러남.


