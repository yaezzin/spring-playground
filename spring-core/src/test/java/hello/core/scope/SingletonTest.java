package hello.core.scope;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonTest {

    @Test
    public void singletonBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class);
        SingletonBean singletonBean1 = ac.getBean(SingletonBean.class);
        SingletonBean singletonBean2 = ac.getBean(SingletonBean.class);

        // 싱글톤 이므로 같은 인스턴스를 반환할 것임!
        System.out.println("SingletonTest1 = " + singletonBean1);
        System.out.println("SingletonTest2 = " + singletonBean2 );
        assertThat(singletonBean1).isSameAs(singletonBean2);
        ac.close();
    }

    @Scope("singleton")
    static class SingletonBean {
        
        @PostConstruct
        public void init() {
            System.out.println("SingletonBean.init");
        }
        
        @PreDestroy
        public void destroy() {
            System.out.println("SingletonBean.destroy");
        }
        
    }
}
