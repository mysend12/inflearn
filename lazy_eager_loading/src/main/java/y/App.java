package y;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import y.domain.Member;
import y.domain.Team;

public class App {
    public static void main( String[] args ) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Team teamA = new Team();
            Team teamB = new Team();
            teamA.setName("teatA");
            teamB.setName("teatB");
            em.persist(teamA);
            em.persist(teamB);

            Member member1 = new Member();
            Member member2 = new Member();
            member1.setUsername("user1");
            member2.setUsername("user2");
            member1.setTeam(teamA);
            member2.setTeam(teamB);
            em.persist(member1);
            em.persist(member2);

            em.flush();
            em.clear();

            // Member m = em.find(Member.class, member1.getId());          // LAZY로딩 시, member.getTeam은 초기화되지 않은 프록시 객체
            // System.out.println("m: " + m.getTeam().getClass());
            // System.out.println("m.getTeam: " + m.getTeam().getName());  // 여기서 team이 초기화되므로 select문이 날아간다.

            System.out.println("---------------------------------------------------------------------------------------------------");

            // Member m = em.find(Member.class, member1.getId());          // EAGER로딩 시, member.team은 여기서 초기화
            // System.out.println("m: " + m.getTeam().getClass());
            // System.out.println("m.getTeam: " + m.getTeam().getName());      // 이미 값을 가지고 있기 때문에 select문이 나가지 않는다.

            System.out.println("---------------------------------------------------------------------------------------------------");

            // EAGER 로딩을 했음에도 JPQL을 사용할 경우 SQL로 번역이 되므로 Member만 select하면 Member만 가져온다.
            // List<Member> members 
            // = em.createQuery("select m from Member m", Member.class).getResultList();

            // LAZY로딩과 간단한 fetch조인 예시.
            List<Member> members 
            = em.createQuery("select m from Member m join fetch m.team", Member.class).getResultList();

            members.forEach(m -> {
                System.out.println(m.getUsername() + ", " + m.getTeam());
                System.out.println(m.getTeam().getName());
            });

            tx.commit();
        } catch(Exception e) {
            e.printStackTrace();
            em.close();
        } finally {
            emf.close();
        }
    }
    /**
     * 지연 로딩(LAZY)
     * 지연로딩을 사용하면 Member를 호출할 때, Team 객체는 프록시 객체를 넣어준다.
     * 따라서, Member를 호출 시 Member 필드들 중, team을 제외한 필드들만 호출되며 team 필드는 실제로 사용할 때 select query가 날아간다.
     
     * 즉시 로딩(EAGER)
     * 즉시로딩을 사용하면 Member를 호출할 때 team객체도 같이 가져온다.
    
     * 실무에서는 가급적이면 지연 로딩만 사용하는 것이 좋다.
     * 이론적으로는 Member와 Team이 자주 함께 사용한다면 Eager로딩, Member와 Team이 같이 사용되는 빈도가 적다면 LAZY로딩을 사용한다.
     * 실무에서는 지연 로딩만 사용하는 이유)
     * 1. 즉시 로딩을 적용하면 예상하지 못한 SQL이 발생.
     * 2. 테이블이 한 두개라면 DB내에서 조인을 한다고 해도 성능 상 문제가 없다.
     *  -> 하지만, 실무에서는 Member같은 경우 대부분의 테이블과 연관되어 있기 때문에 엄청나게 많은 조인이 발생할 가능성이 높다. ( 성능상 문제 )
     * 3. 즉시 로딩은 JPQL에서 N+1(쿼리가 20~30개가 나간다...) 문제를 일으킨다.
     * OneToMany는 기본이 LAZY이지만, ManyToOne이나 OneToOne은 default 값이 EAGER로딩이므로 전부 LAZY로 바꾸자.
     
     * 지연로딩의 활용 - 실무
     * 모든 연관관계에서 지연로딩을 사용하라
     * JPQL fetch 조인이나 엔티티 그래프 기능을 사용해라
     * 즉시 로딩은 상상하지 못한 쿼리가 나간다.
     */
}
