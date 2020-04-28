package proxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import proxy.domain.Member;
import proxy.domain.Team;

public class App {
    public static void main( String[] args ) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Member member = new Member();
            member.setUsername("username");

            em.persist(member);

            em.flush();
            em.clear();

            Member findMember = em.getReference(Member.class, member.getId());  // proxy 클래스
            System.out.println("findMember = "+findMember.getClass());

            tx.commit();
        } catch (Exception e) {
            System.out.println("rollback..........");
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    /**
     * em.find(): 데이터베이스를 통해서 실제 엔티티 객체 조회
     * em.getReference(): 데이터베이스 조회를 미루는 가짜(프록시) -> 데이터베이스 조회를 미루는 가짜 엔티티 객체 조회(프록시)
     * hibernate가 내부적으로 프록시 라이브러리를 사용해서 실제 엔티티 클래스를 상속받아 만들어진다 ( 엔티티와 겉 모양이 같다 )
     * 이론상, 실제 엔티티 클래스처럼 사용하면 된다.
     *  -> 프록시 객체는 실제 객체의 참조(target)를 보관
     *  -> 프록시 객체를 호출하면 프록시 객체는 실제 객체의 메서드를 호출한다.
     
     * 프록시 객체의 특징
     * 
     */

}