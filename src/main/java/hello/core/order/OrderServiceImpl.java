package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
    //private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    //private final DiscountPolicy discountPolicy = new RateDiscountPolicy(); //할인 정책을 변경하려면 OrderServiceImpl 코드르 변경해주어야 함

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        // orderServiceImpl 입장에서는 할인에 대한 것은 모름 -> 할인에 대한 변경이 생기면 할인쪽만 변경하면 됨 (단일 책임 원칙 ok)
        int discountPrice = discountPolicy.discount(member, itemPrice); // 할인될 금액 리턴
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
