package hello.core.singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SingletonService {

    // static 영역에 객체를 1개만 생성
    private static final SingletonService instance = new SingletonService();

    // public으로 열어서 객체 인스턴스가 필요하면 이 static 메서드를 통해서만 조회 가능
    public static SingletonService getInstance() {
        return instance;
    }

    // 생성자는 private으로 막아서 외부에서 new를 통해 객체 생성을 못하도록 막음
    private SingletonService() {}

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }

}
