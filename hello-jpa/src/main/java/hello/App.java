package hello;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * 
     * JPA는 특정 데이터베이스에 종속되지 않는다.
     * SQL문이 데이터베이스마다 다른데, JPA는 이것에서 자유롭다.
     * -> 데이터베이스를 운영 중에 바꾸더라도, 문제가 없어야 한다(이론적인 부분)
     * 
     */
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
