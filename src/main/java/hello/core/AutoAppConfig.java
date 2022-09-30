package hello.core;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(

        basePackages = "hello.core", // 탐색할 패키지의 시작 위치 (관례 : basePackages를 쓰지 않고 설정정보의 클래스는 최상단에 두자!)
        // AppConfig가 자동으로 등록되지 않도록 @Configuration이 붙은 것들은 제외하도록 함!
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
    // 빈으로 등록된 클래스가 하나도 없다!
}

