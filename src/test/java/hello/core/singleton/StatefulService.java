package hello.core.singleton;

public class StatefulService {

    /* private int price; // 상태를 유지하는 가격 필드 -> 없애자!

    public void order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        this.price = price; // 여기가 문제임!
    }

    public int getPrice() {
        return price;
    }
    */

    // 변경
    public int order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        return price;
    }
}
