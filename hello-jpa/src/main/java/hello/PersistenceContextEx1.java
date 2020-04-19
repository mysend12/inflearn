package hello;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class PersistenceContextEx1 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        /**
         * JPA에서는 트랜잭션 단위가 매우 중요함. 데이터를 변경하는 모든 작업은 트랜잭션 단위 안에서 해야한다.
         */
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member1 = new Member(7L, "Member4");
            Member member2 = new Member(8L, "Member6");

            em.persist(member1);
            em.persist(member2);

            Member member = em.find(Member.class, 4L);
            // persist를 호출하지 않아도 JPA가 update쿼리를 알아서 날려준다.(변경 감지)
            // update도 commit시점에 query 실행.
            member.setName("change4Member..");  

            System.out.println("===========================================");

            // insert는 commit시점에 데이터베이스에 쿼리를 날린다.
            // JDBC배치 ( hibernate.jdbc.batch_size 로 설정 가능 )
            // DB 접속을 줄여준다. -> 잘 활용한다면 JPA의 성능문제를 해결할 수 있다.
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
    /**
     * 
     * 변경 감지(Dirty Checking)
     * 1. commit을 하면 flush()가 발생
     * 2. 엔티티와 스냅샷 비교
     * 3. Entity가 변경된 경우 UPDATE SQL을 생성한다.
     * 4. flush ( 쿼리 수행? )
     * 5. commit
     
     * 스냅샷
     *  - 1차 캐시가 변경될때마다 스냅샷을 찍어두고, 
     * 위(변경 감지)의 2번째 단계에서 Entity와 스냅샷의 변경사항을 비교한다.
     * 
     */
}