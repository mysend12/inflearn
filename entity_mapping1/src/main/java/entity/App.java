package entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import entity.domain.Member;
import entity.domain.Team;

/**
 * Hello world!
 */
public final class App {

    /**
     * 1. 객체와 테이블 연관관계의 차이를 이해
     * 2. 객체의 참조와 테이블의 외래 키를 매핑
      
     * 방향성(단, 양방향), 다중성(1:1, 1:N, N:M, N:1), 연관관계의 주인(Owner)
     
     * 테이블은 외래 키로 조인을 사용해서 연관된 테이블을 찾는다.
     * 객체는 참조를 사용해서 연관된 객체를 찾는다.
     */
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            Team team = new Team();
            team.setName("TeamB");
            em.persist(team);
            
            Member member = new Member();
            member.setUsername("bosung2");
            member.setTeam(team);
            em.persist(member);
            
            Member findMember = em.find(Member.class, member.getId());
            Team findTeam = findMember.getTeam();

            System.out.println("findTeam: " + findTeam.getName());

            tx.commit();
        }catch(Exception e){
            System.out.println("exception....");
            tx.rollback();
        }finally{
            em.close();
        }
        emf.close();

    }


}
