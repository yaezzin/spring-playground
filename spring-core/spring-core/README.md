# 🌱 spring-core-summary

<img width="500" alt="스크린샷 2022-09-28 오후 5 08 02" src="https://user-images.githubusercontent.com/97823928/192724685-59498521-0756-454b-a168-9b087372bee6.png">

### 기존 코드 

```java
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();
    // 할인 정책을 변경하려면 OrderServiceImpl 코드르 변경해주어야 함
    // private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy(); 

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        // orderServiceImpl 입장에서는 할인에 대한 것은 모름 -> 할인에 대한 변경이 생기면 할인쪽만 변경하면 됨 (단일 책임 원칙 ok)
        int discountPrice = discountPolicy.discount(member, itemPrice); // 할인될 금액 리턴
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
```
* OrderServiceImpl의 경우 구현체를 선택할 수 있음 : ```FixDiscountPolicy``` or ```RateDiscountPolicy```
* 즉, ```추상화```(DiscountPolicy 인터페이스)와 ```구체화```(MemoryMemberRepository) 모두 의존중 → DIP 위반
* 할인 정책을 변경하려면 OrderServiceImpl 코드르 변경해주어야 함 → OCP 위반

### 인터페이스에만 의존하도록 코드 변경

```java
public class OrderServiceImpl implements OrderService {
    // private final MemberRepository memberRepository = new MemoryMemberRepository();
    // private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
    private DiscountPolicy discountPolicy;
    private final MemberRepository memberRepository;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
}
```
* OrderServiceImpl은 DiscountPolicy만 의존 (Fix, Rate에 의존하지 않음)
* 인터페이스에만 의존하도록 설계되었으나 구현체가 없기 때문에 Null Pointer Exception이 발생함
* 그러므로 OrderServiceImpl에 DiscountPolicy 구현 객체를 대신 생성하고 주입해주는 별도의 설정 클래스가 필요함 → AppConfig

### AppConfig

```java
public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }
    
    public OrderService orderService() {
        return new OrderServiceImpl(
                new MemoryMemberRepository(),
                new FixDiscountPolicy()); // new RateDiscountPolicy() → AppConfig(구성영역)만 변경해주면 됨
    } 
}
```
* OrderServiceImpl 입장에서 생성자를 통해 어떤 구현 객체가 주입될지 알 수 없음
* OrderServiceImpl의 생성자를 통해서 어떤 구현 객체을 주입할지는 오직 외부( AppConfig )에서 결정
* OrderServiceImpl은 기능을 실행하는 책임만 지면 된다!

### 스프링으로 전환

```java
public class MemberApp {
    public static void main(String[] args) {
     // AppConfig appConfig = new AppConfig();
     // MemberService memberService = appConfig.memberService();
     // 스프링 컨테이너 생성
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
    }
}
```
* appConfig.memberService()처럼 직접 호출할 필요 X
* 스프링 컨테이너 사용 시 @Configuration이 붙은 AppConfig를 설정정보로 사용하고, @Bean이 붙은 메서드를 모두 호출하여 스프링 컨테이너에 등록
* applicationContext.getBean()을 사용하면 스프링 컨테이너에서 스프링 빈을 찾아 사용 가능해짐!

#### 과정 
  1. 스프링 컨테이너 생성 : ```new AnnotationConfigApplicationContext(AppConfig.class)```
  2. 스프링 빈 등록 : AppConfig의 메서드에 @Bean을 붙여 주면 스프링 컨테이너는 파라미터로 넘어온 설정 클래스 정보를 통해 스프링 빈을 등록
  3. 의존 관계 주입

  <img width="500" alt="스크린샷 2022-09-29 오후 4 44 18" src="https://user-images.githubusercontent.com/97823928/192971147-a85fefec-9951-42e6-b3a0-9244cc45e655.png">

### BeanDefinition

<img width="500" alt="스크린샷 2022-09-29 오후 4 48 57" src="https://user-images.githubusercontent.com/97823928/192972134-2a38ac4e-e03c-4834-ae16-b004cfd41b6d.png">

스프링 컨테이너는 자바 코드, XML, groovy 등 다양한 설정 형식을 지원하는 이유는 BeanDefinition이라는 추상화 때문!
* XML, 자바 코드 등을 읽어서 단지 BeanDefinition을 만들면 됨. 스프링 컨테이너는 XML인지 자바 코드인지 알 필요 없이 그냥 Bean Defincition만 알면 됨
* BeanDefinition을 빈 설정 메타정보라고 하는데, @Bean 당 각각 하나씩 메타 정보가 생성됨
* 스프링 컨테이너는 이 메타 정보를 기반으로 스프링 빈을 생성함

### 싱글톤 패턴

<img width="500" alt="스크린샷 2022-09-29 오후 5 37 30" src="https://user-images.githubusercontent.com/97823928/192983082-87875f40-39bd-45b7-8e80-ce2b3d13ddb8.png">

```text
AppConfig appConfig = new AppConfig();

//1. 조회: 호출할 때 마다 객체를 생성
MemberService memberService1 = appConfig.memberService();

//2. 조회: 호출할 때 마다 객체를 생성
MemberService memberService2 = appConfig.memberService();

//참조값이 다름
System.out.println("memberService1 = " + memberService1); 
System.out.println("memberService2 = " + memberService2);       
```

* 스프링 없는 순수한 DI 컨테이너인 AppConfig는 요청을 할 때 마다 객체를 새로 생성
* 이러한 방식은 메모리 낭비가 심하므로 객체를 딱 1개만 생성하고 공유하도록 설계하면 된다 -> 싱글톤 패턴

``` java
public class SingletonService {

//1. static 영역에 객체를 딱 1개만 생성해둔다.
private static final SingletonService instance = new SingletonService();

//2. public으로 열어서 객체 인스터스가 필요하면 이 static 메서드를 통해서만 조회하도록 허용한다.
public static SingletonService getInstance() {
    return instance;
}

//3. 생성자를 private으로 선언해서 외부에서 new 키워드를 사용한 객체 생성을 못하게 막는다. 
private SingletonService() {
}
```


### 싱글톤 컨테이너

<img width="500" alt="스크린샷 2022-09-29 오후 5 41 14" src="https://user-images.githubusercontent.com/97823928/192984015-1fefb62b-3070-436a-9202-ad08eae8ef9e.png">

* 스프링 컨테이너는 싱글턴 패턴을 적용하지 않아도, 객체 인스턴스를 싱글톤으로 관리한다.
* 싱글톤 패턴이든 싱글톤 컨테이너든 객체를 하나만 생성해서 클라이언트가 같은 객체를 공유하는 방식이므로 ```stateless```하게 설계해야함 

```java
public class StatefulService {

    private int price; // 상태를 유지하는 필드

    public void order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        this.price = price; // 여기가 문제임!
    }

    public int getPrice() {
        return price;
    }
}

class StatefulServiceTest {
    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // ThreadA : A 사용자가 10000원 주문
        statefulService1.order("userA", 10000);

        // ThreadB : B 사용자가 20000원 주문
        statefulService2.order("useB", 20000);

        int price = statefulService1.getPrice();
        System.out.println("price = " + price);
        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }
}
```
* StatefulService의 price 필드는 공유되는 필드이므로 특정 클라이언트가 값을 변경하게 됨 (this.price = price;)
* 사용자A의 주문 금액은 10000원이지만, 싱글톤이기 때문에 ThreadB가 사용자B 코드를 호출함에 따라 20000원으로 변경됨 → 무상태로 설계하자

```java
public class StatefulService {
    public int order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        return price; // 아예 가격을 넘겨버리자
    }
}
```

### @Configuration과 싱글톤

```java
@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(
                memberRepository(),
                discountPolicy());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new FixDiscountPolicy();
    }
}
```
AppConfig에서 드는 의문 : memberRepository는 총 3번 호출되어 싱글톤이 깨지는거 아닌가 ...?
* 스프링 컨테이너가 스프링 빈에 등록하기 위해 @Bean이 붙어있는 memberRepository() 호출
* memberSerivce 빈을 만드는 코드에서 memberRepository() 호출 → 자동적으로 MemoryMemberRepository() 호출
* orderSerivce 빈을 만드는 코드에서도 memberRepository() 호출 → 동일하게 MemoryMemberRepository() 호출
* 하지만 3번이 아닌 1번이 호출됨.. 왜?


``` java
bean = class hello.core.AppConfig$$EnhancerBySpringCGLIB$$bd479d70
// 순수한 클래스라면 class hello.core.AppConfig로 출력되어야 함
```

* AppConfig 스프링빈을 조회해서 클래스 정보를 출력해보면 클래스명이 복잡하게 되어짐
* 스프링이 CGLIB라는 바이트코드 조작 라이브러리를 사용해서 AppConfig 클래스를 상속받은 **임의의 다른 클래스를 만들고, 그 다른 클래스를 스프링 빈으로 등록함**

<img width="500" alt="스크린샷 2022-09-30 오전 1 45 49" src="https://user-images.githubusercontent.com/97823928/193090426-72c805ab-b14f-45c9-ac23-4c108b217a81.png">

* @Bean이 붙은 메서드마다 스프링 빈이 이미 존재하면 존재하는 빈을 반환하고, 스프링 빈이 없으면 생성해서 스프링 빈으로 등록하고 반환하는 코드가 동적으로 만들어짐 → 싱글톤 보장 O
* @Configuration 없이 @Bean만 붙이게 되면 MemberRepository가 총 3번 호출되고 서로 다른 인스턴스가 만들어짐 → 싱글톤 보장 X

### 컴포넌트 스캔

```java
@Configuration
@ComponentScan( 
        // 기존의 AppConfig가 자동으로 등록되지 않도록 @Configuration이 붙은 것들은 제외하도록 함!
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
    // 빈으로 등록된 클래스가 하나도 없다!
}
```
스프링은 설정 정보가 없어도 ```자동으로 스프링 빈을 등록```하는 컴포넌트 스캔이라는 기능을 제공함
* 메서드 하나하나마다 @Bean을 붙이지 않아도 됨!
* 말 그대로 ```@Component```가 붙은 클래스를 스캔해서 스프링빈으로 등록해줌
* AppConfig에서는 @Bean을 통해 직접 설정 정보를 작성하고 의존관계를 직접 명시함
* 하지만 이제 설정 정보 자체가 없으므로 ```@Autowired```를 통해 의존관계를 자동으로 주입해야함

### 빈충돌

수동빈과 자동빈이 충돌하는 경우 수동빈이 우선권을 가짐 -> 수동빈이 자동빈을 오버라이딩함

<img width="800" alt="스크린샷 2022-09-30 오후 3 18 22" src="https://user-images.githubusercontent.com/97823928/193203390-629a95bc-ec68-434f-8d68-3ca20c386632.png">

하지만 버그가 발생할 경우가 많으므로 스프링부트는 수동 빈 등록과 자동 빈 등록이 충돌나면 오류가 발생하도록 기본 값을 바꾸었음 (스프링부트를 실행해야 함)

<img width="800" alt="스크린샷 2022-09-30 오후 3 20 01" src="https://user-images.githubusercontent.com/97823928/193203608-d9881042-0ba1-4411-bf8d-96bb4df1ff78.png">

### 의존관계 주입 - 생성자 주입을 사용해라 

의존관계 주입은 한번 일어나면 애플리케이션 종료시점까지 의존관계를 변경할 일이 없고, 변하면 안된다. (불변)
* 수정자 주입을 사용하면 메서드를 public으로 열어두어야 하므로 누군가에 의해 변경될 가능성이 높다
* 또한 생성자 주입을 사용하면 주입 데이터를 누락했을 때 컴파일 오류가 발생함 (Null Point Exception가 X) → 그래서 어떠한 값을 주입해야할지 바로 알 수 있음
* 생성자 주입을 사용하면 final을 사용할 수 있음 → 생성자에서 혹시라도 값이 설정되지 않는 오류를 컴파일 시점에 막아줌
  * ```java: variable discountPolicy might not have been initialized```
  * ```@RequiredArgsConstructor```을 사용하면 final이 붙은 필드를 모아서 생성자를 자동으로 만들어줌

### 스프링빈의 라이프사이클

스프링 컨테이너 생성 → 스프링 빈 생성 → 의존관계 주입 → 초기화 콜백 → 사용 → 소멸 전 콜백 → 종료

* 스프링빈은 객체를 생성하고 의존관계 주입이 다 끝난 다음에야 필요한 데이터를 사용할 준비를 마치므로 초기화 작업은 의존관계 주입이 모두 완료되고 호출해야함
* 스프링은 의존관계 주입이 완료되면 스프링 빈에게 콜백 메서드를 통해서 초기화 시점을 알려줌
* 또한 스프링 컨테이너가 종료되기 직전에 소멸 콜백을 줌

### 초기화 콜백

방법 1 : ```implements InitializingBean, DisposableBean```
* InitializingBean은 afterPropertiesSet() 메서드로 초기화를 지원
* DisposableBean은 destroy() 메서드로 소멸을 지원
* 이 인터페이스는 스프링 전용 인터페이스므로 해당 코드가 스프링 전용 인터페이스에 의존하므로 메서드 이름 변경을 할 수 없으며, 외부 라이브러리에 적용 불가

방법 2 : 빈등록 초기화, 소멸 메서드 지정
* @Bean(initMethod = "init", destroyMethod = "close")
* 스프링 빈이 스프링 코드에 의존하지 X + 설정 정보를 사용하기 때문에 외부라이브러에도 초기화, 종료 메서드를 작성할 수 있음
* destroyMethod의 속성의 경우 기본값이 inferred(추론)이므로 close, shutdown 같은 메서드를 자동으로 호출

방법 3 : @PostConstruct, @PreDestory
* 최신 스프링에서 가장 권장하는 방법 
* 패키지 : ```javax.annotation.PostConstruct``` 
  * 스프링 종속적 x, JSR-250 라는 자바 표준
  * 그러므로 스프링이 아닌 다른 컨테이너에서도 작동함
* 이 애노테이션을 사용하되, 코드를 고칠 수 없는 외부 라이브러리를 초기화, 종료해야한다면 방법2를 사용하자 

### 빈 스코프

#### 1. 싱글톤
* 기본 스코프, 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프

```java
@Test
public class SingletonTest {
    @Test  
    public void singletonBeanFind() {
      SingletonBean singletonBean1 = ac.getBean(SingletonBean.class);
      SingletonBean singletonBean2 = ac.getBean(SingletonBean.class);
      // 싱글톤이므로 같은 인스턴스를 반환
      System.out.println("SingletonTest1 = " + singletonBean1);
      System.out.println("SingletonTest2 = " + singletonBean2);
      assertThat(singletonBean1).isSameAs(singletonBean2);
    }
}
```

<img width="800" alt="스크린샷 2022-10-04 오후 4 38 07" src="https://user-images.githubusercontent.com/97823928/193761978-0bcd554f-7ec1-43ee-809e-af80212b9b02.png">

* 초기화 메서드 실행 → 빈 조회 → 종료 메서드 호출

#### 2. 프로토타입

<img width="500" alt="스크린샷 2022-10-04 오후 4 35 25" src="https://user-images.githubusercontent.com/97823928/193761469-76c1ba06-c35c-418d-bf2e-a4cfb50fda6b.png">

* 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입까지만 관여하고 더는 관리하지 않는 매우 짧은 범위의 스코프

```java
public class PrototypeTest {

  @Test
  void prototypeBeanFind() {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

    System.out.println("find prototypeBean1");
    PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);

    System.out.println("find prototypeBean2");
    PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
    System.out.println("prototypeBean1 = " + prototypeBean1);
    System.out.println("prototypeBean2 = " + prototypeBean2);
    assertThat(prototypeBean1).isNotSameAs(prototypeBean2);

    ac.close();
  }

  @Scope("prototype")
  // @Scope을 사용하면 @Component가 없어도 스프링 빈으로 등록됨
  static class PrototypeBean {

    @PostConstruct
    public void init() {
      System.out.println("PrototypeBean.init");
    }

    @PreDestroy
    public void destroy() {
      System.out.println("PrototypeBean.destroy");
    }
  }
}
```

<img width="570" alt="스크린샷 2022-10-04 오후 4 34 40" src="https://user-images.githubusercontent.com/97823928/193761318-6f600609-d3bb-48ff-9df0-5109bb76b12c.png">

|싱글톤| 프로토 타입|
|----|-----|
|스프링 컨테이너 생성 시점에 초기화 메서드 실행|스프링 컨테이너에서 빈을 조회할 때 스코프빈이 생성되고, 초기화 메서드도 실행됨|
|항상 같은 인스턴스를 반환|조회할 때마다 다른 스프링빈을 반환|
|스프링컨테이너가 관리하므로 종료될떄 빈의 종료 메서드가 호출됨| 스프링 컨테이너가 생성과 의존관계 주입, 초기화까지만 관리하고 더이상 관리하지 않음 → @PreDestory 메서드 실행X|

#### 3. 웹 스코프

<img width="500" alt="스크린샷 2022-10-05 오후 5 37 04" src="https://user-images.githubusercontent.com/97823928/194017771-11960b27-806e-4682-8781-16436abf8c2d.png">

* request: Http 웹 요청이 들어오고 나갈때 까지 유지되는 스코프, 각각의 HTTP 요청마다 별도의 빈 인스턴스가 생성되고, 관리됨
* session: 웹 세션이 생성되고 종료될 때 까지 유지되는 스코프
* application: 웹의 서블릿 컨텍스트와 같은 범위로 유지되는 스코프
* websocket : 웹소켓과 동일한 생명주기를 가지는 스코프



### 프로토타입 스코프 with 싱글톤 빈

<img width="500" alt="스크린샷 2022-10-04 오후 5 10 38" src="https://user-images.githubusercontent.com/97823928/193768275-c3292e0a-a278-49c7-89d9-fde42dbbf728.png">

* ClientBean은 싱글톤이므로 스프링 컨테이너 생성 시점에 함께 생성되고, 의존관계도 주입됨
  * 1 ) clientBean이 의존관계 자동 주입을 사용하여, 주입 시점에 스프링컨테이너에 프로토타입 빈을 요청함
  * 2 ) 스프링 컨테이너는 프로토타입 빈을 생성해서 clientBean에 반환 
  * clientBean은 프로토토타입빈을 내부 필드에 보관 (정확히는 참조값)
  
<img width="500" alt="스크린샷 2022-10-04 오후 5 18 28" src="https://user-images.githubusercontent.com/97823928/193769861-6dfbf43d-473d-4a88-94a3-f75a2f3729fd.png">

* 클라이언트 A는 clientBean을 스프링컨테이너에 요청해서 받음
  * 3 ) 클라이언트 A가 clientBean.logic()을 호출
  * 4 ) clientBean은 프로토타입빈의 addCount()를 호출해서 프로토타입의 count 값을 1 증가시킴
  
<img width="500" alt="스크린샷 2022-10-04 오후 5 32 54" src="https://user-images.githubusercontent.com/97823928/193772872-1879d88b-09cc-436c-8cd8-b46fd06c8e4e.png">

* 클라이언트 B는 clientBean을 스프링컨테이너에 요청해서 받음
* **clientBean이 내부에 가지고 있는 프로토타입빈은 이미 과거에 주입이 끝난 빈**, 사용할때마다 새로된 것이 X
  * 5 ) 클라이언트 A가 clientBean.logic()을 호출
  * 6 ) clientBean은 프로토타입빈의 addCount()를 호출해서 프로토타입의 count 값을 1 증가시킴 -> count 값이 2가 됨

#### 문제는 프로토타입 빈을 주입 시점에만 새로 생성하고, 사용할 때마다 새로 생성해서 사용하는 것이 아니다!
* 싱글톤빈은 생성 시점에만 의존관계 주입을 받기 때문에 프로토타입 빈이 새로 생성되기는 하나, 싱글톤 빈과 함께 계속 유지되는 것이 문제이다.

### Provider을 사용하여 문제 해결

방법 1 : 싱글톤 빈이 프로토타입을 사용할 때 마다 스프링컨테이너에 새로 요청    
* 문제점 : 스프링의 애플리케이션 컨텍스트 전체를 주입받게 되면, 스프링 컨테이너에 종속적인 코드가 되고, 단위 테스트도 어려워짐

```java
static class ClientBean {
    @Autowired
    private ApplicationContext ac;

    public int logic() { // 로직을 호출할때마다 스프링컨테이너에 새로 프로토타입빈을 요청하자!
        // 의존관계를 외부에서 주입받는 것이 아니라 직접 필요한 의존관계를 찾음 : DL(의존관계 탐색)
        PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
        prototypeBean.addCount();
        int count = prototypeBean.getCount();
        return count;
    }
}
```

방법 2 : ```ObjectFactory```, ```ObjectProvider```
* ObjectProvider 의 getObject() 를 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환한다. (DL)
  * ObjectFactory: 기능이 단순, 별도의 라이브러리 필요 없음, 스프링에 의존
  * ObjectProvider: ObjectFactory 상속, 옵션, 스트림 처리등 편의 기능이 많고, 별도의 라이브러리 필요 없음, 스프링에 의존

```java
static class ClientBean {
    @Autowired
    private ObjectProvider<PrototypeBean> prototypeBeanProvider;
    
    public int logic() {
        PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
        prototypeBean.addCount();
        int count = prototypeBean.getCount();
        return count;
    }
}
```

방법 3 : ```JSR-330 Provider```

```java
//implementation 'javax.inject:javax.inject:1' gradle 추가 필수 @Autowired
private Provider<PrototypeBean> provider;
    
public int logic() {
    PrototypeBean prototypeBean = provider.get();
    prototypeBean.addCount();
    int count = prototypeBean.getCount();
    return count;
}
```
* get()메서드를 사용해서 해당 빈을 찾아서 반환하나 별도의 라이브러리가 필요하다.
  * ObjectProvider는 DL을 위한 편의 기능을 많이 제공해주고 스프링 외에 별도의 의존관계 추가가 필요 없기 때문에 편리하다. 
  * 만약 코드를 스프링이 아닌 다른 컨테이너에서도 사용할 수 있어야 한다면 JSR-330 Provider를 사용해야한다.

### 스코프와 프록시

스프링 애플리케이션을 실행하는 시점에 싱글톤 빈은 생성해서 주입이 되나, request 스코프 빈은 생성되지 않는다!

방법 1 : ObjectProvider 사용  
방법 2 : 프록시 방법 사용 

```java
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {
    ...
}
```
* MyLooger의 가짜 프록시 클래스를 만들어두고 request와 상관없이 가짜 프록시를 다른 빈에 미리 주입할 수 있음!!
* proxyMode를 설정하면 스프링컨테이너는 CGLIB라는 라이브러리로 내 클래스를 상속 받은 가짜 프록시 객체를 만들어서 주입함!
* 그러므로 진짜 객체 조회를 꼭 필요한 시점까지 지연처리 가능해짐

<img width="500" alt="스크린샷 2022-10-05 오후 6 33 26" src="https://user-images.githubusercontent.com/97823928/194029292-171401e9-9dc1-4a58-9d3a-420238e49815.png">

* CGLIB라는 라이브러리로 내 클래스를 상속 받은 가짜 프록시 객체를 만들어서 주입
* 이 가짜 프록시 객체는 실제 요청이 오면 그때 내부에서 실제 빈을 요청하는 위임 로직이 들어있으며 단순히 싱글톤처럼 동작한다
* 마치 싱글톤을 사용하는 것 같지만 다르게 동작하기 때문에 이러한 scope는 꼭 필요한 곳에서만 최소화해서 사용하자!
