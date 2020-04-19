package hello;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * PersistencContextEx
 */
public class PersistencContextEx {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        /**
         * JPA에서는 트랜잭션 단위가 매우 중요함. 데이터를 변경하는 모든 작업은 트랜잭션 단위 안에서 해야한다.
         */
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 비영속
            Member member = new Member();
            member.setId(3L);
            member.setName("hello3Member...");

            // 영속
            System.out.println("=========before========");
            em.persist(member);
            System.out.println("=========after========");

            // 조회용 Query가 나가지 않는다. 위에서 1차 캐시에 저장된 member를 가져오기 때문.
            System.out.println("===================================================");
            Member findMember = em.find(Member.class, 3L);
            System.out.println("findMember: " + findMember.getId());
            System.out.println("findMember: " + findMember.getName());

            // 영속 -> 아래 Query는 1번만 실행된다. findMember2는 캐시에 있는 값을 반환한다.
            Member findMember1 = em.find(Member.class, 3L);
            Member findMember2 = em.find(Member.class, 3L);

            // 영속 Entity의 동일성을 보장한다 ( 복사가 아니라 아예 같은 객체 )
            System.out.println("===================================================");
            System.out.println(findMember1 == findMember2);
            System.out.println("===================================================");
            tx.commit(); // 여기서 DB에 저장된다.
            System.out.println("===================================================");
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

}
    /**
     * JPA를 이해하기 위해 영속성 컨텍스트를 이해해야 한다.
     
    * JPA에서 가장 중요한 2가지
    * 1. 객체와 관계형 데이터베이스 매핑 ( 설계 )
    * 2. 영속성 컨텍스트 ( 실제 JPA가 내부에서 어떻게 동작하나? )
    
    * EntityManagerFactory와 EntityManager
    * EntityManagerFactory를 통해 고객의 요청이 올때마다 EntityManger를 생성하여 DB를 조회한다.
    
    * 영속성 컨텍스트: JPA를 이해한는데 가장 중요한 용어
    * -> "엔티티를 영구 저장하는 환경"
    * EntityManger.persist(entity); : DB에 저장하는것이 아니라 영속성 컨텍스트에 저장하는 것을 의미한다.
    
    * 영속성 컨텍스트는 논리적인 개념
    * 눈에 보이지 않는다
    * 엔티티 매니저를 통해 영속성 컨텍스트에 접근한다.
    * EntityManaer 1:1 PersistenceContext
    
    * 비영속(new/transient): 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태(최초에 객체를 생성한 상태)
    * 영속(managed): 영속성 컨텍스트에 관리되는 상태(EntityManger.persist(entity))
    * 준영속(detached): 영속성 컨텍스트에 저장되었다가 분리된 상태?
    * 삭제(removed)
    
    * 비영속
    * 객체를 생성한 상태(JPA와 관계가 없는 상태)
    
    * 영속
    * EntityManger를 가져와서 객체를 저장한 상태 ( 영속 )
    * -> em.persist(entity) : 당연히 트랜잭션 안이어야 한다.
    * persist코드에서(영속상태에서) DB에 저장되는 것이 아니다.
    * -> 트랜잭션을 커밋할 때 쿼리가 날아간다.

    * 준영속
    * em.detach(entity) 객체를 영속성 컨텍스트에서 분리, 준영속 상태
    
    * 삭제
    * em.remove(entity) 객체를 삭제한 상태

    */


    /***
     
    * 엔티티 조회, 1차 캐시
    Member member = new Member();
    member.setId("member1");
    em.persist(member); // 1차 캐시에 저장된다.
    Member findMember = em.find(Member.class, "member1"); // 1차 캐시에서 조회

    일단 1차 캐시를 확인하고, 캐시에 없으면 DB를 확인한다.
    DB조회 후, 가져올때 1차 캐시에 저장하고 데이터를 가져온다.
    -> DB 트랜잭션 단위 -> 트랜잭션이 끝나면 캐시도 지워진다.
    -> 너무 짧은 단위이기 때문에 성능상의 이점을 얻기 힘들다. ( 오히려 캐시에 저장하는게 더 오래걸리지 않을까.. )
    -> 다만, 비즈니스 로직이 복잡한 경우 성능상의 이점이 있을 수 있음.


    */