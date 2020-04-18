package hello;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * JpaMain
 * 
 * resources/META-INF/persistence.xml을 참고한다.
 
 * 엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유하자.
 * 엔티티 매니저는 쓰레드 간에 공유하면 안된다 ( 사용하고 버려야 한다. )
 * JPA의 모든 데이터 변경은 트랜잭션 안에서 실행한다.
  
 * JPQL, JPA에서의 SQL이라고 생각하면 된다.
 * 
 */
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        /**
         * JPA에서는 트랜잭션 단위가 매우 중요함.
         * 데이터를 변경하는 모든 작업은 트랜잭션 단위 안에서 해야한다.
         */
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        
        // 정석 코드
        try{
            // insert
            // Member member = new Member();
            // member.setId(2L);
            // member.setName("HelloMember2");
            // entityManager.persist(member);

            // select
            // Member findMember = em.find(Member.class, 1L);
            // System.out.println(findMember.getName());
            // System.out.println(findMember.getId());

            // update
            // JPA를 통해서 select를 사용하여 객체를 가져오면 객체를 JPA가 관리한다.
            // 즉, 트랜잭션 안에서 객체가 변하고 커밋이 될 경우, JPA가 자동으로 update query를 날려준다.
            // Member findMember = em.find(Member.class, 1L);
            // findMember.setName("HelloJPA");

            // JPQL ( 객체지향 SQL )
            // ( 테이블 기준이 아니라 객체 기준!!!!!! 객체지향 쿼리 언어, DB를 바꿔도 동일한 동작 ) <-> SQL: 테이블 기준 쿼리
            // 대상이 테이블이 아니라 객체가 된다. -> 자바라서 대소문자도 구분함 ㅡ.ㅡ
            // where조건을 사용할 때도 객체가 대상임.
            List<Member> result = em.createQuery("SELECT M FROM Member AS M", Member.class)
                .setFirstResult(5).setMaxResults(8)    // 페이징..
                .getResultList()
                ;
            
            result.forEach(member -> {
                System.out.println("name: " + member.getName());
            });

            tx.commit();

        } catch (Exception e){
            tx.rollback();
        }finally{
            em.close();
        }
        emf.close();
        
    }
}