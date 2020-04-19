package hello;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class DetachedEx {
    /**
     * 준영속 상태 (detached 상태)
     * 영속 상태의 엔티티가 영속성 컨텍스트에서 분리되는 것
     * 영속성 컨텍스트가 제공하는 기능(더티 체킹 등)을 사용하지 못한다.
     */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        /**
         * JPA에서는 트랜잭션 단위가 매우 중요함. 데이터를 변경하는 모든 작업은 트랜잭션 단위 안에서 해야한다.
         */
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 영속
            Member member = em.find(Member.class, 1L);
            member.setName("Member1Change");

            // 준영속 -> JPA가 관리하지 않는다, 위에서 데이터를 변경했음에도 update Query가 발생하지 않는다.
            em.detach(member);

            System.out.println("=========================================");
            // 위에서 영속성 컨텍스트를 초기화시켰기 때문에 같은 객체임에도 select문이 다시 실행된다.
            Member member1 = em.find(Member.class, 1L);
            System.out.println("=========================================");
            
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
    /**
     * em.detach(entity): entity만 영속성 컨텍스트에서 제외시킨다.
     * em.clear(): 영속성 컨텍스트를 통째로 지운다.
     */

}