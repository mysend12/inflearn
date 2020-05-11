package type;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import type.domain.AddressEntity;
import type.domain.Member;
import type.types.Address;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // Address address = new Address("city", "street", "10-1");
            // Period period = new Period();

            // Member member1 = new Member();
            // member1.setUsername("userName1");
            // member1.setHomeAddress(address);
            // member1.setWorkPeriod(period);
            // Member member2 = new Member();
            // member2.setUsername("username2");
            // member2.setHomeAddress(address);
            // member1.setWorkPeriod(period);

            // em.persist(member1);
            // em.persist(member2);

            // 참조 값이기 때문에 member1의 city만 변경하려고 했지만, member1과 2 모두 변경됨.
            // member1.getHomeAddress().setCity("new City");
            // -> 불변객체로 만들기 위해 setter를 제거
            // ------------------------------------------------------------------------
            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(new Address("city", "street", "10-1"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new AddressEntity("oldCity1", "oldStreet1", "10-2"));
            member.getAddressHistory().add(new AddressEntity("oldCity2", "oldStreet2", "10-3"));
            member.getAddressHistory().add(new AddressEntity("oldCity3", "oldStreet3", "10-4"));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("--------------------------------------------------------------------------");
            Member findMember = em.find(Member.class, member.getId()); // 컬렉션 값 타입들은 지연로딩
            Set<String> favoriteFoods = findMember.getFavoriteFoods(); // select
            favoriteFoods.forEach(food -> System.out.println(food));

            List<AddressEntity> addressHistory = findMember.getAddressHistory(); // select
            addressHistory.forEach(addr -> System.out.println(addr.getCity() + "," + addr.getStreet() + "," + addr.getZipcode()));

            // 수정 -> 값 타입은 불변으로 만들어야 한다. 수정이 아닌, 삭제 후 추가.
            findMember.setHomeAddress(new Address("new city", "new street", "new zipcode"));

            // 컬렉션의 치킨을 한식으로 변경 -> 해당 값을 변경하는 것이 아닌, 삭제 후 추가
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");

            // 이렇게 할 경우, 쿼리가 지나치게 많이 나간다
            // -> 값 변경시, memberId를 기준으로 삭제하기 때문에 컬렉션의 모든 데이터를 삭제 후, 다시 추가한다.
            findMember.getAddressHistory().remove(new AddressEntity("oldCity1", "oldStreet1", "10-2"));
            findMember.getAddressHistory().add(new AddressEntity("newCity0", "oldStreet1", "10-0"));

            tx.commit();
        } catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
    /**
     * 
     * 기본값 타입
     *  - 자바 기본 타입(int, double) -> 공유가 안되므로 사이드 이펙트가 없음.
     *  - 래퍼 클래스(Integer, Long) -> 참조형이므로 공유는 가능하지만, 변경이 불가능하므로 사이드 이펙트가 없음.
     *  - String
      
     * 임베디드 타입(embedded type, 복합 값 타입) -> 공유되는 타입이므로 사이트 이펙트를 조심해야 한다!
     *  - 개발자가 직접 커스터마이징한 값 타입으로 복합 값 타입이라고도 한다.
     *  - 재사용 가능, 높은 응집도. 해당 값 타입만 사용하는 의미 있는 메서드를 만들 수 있다.
     *  - 값 타입을 소유한 엔티티의 생명주기에 의존한다.
     *  - 엔티티의 값일 뿐, 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같다.
     *  - 객체와 테이블을 아주 세밀하게 매핑하는 것이 가능해진다.
     *  - 잘 설계한 ORM 어플리케이션은 매핑한 테이블의 수보다 클래스의 수가 많아진다.
     ** 한 엔티티에서 같은 값 타입을 사용하려면 컬럼 명이 중복되어 에러가 발생한다.
     *  - @AttributeOverrides, @AttributeOverride를 사용해서 컬럼 명 속성을 재정의해서 사용하면 된다.
     
     * 값 타입과 불변 객체
     * 값 타입: 복잡한 객체 세상을 단순화하기 위해 만든 개념, 안전하게 다룰 수 있어야 한다.
     * 값 타입 공유 참조
     *  -> 임베디드 타입 같은 참조형 값 타입을 여러 엔티티에서 공유 시, 부작용(side effect) 발생 가능
     *  -> 만약 인스턴스를 공유하는 상황이 발생할 경우, 인스턴스를 deepCopy(참조 X)해서 사용해야 하지만...
     *  -> 하지만, 실수할 수 있음. 자바에서는 컴파일 단계에서 이 실수를 막을 방법이 없다.
     ** 불변 객체
     * 객체 타입을 수정할 수 없게 만들면 부작용을 원천 차단할 수 있다.
     * 값 타입은 불변 객체로 설계해야 함! -> 여러가지 방법이 있음.
     * 1. Setter를 만들지 않고 생성자로만 값을 설정할 수 있게 만들면 된다.
     *  -> 다만, 값을 변경할 수 없으므로 데이터 변경 시 새로운 객체를 생성해야 하는 약간의 불편함이 생긴다.
     *  -> 이 경우 자주 변경할 것 같다면, 객체 내부에 copy 메서드를 만들어서 사용하면 된다.
     ** 값 타입의 비교
     * 값 타입은 주소 값이 달라도 그 안에 값이 같으면 같다고 나오도록 만들어야 한다.
     * 동일성(참조값) <--> 동등성(값) 비교
     * -> equals() 메서드를 오버라이딩해야 함.
     
     * 컬렉션 값 타입
     * 값 타입을 컬렉션에 담아서 사용하는 것.
     * 데이터베이스는 컬렉션을 같은 테이블에 저장할 수 없다.
     *  -> 컬렉션을 저장하기 위한 별도의 테이블이 필요. -> N:M의 관계...
     *  -> 컬렉션 값 타입은 위의 예제에서 Member의 생명주기에 소속되어 있기 때문에 member만 persist해도 하위 값타입들은 알아서 insert/update 된다.
     *  -> cascade 옵션을 사용하는 것과 비슷한 효과.
     *  -> member 객체를 find할 경우, 지연로딩.(select할 때 가져오지 않음)
     ** 값 타입 컬렉션의 제약사항 
     * -> 값 타입은 엔티티와 다르게 식별자 개념이 없다.
     * 변경시, 추적이 어렵다.
     * 값 타입 컬렉션에 변경이 생길 경우, 주인 엔티티와 관련된 모든 데이터를 삭제하고 현재 있는 모든 데이터를 다시 저장한다.
     * 값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본 키를 구성해야 한다. null X, 중복 X
     *******
     * 값 타입 컬렉션은 위 방식으로 사용하면 안됨!
     ** 대안
     * 실무에서는 값 타입 컬렉션 대신에 1:N 관계를 고려하자!
     * 1:N 관계를 위한 엔티티를 만들고, 거기서 값 타입을 사용
     * 영속성 전이, 고아 객체 제거를 사용해서 값 타입 컬렉션처럼 사용.
     
     ** 값 타입 컬렉션을 사용하는 경우 
     * -> 정말 간단한 경우, 셀렉트 박스에 [치킨, 피자] 정도로 간단한 데이터만 있는 경우???
     * -> 어쨋든 간단한 경우, 데이터가 변경되도 상관없는 경우??에만 사용하라는데 모르겠다. 
     * 걍 쓰지말자.
     */
}
