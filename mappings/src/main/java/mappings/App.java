package mappings;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class App {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            // Member member = new Member();
            // member.setUsername("member2");

            // em.persist(member);             // 팀을 먼저 컨텍스트에 저장하지 않으면 데이터가 저장되지 않는다.

            // Team team = new Team();
            // team.setName("teamA");
            
            // team.getMembers().add(member);  // 외래키 업데이트
            // em.persist(team);

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
//  -------------------------------------------------------------------------------------------
 * 1:1: 주 테이블이나 대상 테이블 중 외래 키 선택 가능
 *  -> 주 테이블에 외래 키
 *  -> 대상 테이블에 외래 키 단방향: JPA에서 지원하지 않으며 양방향으로 바꾸면 가능
 * 외래 키에 데이터베이스 유니크(UNI) 제약조건 추가
 
 * 주 테이블에 외래 키
 *  -> 주 객체가 대상 객체의 참조를 가지는 것처럼 주 테이블에 외래 키를 두고 대상 테이블을 찾는다.
 *  -> 객체 지향 개발자 선호
 *  -> JPA 매핑 편리
 *  -> 장점: 주 테이블만 조회해도 대상 테이블에 데이터가 있는지 확인 가능
 *  -> 단점: 값이 없으면 외래 키에 null 허용
 
 * 보조 테이블에 외래 키
 *  -> 대상 테이블에 외래 키가 존재
 *  -> 전통적인 데이터베이스 개발자 선호
 *  -> 장점: 주 테이블과 대상 테이블을 1:1에서 1:다의 관계로 변경 시 테이블 구조를 유지할 수 있음
 *  -> 단점: 프록시 기능의 한계로 지연 로딩으로 설정해도 항상 즉시 로딩됨
//  -------------------------------------------------------------------------------------------
 * 다:다 ( 실무에서 쓰지 말자 )
 * 관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없다.
 * 연결 테이블(조인 테이블)을 추가해서 일대다, 다대일 관계로 풀어야 한다.
 * 객체는 컬렉션을 사용해서 객체 2개로 다대다 관계가 가능.
 *  -> 편리해 보이지만 실무에서 사용하면 안됨
 *  -> 연결 테이블이 단순히 연결만 하고 끝나지 않는다.
 *  -> 주문시간, 수량 같은 데이터가 들어올 수 있는데 중간 테이블에 추가 데이터를 넣을 수 없기 때문에 사용 불가
 *  -> 쿼리도 내가 생각하지 못한 쿼리가 날아갈 수 있기 때문에 사용하지 말것.
 
 * 다대다 한계 극복 -> 1 : 다, 다 : 1의 관계로 변경
 *  -> 이 경우에서도 DB설계 시 복합 PK가 아니라 PK 하나 만드는 것이 편리함
 * 실무에서는 JPA에서도 테이블처럼 중간 객체를 하나 만들어서 1:다, 다:1 관계로 변경하여 사용해야 한다.
 
 * PK ( 키값 )은 비즈니스적으로 의미가 없는 값으로 생성해서 사용하자! -> 시스템이 매우 유연해진다.
 */
}
