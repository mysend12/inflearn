package mappings;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import mappings.domain.Member;
import mappings.domain.Team;

public class App {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Member member = new Member();
            member.setUsername("member2");

            em.persist(member);             // 팀을 먼저 컨텍스트에 저장하지 않으면 데이터가 저장되지 않는다.

            Team team = new Team();
            team.setName("teamA");
            
            team.getMembers().add(member);  // 외래키 업데이트
            em.persist(team);

            tx.commit();
        }catch(Exception e){
            tx.rollback();
        }finally{
            em.close();
        }
        emf.close();
    }
/***
 * ManyToMany 매핑은 실무에서 사용하면 안된다.
 
 * 이전 강의들의 핵심 내용
 * 테이블: 외래키 하나로 조인 -> 방향이라는 개념이 없다
 * 객체: 참조용 필드가 있는 쪽으로만 참조가능 -> 양쪽이 서로 참조하면 양방향(단방향 2개)
 * 연관관계의 주인: 외래 키를 관리하는 참조
 * 주인의 반대편: 외래 키에 영향을 주지 않고 단순 조회만 가능하다.
 
 * OneToMany 단방향 매핑 ( (우형팀장 김영한님 기준) 실무에서 잘 사용하지 않고 권장하지 않음 )
 * Team을 중심으로 관리(Team에서 외래키를 관리)
 * 코드를 알아보기 힘들기(코드와 쿼리가 매칭이 잘 안된다) 때문에 실무에서 사용하는데 권장하지 않는다.
 *  -> 참조할 일이 없더라도 양방향으로 만들어서 사용.
 * 
 * 1:다 단방향의 경우 1이 연관관계의 주인
 * 테이블 일대다 관계는 항상 다(N)쪽에 외래키가 있다.
 *  -> 코드가 이해하기 어려워져서 엉뚱한 테이블에 쿼리가 날아갈수 있다.
 *  -> 엔티티가 관리하는 외래 키가 다른 테이블에 있어서 연관관계 관리를 위해 추가로 UPDATE가 실행된다.
 *  -> 1:다 단방향 매핑보다는 다대일 양방향 매핑을 사용하는 것을 권장!!
 *  -> 1:다 양방향 매핑은 아래처럼 사용은 가능 ( 스펙상 존재하는 것이 아니라 꼼수 )
 *  @ManyToOne @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
 *  private Team team;
 *  -> 읽기 전용으로 만들기 위해 insert와 update 기능은 끄는 것. C, U를 끄지 않으면 데이터가 꼬인다.(심각한 오류)
 
 * @JoinColumn을 사용하지 않으면 중간 테이블(연관관계 테이블)이 하나 생긴다
 * 
 
 * 
 * 
 */

}
