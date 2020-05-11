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
     
     * 양방향 매핑시 가장 중요한 것.
     * Insert시에 Member테이블이 연관관계의 주인이므로 Member객체의 team 변수에 team을 넣어줘야 한다.
     * 만약, Team객체의 Members에만 Member객체 넣어주더라도 참조값(TEAM_ID)는 null이 된다.
     
     * 왠만하면 양쪽 다 변수에 서로를 가지고 있을 것.
     * insert 후, flush()를 안하고 select할 경우 1차 캐시의 내용을 가지고 오기 때문에 select문이 안나간다.
     * 즉, 한쪽에만 값을 세팅할 경우 트랜잭션 안에서 다시 값을 사용할 때 잘못된 결과가 나올 수 있다.
     * -> 순수 객체 상태를 고려해서 항상 양쪽에 값을 설정!
     
     * 연관관계 편의 메서드 생성(연관관계의 주인 쪽 setter -> 어디에 만들든 상관없지만 한 객체에만 만들것)
     * 혹은 setter는 관례적으로 사용하는 메서드이기 때문에 의미 전달이 안될 확률이 있으므로, 
     * 메서드 이름을 setTeam이 아니라 잘 알수 있도록 바꾸면 좋다.
     public void setTeam(Team team) {
         this.team = team;               // 이 변수가 팀을 저장하고
         team.getMembers().add(this);    // 팀에도 이 멤버를 저장시킨다.
        }
     
     * toString(), lombok, JSON 생성 라이브러리 사용 시, 무한루프가 발생할 수 있다.
     * ex) toString시, Member에서 member변수를 호출하면 team객체가 호출되고 team에 member가 포함되어 있어 다시 Member를 호출해서 무한루프
     * 해결 방법
     * 1. toString()을 사용하지 말것. -> 그냥 한쪽에 toString(exclude="객체")를 사용할 것.
     * 2. Entity를 Controller에서 직접 반환하지 말고 다른 객체로 전환해서 반환.
     
     * 양방향 매핑 정리
     * 1. 단방향 매핑만으로도 이미 연관관계 매핑이 완료된다.
     *  -> 양방향 매핑은 반대 방향으로 조회 기능이 추가된 것일 뿐이다.
     *  -> JPQL에서 역방향으로 탐색할 일이 많다.
     *  -> 단방향 매핑을 잘하면 양방향은 필요할 때 추가해도 된다. (테이블에 영향을 주지 않는다)
     *
     * 2. 연관관계의 주인을 정하는 기준
     *  -> 비즈니스 로직을 기준으로 연관관계의 주인을 선택하면 안된다!
     *  -> 연관관계의 주인은 외래키의 위치를 기준으로 정해야 한다.
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
