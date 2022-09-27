# 🌱 spring-core

<img width="600" alt="스크린샷 2022-09-28 오전 3 30 33" src="https://user-images.githubusercontent.com/97823928/192607612-be80bf23-283b-4016-b7a8-cef06fa52b1b.png">

``` java
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository = new MemoryMemberRepository(); 

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
```

* MemberServiceImpl의 경우 구현체를 선택할 수 있음 : ```new MemoryMemberRepository()```
* 즉, ```추상화```(MemberService 인터페이스)와 ```구체화```(MemoryMemberRepository) 모두 의존중
