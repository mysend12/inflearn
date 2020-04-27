package supermappin;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import supermappin.domain.Movie;

public class App {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            // Movie movie = new Movie();
            // movie.setActor("actor2");
            // movie.setName("movie2");
            // movie.setDirector("b");
            // movie.setPrice(1000);
            // em.persist(movie);

            // em.flush();
            // em.clear();

            Movie findMovie = em.find(Movie.class, 1);
            System.out.println("findMovie = " + findMovie);

            tx.commit();
        }catch(Exception e){
            tx.rollback();
        }finally{
            em.close();
        }
        emf.close();
    }

    /**
     * 슈퍼타입 서브타입 논리 모델을 실제 물리 모델로 구현하는 방법
     * 1. 각각 테이블로 변환 -> 조인 전략 ( 정규화(슈퍼테이블 + 서브테이블) )
     *  -> 객체들을 만들고 부모클래스에 @Inheritance(strategy = InheritanceType.JOINED) 추가
     *
     * 2. 통합 테이블로 변환 -> 단일 테이블 전략 ( 한 테이블에 데이터를 전부 넣는 방법, 단일 테이블 전략 )
     *  -> 각 객체들을 만들고 상속관계만 맺어주면 됨.
     *  -> 혹은, 부모 클래스에서 @Inheritance(strategy = InheritanceType.SINGLE_TABLE) 추가
     *  -> DTYPE은 무조건 생김 ( 단일 테이블이기 때문에 dtype이 없을 경우, 어느 데이터인지 확인할 방법이 없음 )
     * 
     *  3. 서브타입 테이블로 변환 -> 구현 클래스마다 테이블 전략 ( 조금 중복이더라도 서브 테이블들을 만들고 그 안에 수퍼 테이블의 컬럼까지 포함시키기(서브테이블 갯수만큼) )
     *  -> 부모 클래스에 @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) 추가, 부모클래스가 반드시 추상클래스여야 한다.
     *  -> id로 데이터를 찾기 위해서는 서브테이블을 전부 찾아야 하기 때문에, 검색할 때 전부 조인해서 데이터를 가져오는 매우 비효율적인 동작을 한다.
     
     * 부모 클래스에 @DiscriminatorColumn 추가 시, DTYPE이 생성 ( 어느 테이블과 조인해야 하는지 확인하기 위함? )
     * 자식 클래스에 @DiscriminatorValue("A") 를 사용하면 DTYPE 이 A로 들어간다 ( default는 Entity명과 같다. )
     
     * JPA의 장점: 테이블의 설계(전략)을 변경할 경우에 어노테이션만 변경해도 충분히 쉽게 변경이 가능하다.
     * 
     * 1. 조인 전략 ( 정석, 객체지향적으로나 설계가 깔끔해진다 )
     *  장점: 테이블 정규화, 저장 공간의 효율화, 외래 키 참조 무결성 제약조건 활용 가능(?)
     *  단점: 조회시 조인을 많이 사용(성능 저하), 조회 쿼리가 복잡, 데이터 저장시 INSERT SQL 2번 호출
     
     * 2. 단일 전략
     *  장점: 조회 쿼리가 단순, 조인이 필요없어 조회 성능이 가능
     *  단점: 자식 엔티티가 매핑한 컬럼은 모두 NULL허용, 단일 테이블에 모든 것을 저장해서 테이블이 지나치게 커질 수 있다.
     *  -> 테이블이 지나치게 커지는건 임계점(?)을 넘어야함. 왠만하면 안넘음.
     
     * 3. 구현 클래스마다 테이블 전략( 쓰지 말것, DB설계자와 ORM전문가 모두 싫어함 )
     *  장점: 서브 타입을 명확하게 구분하여 처리할 때 효과적, NOT NULL 사용가능
     *  단점: 여러 자식 테이블을 함께 조회할 때 성능이 느리며 자식 테이블을 통합해서 쿼리하기 어려움
     
     * 기본은 조인전략, 단순하고 데이터가 적고 확장할 일도 없을거 같은때 단일 전략
     // -------------------------------------------------------------------------------------
     * @MappedSuperclass: 안에있는 필드(컬럼)들만 상속받고 싶을때 부모클래스를 정해서 사용.
     *  -> 기본적으로 반드시 있어야 하는 공통의 매핑정보에 사용한다(ex. create_time 등등...).
     *  -> 상속관계이지만 쿼리조회는 할 수 없다.
     *  -> 즉, 직접 사용할 수 없으므로 추상클래스로 사용하는 것을 추천.
     *  -> 참고 ) JPA에서 Entity는 Entity나 MappedSuperclass만 상속받을 수 있다.

     */

}
