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

싱글톤 패턴의 문제점 
* 

### 싱글톤 컨테이너

<img width="500" alt="스크린샷 2022-09-29 오후 5 41 14" src="https://user-images.githubusercontent.com/97823928/192984015-1fefb62b-3070-436a-9202-ad08eae8ef9e.png">

* 스프링 컨테이너는 싱글턴 패턴을 적용하지 않아도, 객체 인스턴스를 싱글톤으로 관리한다.
* 싱글톤 패턴이든 싱글톤 컨테이너든 객체를 하나만 생성해서 클라이언트가 같은 객체를 공유하는 방식이므로 ```stateless```하게 설계해야함 

