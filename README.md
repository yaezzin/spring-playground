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

```java
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

```java
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


```java
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
