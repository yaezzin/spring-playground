package hello.core.singleton;

public class StatefulService {

    private int price; // 상태를 유지하는 가격 필드

    //클라이언트 의도 :
    public void order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
