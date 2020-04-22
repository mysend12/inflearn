package entity;

import java.util.List;

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
     
     * 양방향 매핑시, 참조와 외래키의 차이를 이해해야 한다.
     * 테이블 간의 연관관계는 외래키로 이루어져 있다. ( 외래키 하나로 양방향 연관관계가 맺어져 있다. )
     * 객체간의 참조에서는 서로를 멤버변수로 가지고 있어야 한다.
     
     * mappedBy(조회만 가능?). 객체와 테이블간의 연관관계를 맺는 차이를 이해해야 한다.
 
     * 객체 연관관계 = 2개( 단방향 연관관계 2개 )
     *  - 회원 -> 팀 1개(단방향)
     *  - 팀 -> 회원 1개(단방향)
      
     * 테이블 연관관계 = 1개( 방향이 없음,FK )
     
     * 연관관계의 주인(Owner)
     * 두 객체 중 하나를 연관관계의 주인으로 지정하여 연관관계의 주인만이 외래 키를 관리(등록 / 수정)
     * 주인이 아닌쪽은 읽기만 가능.
     * 주인이 아닌쪽에 mappedBy를 이용하여 주인과 연결.
     
     * 누가 주인이 될까? ( 우형 팀장의 가이드.. 성능이슈, 설계 상의 이점.)
     *  -> 외래 키가 있는 곳을 주인으로 정하자
     *  -> 비즈니스로 생각하지 말고 그냥 1:다 관계에서는 다쪽이 연관관계의 주인이 되면 된다.
     
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
            // Team findTeam = findMember.getTeam();
            List<Member> members = findMember.getTeam().getMembers();

            for(Member m : members)
                System.out.println(m.toString());

            // System.out.println("findTeam: " + findTeam.getName());

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
