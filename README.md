# 🌱 spring-core

<img width="600" alt="스크린샷 2022-09-28 오후 5 08 02" src="https://user-images.githubusercontent.com/97823928/192724685-59498521-0756-454b-a168-9b087372bee6.png">

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
* OrderServiceImpl 입장에서 생성자를 통해 어떤 구현 객체가 들어올지(주입될지)는 알 수 없음
* OrderServiceImpl 의 생성자를 통해서 어떤 구현 객체을 주입할지는 오직 외부( AppConfig )에서 결정
* OrderServiceImpl 은 기능을 실행하는 책임만 지면 된다!
