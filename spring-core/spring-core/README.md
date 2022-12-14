# ๐ฑ spring-core-summary

<img width="500" alt="แแณแแณแแตแซแแฃแบ 2022-09-28 แแฉแแฎ 5 08 02" src="https://user-images.githubusercontent.com/97823928/192724685-59498521-0756-454b-a168-9b087372bee6.png">

### ๊ธฐ์กด ์ฝ๋ 

```java
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();
    // ํ ์ธ ์ ์ฑ์ ๋ณ๊ฒฝํ๋ ค๋ฉด OrderServiceImpl ์ฝ๋๋ฅด ๋ณ๊ฒฝํด์ฃผ์ด์ผ ํจ
    // private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy(); 

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        // orderServiceImpl ์์ฅ์์๋ ํ ์ธ์ ๋ํ ๊ฒ์ ๋ชจ๋ฆ -> ํ ์ธ์ ๋ํ ๋ณ๊ฒฝ์ด ์๊ธฐ๋ฉด ํ ์ธ์ชฝ๋ง ๋ณ๊ฒฝํ๋ฉด ๋จ (๋จ์ผ ์ฑ์ ์์น ok)
        int discountPrice = discountPolicy.discount(member, itemPrice); // ํ ์ธ๋  ๊ธ์ก ๋ฆฌํด
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
```
* OrderServiceImpl์ ๊ฒฝ์ฐ ๊ตฌํ์ฒด๋ฅผ ์ ํํ  ์ ์์ : ```FixDiscountPolicy``` or ```RateDiscountPolicy```
* ์ฆ, ```์ถ์ํ```(DiscountPolicy ์ธํฐํ์ด์ค)์ ```๊ตฌ์ฒดํ```(MemoryMemberRepository) ๋ชจ๋ ์์กด์ค โ DIP ์๋ฐ
* ํ ์ธ ์ ์ฑ์ ๋ณ๊ฒฝํ๋ ค๋ฉด OrderServiceImpl ์ฝ๋๋ฅด ๋ณ๊ฒฝํด์ฃผ์ด์ผ ํจ โ OCP ์๋ฐ

### ์ธํฐํ์ด์ค์๋ง ์์กดํ๋๋ก ์ฝ๋ ๋ณ๊ฒฝ

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
* OrderServiceImpl์ DiscountPolicy๋ง ์์กด (Fix, Rate์ ์์กดํ์ง ์์)
* ์ธํฐํ์ด์ค์๋ง ์์กดํ๋๋ก ์ค๊ณ๋์์ผ๋ ๊ตฌํ์ฒด๊ฐ ์๊ธฐ ๋๋ฌธ์ Null Pointer Exception์ด ๋ฐ์ํจ
* ๊ทธ๋ฌ๋ฏ๋ก OrderServiceImpl์ DiscountPolicy ๊ตฌํ ๊ฐ์ฒด๋ฅผ ๋์  ์์ฑํ๊ณ  ์ฃผ์ํด์ฃผ๋ ๋ณ๋์ ์ค์  ํด๋์ค๊ฐ ํ์ํจ โ AppConfig

### AppConfig

```java
public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }
    
    public OrderService orderService() {
        return new OrderServiceImpl(
                new MemoryMemberRepository(),
                new FixDiscountPolicy()); // new RateDiscountPolicy() โ AppConfig(๊ตฌ์ฑ์์ญ)๋ง ๋ณ๊ฒฝํด์ฃผ๋ฉด ๋จ
    } 
}
```
* OrderServiceImpl ์์ฅ์์ ์์ฑ์๋ฅผ ํตํด ์ด๋ค ๊ตฌํ ๊ฐ์ฒด๊ฐ ์ฃผ์๋ ์ง ์ ์ ์์
* OrderServiceImpl์ ์์ฑ์๋ฅผ ํตํด์ ์ด๋ค ๊ตฌํ ๊ฐ์ฒด์ ์ฃผ์ํ ์ง๋ ์ค์ง ์ธ๋ถ( AppConfig )์์ ๊ฒฐ์ 
* OrderServiceImpl์ ๊ธฐ๋ฅ์ ์คํํ๋ ์ฑ์๋ง ์ง๋ฉด ๋๋ค!

### ์คํ๋ง์ผ๋ก ์ ํ

```java
public class MemberApp {
    public static void main(String[] args) {
     // AppConfig appConfig = new AppConfig();
     // MemberService memberService = appConfig.memberService();
     // ์คํ๋ง ์ปจํ์ด๋ ์์ฑ
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
    }
}
```
* appConfig.memberService()์ฒ๋ผ ์ง์  ํธ์ถํ  ํ์ X
* ์คํ๋ง ์ปจํ์ด๋ ์ฌ์ฉ ์ @Configuration์ด ๋ถ์ AppConfig๋ฅผ ์ค์ ์ ๋ณด๋ก ์ฌ์ฉํ๊ณ , @Bean์ด ๋ถ์ ๋ฉ์๋๋ฅผ ๋ชจ๋ ํธ์ถํ์ฌ ์คํ๋ง ์ปจํ์ด๋์ ๋ฑ๋ก
* applicationContext.getBean()์ ์ฌ์ฉํ๋ฉด ์คํ๋ง ์ปจํ์ด๋์์ ์คํ๋ง ๋น์ ์ฐพ์ ์ฌ์ฉ ๊ฐ๋ฅํด์ง!

#### ๊ณผ์  
  1. ์คํ๋ง ์ปจํ์ด๋ ์์ฑ : ```new AnnotationConfigApplicationContext(AppConfig.class)```
  2. ์คํ๋ง ๋น ๋ฑ๋ก : AppConfig์ ๋ฉ์๋์ @Bean์ ๋ถ์ฌ ์ฃผ๋ฉด ์คํ๋ง ์ปจํ์ด๋๋ ํ๋ผ๋ฏธํฐ๋ก ๋์ด์จ ์ค์  ํด๋์ค ์ ๋ณด๋ฅผ ํตํด ์คํ๋ง ๋น์ ๋ฑ๋ก
  3. ์์กด ๊ด๊ณ ์ฃผ์

  <img width="500" alt="แแณแแณแแตแซแแฃแบ 2022-09-29 แแฉแแฎ 4 44 18" src="https://user-images.githubusercontent.com/97823928/192971147-a85fefec-9951-42e6-b3a0-9244cc45e655.png">

### BeanDefinition

<img width="500" alt="แแณแแณแแตแซแแฃแบ 2022-09-29 แแฉแแฎ 4 48 57" src="https://user-images.githubusercontent.com/97823928/192972134-2a38ac4e-e03c-4834-ae16-b004cfd41b6d.png">

์คํ๋ง ์ปจํ์ด๋๋ ์๋ฐ ์ฝ๋, XML, groovy ๋ฑ ๋ค์ํ ์ค์  ํ์์ ์ง์ํ๋ ์ด์ ๋ BeanDefinition์ด๋ผ๋ ์ถ์ํ ๋๋ฌธ!
* XML, ์๋ฐ ์ฝ๋ ๋ฑ์ ์ฝ์ด์ ๋จ์ง BeanDefinition์ ๋ง๋ค๋ฉด ๋จ. ์คํ๋ง ์ปจํ์ด๋๋ XML์ธ์ง ์๋ฐ ์ฝ๋์ธ์ง ์ ํ์ ์์ด ๊ทธ๋ฅ Bean Defincition๋ง ์๋ฉด ๋จ
* BeanDefinition์ ๋น ์ค์  ๋ฉํ์ ๋ณด๋ผ๊ณ  ํ๋๋ฐ, @Bean ๋น ๊ฐ๊ฐ ํ๋์ฉ ๋ฉํ ์ ๋ณด๊ฐ ์์ฑ๋จ
* ์คํ๋ง ์ปจํ์ด๋๋ ์ด ๋ฉํ ์ ๋ณด๋ฅผ ๊ธฐ๋ฐ์ผ๋ก ์คํ๋ง ๋น์ ์์ฑํจ

### ์ฑ๊ธํค ํจํด

<img width="500" alt="แแณแแณแแตแซแแฃแบ 2022-09-29 แแฉแแฎ 5 37 30" src="https://user-images.githubusercontent.com/97823928/192983082-87875f40-39bd-45b7-8e80-ce2b3d13ddb8.png">

```text
AppConfig appConfig = new AppConfig();

//1. ์กฐํ: ํธ์ถํ  ๋ ๋ง๋ค ๊ฐ์ฒด๋ฅผ ์์ฑ
MemberService memberService1 = appConfig.memberService();

//2. ์กฐํ: ํธ์ถํ  ๋ ๋ง๋ค ๊ฐ์ฒด๋ฅผ ์์ฑ
MemberService memberService2 = appConfig.memberService();

//์ฐธ์กฐ๊ฐ์ด ๋ค๋ฆ
System.out.println("memberService1 = " + memberService1); 
System.out.println("memberService2 = " + memberService2);       
```

* ์คํ๋ง ์๋ ์์ํ DI ์ปจํ์ด๋์ธ AppConfig๋ ์์ฒญ์ ํ  ๋ ๋ง๋ค ๊ฐ์ฒด๋ฅผ ์๋ก ์์ฑ
* ์ด๋ฌํ ๋ฐฉ์์ ๋ฉ๋ชจ๋ฆฌ ๋ญ๋น๊ฐ ์ฌํ๋ฏ๋ก ๊ฐ์ฒด๋ฅผ ๋ฑ 1๊ฐ๋ง ์์ฑํ๊ณ  ๊ณต์ ํ๋๋ก ์ค๊ณํ๋ฉด ๋๋ค -> ์ฑ๊ธํค ํจํด

``` java
public class SingletonService {

//1. static ์์ญ์ ๊ฐ์ฒด๋ฅผ ๋ฑ 1๊ฐ๋ง ์์ฑํด๋๋ค.
private static final SingletonService instance = new SingletonService();

//2. public์ผ๋ก ์ด์ด์ ๊ฐ์ฒด ์ธ์คํฐ์ค๊ฐ ํ์ํ๋ฉด ์ด static ๋ฉ์๋๋ฅผ ํตํด์๋ง ์กฐํํ๋๋ก ํ์ฉํ๋ค.
public static SingletonService getInstance() {
    return instance;
}

//3. ์์ฑ์๋ฅผ private์ผ๋ก ์ ์ธํด์ ์ธ๋ถ์์ new ํค์๋๋ฅผ ์ฌ์ฉํ ๊ฐ์ฒด ์์ฑ์ ๋ชปํ๊ฒ ๋ง๋๋ค. 
private SingletonService() {
}
```


### ์ฑ๊ธํค ์ปจํ์ด๋

<img width="500" alt="แแณแแณแแตแซแแฃแบ 2022-09-29 แแฉแแฎ 5 41 14" src="https://user-images.githubusercontent.com/97823928/192984015-1fefb62b-3070-436a-9202-ad08eae8ef9e.png">

* ์คํ๋ง ์ปจํ์ด๋๋ ์ฑ๊ธํด ํจํด์ ์ ์ฉํ์ง ์์๋, ๊ฐ์ฒด ์ธ์คํด์ค๋ฅผ ์ฑ๊ธํค์ผ๋ก ๊ด๋ฆฌํ๋ค.
* ์ฑ๊ธํค ํจํด์ด๋  ์ฑ๊ธํค ์ปจํ์ด๋๋  ๊ฐ์ฒด๋ฅผ ํ๋๋ง ์์ฑํด์ ํด๋ผ์ด์ธํธ๊ฐ ๊ฐ์ ๊ฐ์ฒด๋ฅผ ๊ณต์ ํ๋ ๋ฐฉ์์ด๋ฏ๋ก ```stateless```ํ๊ฒ ์ค๊ณํด์ผํจ 

```java
public class StatefulService {

    private int price; // ์ํ๋ฅผ ์ ์งํ๋ ํ๋

    public void order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        this.price = price; // ์ฌ๊ธฐ๊ฐ ๋ฌธ์ ์!
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

        // ThreadA : A ์ฌ์ฉ์๊ฐ 10000์ ์ฃผ๋ฌธ
        statefulService1.order("userA", 10000);

        // ThreadB : B ์ฌ์ฉ์๊ฐ 20000์ ์ฃผ๋ฌธ
        statefulService2.order("useB", 20000);

        int price = statefulService1.getPrice();
        System.out.println("price = " + price);
        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }
}
```
* StatefulService์ price ํ๋๋ ๊ณต์ ๋๋ ํ๋์ด๋ฏ๋ก ํน์  ํด๋ผ์ด์ธํธ๊ฐ ๊ฐ์ ๋ณ๊ฒฝํ๊ฒ ๋จ (this.price = price;)
* ์ฌ์ฉ์A์ ์ฃผ๋ฌธ ๊ธ์ก์ 10000์์ด์ง๋ง, ์ฑ๊ธํค์ด๊ธฐ ๋๋ฌธ์ ThreadB๊ฐ ์ฌ์ฉ์B ์ฝ๋๋ฅผ ํธ์ถํจ์ ๋ฐ๋ผ 20000์์ผ๋ก ๋ณ๊ฒฝ๋จ โ ๋ฌด์ํ๋ก ์ค๊ณํ์

```java
public class StatefulService {
    public int order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        return price; // ์์ ๊ฐ๊ฒฉ์ ๋๊ฒจ๋ฒ๋ฆฌ์
    }
}
```

### @Configuration๊ณผ ์ฑ๊ธํค

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
AppConfig์์ ๋๋ ์๋ฌธ : memberRepository๋ ์ด 3๋ฒ ํธ์ถ๋์ด ์ฑ๊ธํค์ด ๊นจ์ง๋๊ฑฐ ์๋๊ฐ ...?
* ์คํ๋ง ์ปจํ์ด๋๊ฐ ์คํ๋ง ๋น์ ๋ฑ๋กํ๊ธฐ ์ํด @Bean์ด ๋ถ์ด์๋ memberRepository() ํธ์ถ
* memberSerivce ๋น์ ๋ง๋๋ ์ฝ๋์์ memberRepository() ํธ์ถ โ ์๋์ ์ผ๋ก MemoryMemberRepository() ํธ์ถ
* orderSerivce ๋น์ ๋ง๋๋ ์ฝ๋์์๋ memberRepository() ํธ์ถ โ ๋์ผํ๊ฒ MemoryMemberRepository() ํธ์ถ
* ํ์ง๋ง 3๋ฒ์ด ์๋ 1๋ฒ์ด ํธ์ถ๋จ.. ์?


``` java
bean = class hello.core.AppConfig$$EnhancerBySpringCGLIB$$bd479d70
// ์์ํ ํด๋์ค๋ผ๋ฉด class hello.core.AppConfig๋ก ์ถ๋ ฅ๋์ด์ผ ํจ
```

* AppConfig ์คํ๋ง๋น์ ์กฐํํด์ ํด๋์ค ์ ๋ณด๋ฅผ ์ถ๋ ฅํด๋ณด๋ฉด ํด๋์ค๋ช์ด ๋ณต์กํ๊ฒ ๋์ด์ง
* ์คํ๋ง์ด CGLIB๋ผ๋ ๋ฐ์ดํธ์ฝ๋ ์กฐ์ ๋ผ์ด๋ธ๋ฌ๋ฆฌ๋ฅผ ์ฌ์ฉํด์ AppConfig ํด๋์ค๋ฅผ ์์๋ฐ์ **์์์ ๋ค๋ฅธ ํด๋์ค๋ฅผ ๋ง๋ค๊ณ , ๊ทธ ๋ค๋ฅธ ํด๋์ค๋ฅผ ์คํ๋ง ๋น์ผ๋ก ๋ฑ๋กํจ**

<img width="500" alt="แแณแแณแแตแซแแฃแบ 2022-09-30 แแฉแแฅแซ 1 45 49" src="https://user-images.githubusercontent.com/97823928/193090426-72c805ab-b14f-45c9-ac23-4c108b217a81.png">

* @Bean์ด ๋ถ์ ๋ฉ์๋๋ง๋ค ์คํ๋ง ๋น์ด ์ด๋ฏธ ์กด์ฌํ๋ฉด ์กด์ฌํ๋ ๋น์ ๋ฐํํ๊ณ , ์คํ๋ง ๋น์ด ์์ผ๋ฉด ์์ฑํด์ ์คํ๋ง ๋น์ผ๋ก ๋ฑ๋กํ๊ณ  ๋ฐํํ๋ ์ฝ๋๊ฐ ๋์ ์ผ๋ก ๋ง๋ค์ด์ง โ ์ฑ๊ธํค ๋ณด์ฅ O
* @Configuration ์์ด @Bean๋ง ๋ถ์ด๊ฒ ๋๋ฉด MemberRepository๊ฐ ์ด 3๋ฒ ํธ์ถ๋๊ณ  ์๋ก ๋ค๋ฅธ ์ธ์คํด์ค๊ฐ ๋ง๋ค์ด์ง โ ์ฑ๊ธํค ๋ณด์ฅ X

### ์ปดํฌ๋ํธ ์ค์บ

```java
@Configuration
@ComponentScan( 
        // ๊ธฐ์กด์ AppConfig๊ฐ ์๋์ผ๋ก ๋ฑ๋ก๋์ง ์๋๋ก @Configuration์ด ๋ถ์ ๊ฒ๋ค์ ์ ์ธํ๋๋ก ํจ!
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
    // ๋น์ผ๋ก ๋ฑ๋ก๋ ํด๋์ค๊ฐ ํ๋๋ ์๋ค!
}
```
์คํ๋ง์ ์ค์  ์ ๋ณด๊ฐ ์์ด๋ ```์๋์ผ๋ก ์คํ๋ง ๋น์ ๋ฑ๋ก```ํ๋ ์ปดํฌ๋ํธ ์ค์บ์ด๋ผ๋ ๊ธฐ๋ฅ์ ์ ๊ณตํจ
* ๋ฉ์๋ ํ๋ํ๋๋ง๋ค @Bean์ ๋ถ์ด์ง ์์๋ ๋จ!
* ๋ง ๊ทธ๋๋ก ```@Component```๊ฐ ๋ถ์ ํด๋์ค๋ฅผ ์ค์บํด์ ์คํ๋ง๋น์ผ๋ก ๋ฑ๋กํด์ค
* AppConfig์์๋ @Bean์ ํตํด ์ง์  ์ค์  ์ ๋ณด๋ฅผ ์์ฑํ๊ณ  ์์กด๊ด๊ณ๋ฅผ ์ง์  ๋ช์ํจ
* ํ์ง๋ง ์ด์  ์ค์  ์ ๋ณด ์์ฒด๊ฐ ์์ผ๋ฏ๋ก ```@Autowired```๋ฅผ ํตํด ์์กด๊ด๊ณ๋ฅผ ์๋์ผ๋ก ์ฃผ์ํด์ผํจ

### ๋น์ถฉ๋

์๋๋น๊ณผ ์๋๋น์ด ์ถฉ๋ํ๋ ๊ฒฝ์ฐ ์๋๋น์ด ์ฐ์ ๊ถ์ ๊ฐ์ง -> ์๋๋น์ด ์๋๋น์ ์ค๋ฒ๋ผ์ด๋ฉํจ

<img width="800" alt="แแณแแณแแตแซแแฃแบ 2022-09-30 แแฉแแฎ 3 18 22" src="https://user-images.githubusercontent.com/97823928/193203390-629a95bc-ec68-434f-8d68-3ca20c386632.png">

ํ์ง๋ง ๋ฒ๊ทธ๊ฐ ๋ฐ์ํ  ๊ฒฝ์ฐ๊ฐ ๋ง์ผ๋ฏ๋ก ์คํ๋ง๋ถํธ๋ ์๋ ๋น ๋ฑ๋ก๊ณผ ์๋ ๋น ๋ฑ๋ก์ด ์ถฉ๋๋๋ฉด ์ค๋ฅ๊ฐ ๋ฐ์ํ๋๋ก ๊ธฐ๋ณธ ๊ฐ์ ๋ฐ๊พธ์์ (์คํ๋ง๋ถํธ๋ฅผ ์คํํด์ผ ํจ)

<img width="800" alt="แแณแแณแแตแซแแฃแบ 2022-09-30 แแฉแแฎ 3 20 01" src="https://user-images.githubusercontent.com/97823928/193203608-d9881042-0ba1-4411-bf8d-96bb4df1ff78.png">

### ์์กด๊ด๊ณ ์ฃผ์ - ์์ฑ์ ์ฃผ์์ ์ฌ์ฉํด๋ผ 

์์กด๊ด๊ณ ์ฃผ์์ ํ๋ฒ ์ผ์ด๋๋ฉด ์ ํ๋ฆฌ์ผ์ด์ ์ข๋ฃ์์ ๊น์ง ์์กด๊ด๊ณ๋ฅผ ๋ณ๊ฒฝํ  ์ผ์ด ์๊ณ , ๋ณํ๋ฉด ์๋๋ค. (๋ถ๋ณ)
* ์์ ์ ์ฃผ์์ ์ฌ์ฉํ๋ฉด ๋ฉ์๋๋ฅผ public์ผ๋ก ์ด์ด๋์ด์ผ ํ๋ฏ๋ก ๋๊ตฐ๊ฐ์ ์ํด ๋ณ๊ฒฝ๋  ๊ฐ๋ฅ์ฑ์ด ๋๋ค
* ๋ํ ์์ฑ์ ์ฃผ์์ ์ฌ์ฉํ๋ฉด ์ฃผ์ ๋ฐ์ดํฐ๋ฅผ ๋๋ฝํ์ ๋ ์ปดํ์ผ ์ค๋ฅ๊ฐ ๋ฐ์ํจ (Null Point Exception๊ฐ X) โ ๊ทธ๋์ ์ด๋ ํ ๊ฐ์ ์ฃผ์ํด์ผํ ์ง ๋ฐ๋ก ์ ์ ์์
* ์์ฑ์ ์ฃผ์์ ์ฌ์ฉํ๋ฉด final์ ์ฌ์ฉํ  ์ ์์ โ ์์ฑ์์์ ํน์๋ผ๋ ๊ฐ์ด ์ค์ ๋์ง ์๋ ์ค๋ฅ๋ฅผ ์ปดํ์ผ ์์ ์ ๋ง์์ค
  * ```java: variable discountPolicy might not have been initialized```
  * ```@RequiredArgsConstructor```์ ์ฌ์ฉํ๋ฉด final์ด ๋ถ์ ํ๋๋ฅผ ๋ชจ์์ ์์ฑ์๋ฅผ ์๋์ผ๋ก ๋ง๋ค์ด์ค

### ์คํ๋ง๋น์ ๋ผ์ดํ์ฌ์ดํด

์คํ๋ง ์ปจํ์ด๋ ์์ฑ โ ์คํ๋ง ๋น ์์ฑ โ ์์กด๊ด๊ณ ์ฃผ์ โ ์ด๊ธฐํ ์ฝ๋ฐฑ โ ์ฌ์ฉ โ ์๋ฉธ ์  ์ฝ๋ฐฑ โ ์ข๋ฃ

* ์คํ๋ง๋น์ ๊ฐ์ฒด๋ฅผ ์์ฑํ๊ณ  ์์กด๊ด๊ณ ์ฃผ์์ด ๋ค ๋๋ ๋ค์์์ผ ํ์ํ ๋ฐ์ดํฐ๋ฅผ ์ฌ์ฉํ  ์ค๋น๋ฅผ ๋ง์น๋ฏ๋ก ์ด๊ธฐํ ์์์ ์์กด๊ด๊ณ ์ฃผ์์ด ๋ชจ๋ ์๋ฃ๋๊ณ  ํธ์ถํด์ผํจ
* ์คํ๋ง์ ์์กด๊ด๊ณ ์ฃผ์์ด ์๋ฃ๋๋ฉด ์คํ๋ง ๋น์๊ฒ ์ฝ๋ฐฑ ๋ฉ์๋๋ฅผ ํตํด์ ์ด๊ธฐํ ์์ ์ ์๋ ค์ค
* ๋ํ ์คํ๋ง ์ปจํ์ด๋๊ฐ ์ข๋ฃ๋๊ธฐ ์ง์ ์ ์๋ฉธ ์ฝ๋ฐฑ์ ์ค

### ์ด๊ธฐํ ์ฝ๋ฐฑ

๋ฐฉ๋ฒ 1 : ```implements InitializingBean, DisposableBean```
* InitializingBean์ afterPropertiesSet() ๋ฉ์๋๋ก ์ด๊ธฐํ๋ฅผ ์ง์
* DisposableBean์ destroy() ๋ฉ์๋๋ก ์๋ฉธ์ ์ง์
* ์ด ์ธํฐํ์ด์ค๋ ์คํ๋ง ์ ์ฉ ์ธํฐํ์ด์ค๋ฏ๋ก ํด๋น ์ฝ๋๊ฐ ์คํ๋ง ์ ์ฉ ์ธํฐํ์ด์ค์ ์์กดํ๋ฏ๋ก ๋ฉ์๋ ์ด๋ฆ ๋ณ๊ฒฝ์ ํ  ์ ์์ผ๋ฉฐ, ์ธ๋ถ ๋ผ์ด๋ธ๋ฌ๋ฆฌ์ ์ ์ฉ ๋ถ๊ฐ

๋ฐฉ๋ฒ 2 : ๋น๋ฑ๋ก ์ด๊ธฐํ, ์๋ฉธ ๋ฉ์๋ ์ง์ 
* @Bean(initMethod = "init", destroyMethod = "close")
* ์คํ๋ง ๋น์ด ์คํ๋ง ์ฝ๋์ ์์กดํ์ง X + ์ค์  ์ ๋ณด๋ฅผ ์ฌ์ฉํ๊ธฐ ๋๋ฌธ์ ์ธ๋ถ๋ผ์ด๋ธ๋ฌ์๋ ์ด๊ธฐํ, ์ข๋ฃ ๋ฉ์๋๋ฅผ ์์ฑํ  ์ ์์
* destroyMethod์ ์์ฑ์ ๊ฒฝ์ฐ ๊ธฐ๋ณธ๊ฐ์ด inferred(์ถ๋ก )์ด๋ฏ๋ก close, shutdown ๊ฐ์ ๋ฉ์๋๋ฅผ ์๋์ผ๋ก ํธ์ถ

๋ฐฉ๋ฒ 3 : @PostConstruct, @PreDestory
* ์ต์  ์คํ๋ง์์ ๊ฐ์ฅ ๊ถ์ฅํ๋ ๋ฐฉ๋ฒ 
* ํจํค์ง : ```javax.annotation.PostConstruct``` 
  * ์คํ๋ง ์ข์์  x, JSR-250 ๋ผ๋ ์๋ฐ ํ์ค
  * ๊ทธ๋ฌ๋ฏ๋ก ์คํ๋ง์ด ์๋ ๋ค๋ฅธ ์ปจํ์ด๋์์๋ ์๋ํจ
* ์ด ์ ๋ธํ์ด์์ ์ฌ์ฉํ๋, ์ฝ๋๋ฅผ ๊ณ ์น  ์ ์๋ ์ธ๋ถ ๋ผ์ด๋ธ๋ฌ๋ฆฌ๋ฅผ ์ด๊ธฐํ, ์ข๋ฃํด์ผํ๋ค๋ฉด ๋ฐฉ๋ฒ2๋ฅผ ์ฌ์ฉํ์ 

### ๋น ์ค์ฝํ

#### 1. ์ฑ๊ธํค
* ๊ธฐ๋ณธ ์ค์ฝํ, ์คํ๋ง ์ปจํ์ด๋์ ์์๊ณผ ์ข๋ฃ๊น์ง ์ ์ง๋๋ ๊ฐ์ฅ ๋์ ๋ฒ์์ ์ค์ฝํ

```java
@Test
public class SingletonTest {
    @Test  
    public void singletonBeanFind() {
      SingletonBean singletonBean1 = ac.getBean(SingletonBean.class);
      SingletonBean singletonBean2 = ac.getBean(SingletonBean.class);
      // ์ฑ๊ธํค์ด๋ฏ๋ก ๊ฐ์ ์ธ์คํด์ค๋ฅผ ๋ฐํ
      System.out.println("SingletonTest1 = " + singletonBean1);
      System.out.println("SingletonTest2 = " + singletonBean2);
      assertThat(singletonBean1).isSameAs(singletonBean2);
    }
}
```

<img width="800" alt="แแณแแณแแตแซแแฃแบ 2022-10-04 แแฉแแฎ 4 38 07" src="https://user-images.githubusercontent.com/97823928/193761978-0bcd554f-7ec1-43ee-809e-af80212b9b02.png">

* ์ด๊ธฐํ ๋ฉ์๋ ์คํ โ ๋น ์กฐํ โ ์ข๋ฃ ๋ฉ์๋ ํธ์ถ

#### 2. ํ๋กํ ํ์

<img width="500" alt="แแณแแณแแตแซแแฃแบ 2022-10-04 แแฉแแฎ 4 35 25" src="https://user-images.githubusercontent.com/97823928/193761469-76c1ba06-c35c-418d-bf2e-a4cfb50fda6b.png">

* ์คํ๋ง ์ปจํ์ด๋๋ ํ๋กํ ํ์ ๋น์ ์์ฑ๊ณผ ์์กด๊ด๊ณ ์ฃผ์๊น์ง๋ง ๊ด์ฌํ๊ณ  ๋๋ ๊ด๋ฆฌํ์ง ์๋ ๋งค์ฐ ์งง์ ๋ฒ์์ ์ค์ฝํ

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
  // @Scope์ ์ฌ์ฉํ๋ฉด @Component๊ฐ ์์ด๋ ์คํ๋ง ๋น์ผ๋ก ๋ฑ๋ก๋จ
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

<img width="570" alt="แแณแแณแแตแซแแฃแบ 2022-10-04 แแฉแแฎ 4 34 40" src="https://user-images.githubusercontent.com/97823928/193761318-6f600609-d3bb-48ff-9df0-5109bb76b12c.png">

|์ฑ๊ธํค| ํ๋กํ  ํ์|
|----|-----|
|์คํ๋ง ์ปจํ์ด๋ ์์ฑ ์์ ์ ์ด๊ธฐํ ๋ฉ์๋ ์คํ|์คํ๋ง ์ปจํ์ด๋์์ ๋น์ ์กฐํํ  ๋ ์ค์ฝํ๋น์ด ์์ฑ๋๊ณ , ์ด๊ธฐํ ๋ฉ์๋๋ ์คํ๋จ|
|ํญ์ ๊ฐ์ ์ธ์คํด์ค๋ฅผ ๋ฐํ|์กฐํํ  ๋๋ง๋ค ๋ค๋ฅธ ์คํ๋ง๋น์ ๋ฐํ|
|์คํ๋ง์ปจํ์ด๋๊ฐ ๊ด๋ฆฌํ๋ฏ๋ก ์ข๋ฃ๋ ๋ ๋น์ ์ข๋ฃ ๋ฉ์๋๊ฐ ํธ์ถ๋จ| ์คํ๋ง ์ปจํ์ด๋๊ฐ ์์ฑ๊ณผ ์์กด๊ด๊ณ ์ฃผ์, ์ด๊ธฐํ๊น์ง๋ง ๊ด๋ฆฌํ๊ณ  ๋์ด์ ๊ด๋ฆฌํ์ง ์์ โ @PreDestory ๋ฉ์๋ ์คํX|

#### 3. ์น ์ค์ฝํ

<img width="500" alt="แแณแแณแแตแซแแฃแบ 2022-10-05 แแฉแแฎ 5 37 04" src="https://user-images.githubusercontent.com/97823928/194017771-11960b27-806e-4682-8781-16436abf8c2d.png">

* request: Http ์น ์์ฒญ์ด ๋ค์ด์ค๊ณ  ๋๊ฐ๋ ๊น์ง ์ ์ง๋๋ ์ค์ฝํ, ๊ฐ๊ฐ์ HTTP ์์ฒญ๋ง๋ค ๋ณ๋์ ๋น ์ธ์คํด์ค๊ฐ ์์ฑ๋๊ณ , ๊ด๋ฆฌ๋จ
* session: ์น ์ธ์์ด ์์ฑ๋๊ณ  ์ข๋ฃ๋  ๋ ๊น์ง ์ ์ง๋๋ ์ค์ฝํ
* application: ์น์ ์๋ธ๋ฆฟ ์ปจํ์คํธ์ ๊ฐ์ ๋ฒ์๋ก ์ ์ง๋๋ ์ค์ฝํ
* websocket : ์น์์ผ๊ณผ ๋์ผํ ์๋ช์ฃผ๊ธฐ๋ฅผ ๊ฐ์ง๋ ์ค์ฝํ



### ํ๋กํ ํ์ ์ค์ฝํ with ์ฑ๊ธํค ๋น

<img width="500" alt="แแณแแณแแตแซแแฃแบ 2022-10-04 แแฉแแฎ 5 10 38" src="https://user-images.githubusercontent.com/97823928/193768275-c3292e0a-a278-49c7-89d9-fde42dbbf728.png">

* ClientBean์ ์ฑ๊ธํค์ด๋ฏ๋ก ์คํ๋ง ์ปจํ์ด๋ ์์ฑ ์์ ์ ํจ๊ป ์์ฑ๋๊ณ , ์์กด๊ด๊ณ๋ ์ฃผ์๋จ
  * 1 ) clientBean์ด ์์กด๊ด๊ณ ์๋ ์ฃผ์์ ์ฌ์ฉํ์ฌ, ์ฃผ์ ์์ ์ ์คํ๋ง์ปจํ์ด๋์ ํ๋กํ ํ์ ๋น์ ์์ฒญํจ
  * 2 ) ์คํ๋ง ์ปจํ์ด๋๋ ํ๋กํ ํ์ ๋น์ ์์ฑํด์ clientBean์ ๋ฐํ 
  * clientBean์ ํ๋กํ ํ ํ์๋น์ ๋ด๋ถ ํ๋์ ๋ณด๊ด (์ ํํ๋ ์ฐธ์กฐ๊ฐ)
  
<img width="500" alt="แแณแแณแแตแซแแฃแบ 2022-10-04 แแฉแแฎ 5 18 28" src="https://user-images.githubusercontent.com/97823928/193769861-6dfbf43d-473d-4a88-94a3-f75a2f3729fd.png">

* ํด๋ผ์ด์ธํธ A๋ clientBean์ ์คํ๋ง์ปจํ์ด๋์ ์์ฒญํด์ ๋ฐ์
  * 3 ) ํด๋ผ์ด์ธํธ A๊ฐ clientBean.logic()์ ํธ์ถ
  * 4 ) clientBean์ ํ๋กํ ํ์๋น์ addCount()๋ฅผ ํธ์ถํด์ ํ๋กํ ํ์์ count ๊ฐ์ 1 ์ฆ๊ฐ์ํด
  
<img width="500" alt="แแณแแณแแตแซแแฃแบ 2022-10-04 แแฉแแฎ 5 32 54" src="https://user-images.githubusercontent.com/97823928/193772872-1879d88b-09cc-436c-8cd8-b46fd06c8e4e.png">

* ํด๋ผ์ด์ธํธ B๋ clientBean์ ์คํ๋ง์ปจํ์ด๋์ ์์ฒญํด์ ๋ฐ์
* **clientBean์ด ๋ด๋ถ์ ๊ฐ์ง๊ณ  ์๋ ํ๋กํ ํ์๋น์ ์ด๋ฏธ ๊ณผ๊ฑฐ์ ์ฃผ์์ด ๋๋ ๋น**, ์ฌ์ฉํ ๋๋ง๋ค ์๋ก๋ ๊ฒ์ด X
  * 5 ) ํด๋ผ์ด์ธํธ A๊ฐ clientBean.logic()์ ํธ์ถ
  * 6 ) clientBean์ ํ๋กํ ํ์๋น์ addCount()๋ฅผ ํธ์ถํด์ ํ๋กํ ํ์์ count ๊ฐ์ 1 ์ฆ๊ฐ์ํด -> count ๊ฐ์ด 2๊ฐ ๋จ

#### ๋ฌธ์ ๋ ํ๋กํ ํ์ ๋น์ ์ฃผ์ ์์ ์๋ง ์๋ก ์์ฑํ๊ณ , ์ฌ์ฉํ  ๋๋ง๋ค ์๋ก ์์ฑํด์ ์ฌ์ฉํ๋ ๊ฒ์ด ์๋๋ค!
* ์ฑ๊ธํค๋น์ ์์ฑ ์์ ์๋ง ์์กด๊ด๊ณ ์ฃผ์์ ๋ฐ๊ธฐ ๋๋ฌธ์ ํ๋กํ ํ์ ๋น์ด ์๋ก ์์ฑ๋๊ธฐ๋ ํ๋, ์ฑ๊ธํค ๋น๊ณผ ํจ๊ป ๊ณ์ ์ ์ง๋๋ ๊ฒ์ด ๋ฌธ์ ์ด๋ค.

### Provider์ ์ฌ์ฉํ์ฌ ๋ฌธ์  ํด๊ฒฐ

๋ฐฉ๋ฒ 1 : ์ฑ๊ธํค ๋น์ด ํ๋กํ ํ์์ ์ฌ์ฉํ  ๋ ๋ง๋ค ์คํ๋ง์ปจํ์ด๋์ ์๋ก ์์ฒญ    
* ๋ฌธ์ ์  : ์คํ๋ง์ ์ ํ๋ฆฌ์ผ์ด์ ์ปจํ์คํธ ์ ์ฒด๋ฅผ ์ฃผ์๋ฐ๊ฒ ๋๋ฉด, ์คํ๋ง ์ปจํ์ด๋์ ์ข์์ ์ธ ์ฝ๋๊ฐ ๋๊ณ , ๋จ์ ํ์คํธ๋ ์ด๋ ค์์ง

```java
static class ClientBean {
    @Autowired
    private ApplicationContext ac;

    public int logic() { // ๋ก์ง์ ํธ์ถํ ๋๋ง๋ค ์คํ๋ง์ปจํ์ด๋์ ์๋ก ํ๋กํ ํ์๋น์ ์์ฒญํ์!
        // ์์กด๊ด๊ณ๋ฅผ ์ธ๋ถ์์ ์ฃผ์๋ฐ๋ ๊ฒ์ด ์๋๋ผ ์ง์  ํ์ํ ์์กด๊ด๊ณ๋ฅผ ์ฐพ์ : DL(์์กด๊ด๊ณ ํ์)
        PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
        prototypeBean.addCount();
        int count = prototypeBean.getCount();
        return count;
    }
}
```

๋ฐฉ๋ฒ 2 : ```ObjectFactory```, ```ObjectProvider```
* ObjectProvider ์ getObject() ๋ฅผ ํธ์ถํ๋ฉด ๋ด๋ถ์์๋ ์คํ๋ง ์ปจํ์ด๋๋ฅผ ํตํด ํด๋น ๋น์ ์ฐพ์์ ๋ฐํํ๋ค. (DL)
  * ObjectFactory: ๊ธฐ๋ฅ์ด ๋จ์, ๋ณ๋์ ๋ผ์ด๋ธ๋ฌ๋ฆฌ ํ์ ์์, ์คํ๋ง์ ์์กด
  * ObjectProvider: ObjectFactory ์์, ์ต์, ์คํธ๋ฆผ ์ฒ๋ฆฌ๋ฑ ํธ์ ๊ธฐ๋ฅ์ด ๋ง๊ณ , ๋ณ๋์ ๋ผ์ด๋ธ๋ฌ๋ฆฌ ํ์ ์์, ์คํ๋ง์ ์์กด

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

๋ฐฉ๋ฒ 3 : ```JSR-330 Provider```

```java
//implementation 'javax.inject:javax.inject:1' gradle ์ถ๊ฐ ํ์ @Autowired
private Provider<PrototypeBean> provider;
    
public int logic() {
    PrototypeBean prototypeBean = provider.get();
    prototypeBean.addCount();
    int count = prototypeBean.getCount();
    return count;
}
```
* get()๋ฉ์๋๋ฅผ ์ฌ์ฉํด์ ํด๋น ๋น์ ์ฐพ์์ ๋ฐํํ๋ ๋ณ๋์ ๋ผ์ด๋ธ๋ฌ๋ฆฌ๊ฐ ํ์ํ๋ค.
  * ObjectProvider๋ DL์ ์ํ ํธ์ ๊ธฐ๋ฅ์ ๋ง์ด ์ ๊ณตํด์ฃผ๊ณ  ์คํ๋ง ์ธ์ ๋ณ๋์ ์์กด๊ด๊ณ ์ถ๊ฐ๊ฐ ํ์ ์๊ธฐ ๋๋ฌธ์ ํธ๋ฆฌํ๋ค. 
  * ๋ง์ฝ ์ฝ๋๋ฅผ ์คํ๋ง์ด ์๋ ๋ค๋ฅธ ์ปจํ์ด๋์์๋ ์ฌ์ฉํ  ์ ์์ด์ผ ํ๋ค๋ฉด JSR-330 Provider๋ฅผ ์ฌ์ฉํด์ผํ๋ค.

### ์ค์ฝํ์ ํ๋ก์

์คํ๋ง ์ ํ๋ฆฌ์ผ์ด์์ ์คํํ๋ ์์ ์ ์ฑ๊ธํค ๋น์ ์์ฑํด์ ์ฃผ์์ด ๋๋, request ์ค์ฝํ ๋น์ ์์ฑ๋์ง ์๋๋ค!

๋ฐฉ๋ฒ 1 : ObjectProvider ์ฌ์ฉ  
๋ฐฉ๋ฒ 2 : ํ๋ก์ ๋ฐฉ๋ฒ ์ฌ์ฉ 

```java
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {
    ...
}
```
* MyLooger์ ๊ฐ์ง ํ๋ก์ ํด๋์ค๋ฅผ ๋ง๋ค์ด๋๊ณ  request์ ์๊ด์์ด ๊ฐ์ง ํ๋ก์๋ฅผ ๋ค๋ฅธ ๋น์ ๋ฏธ๋ฆฌ ์ฃผ์ํ  ์ ์์!!
* proxyMode๋ฅผ ์ค์ ํ๋ฉด ์คํ๋ง์ปจํ์ด๋๋ CGLIB๋ผ๋ ๋ผ์ด๋ธ๋ฌ๋ฆฌ๋ก ๋ด ํด๋์ค๋ฅผ ์์ ๋ฐ์ ๊ฐ์ง ํ๋ก์ ๊ฐ์ฒด๋ฅผ ๋ง๋ค์ด์ ์ฃผ์ํจ!
* ๊ทธ๋ฌ๋ฏ๋ก ์ง์ง ๊ฐ์ฒด ์กฐํ๋ฅผ ๊ผญ ํ์ํ ์์ ๊น์ง ์ง์ฐ์ฒ๋ฆฌ ๊ฐ๋ฅํด์ง

<img width="500" alt="แแณแแณแแตแซแแฃแบ 2022-10-05 แแฉแแฎ 6 33 26" src="https://user-images.githubusercontent.com/97823928/194029292-171401e9-9dc1-4a58-9d3a-420238e49815.png">

* CGLIB๋ผ๋ ๋ผ์ด๋ธ๋ฌ๋ฆฌ๋ก ๋ด ํด๋์ค๋ฅผ ์์ ๋ฐ์ ๊ฐ์ง ํ๋ก์ ๊ฐ์ฒด๋ฅผ ๋ง๋ค์ด์ ์ฃผ์
* ์ด ๊ฐ์ง ํ๋ก์ ๊ฐ์ฒด๋ ์ค์  ์์ฒญ์ด ์ค๋ฉด ๊ทธ๋ ๋ด๋ถ์์ ์ค์  ๋น์ ์์ฒญํ๋ ์์ ๋ก์ง์ด ๋ค์ด์์ผ๋ฉฐ ๋จ์ํ ์ฑ๊ธํค์ฒ๋ผ ๋์ํ๋ค
* ๋ง์น ์ฑ๊ธํค์ ์ฌ์ฉํ๋ ๊ฒ ๊ฐ์ง๋ง ๋ค๋ฅด๊ฒ ๋์ํ๊ธฐ ๋๋ฌธ์ ์ด๋ฌํ scope๋ ๊ผญ ํ์ํ ๊ณณ์์๋ง ์ต์ํํด์ ์ฌ์ฉํ์!
