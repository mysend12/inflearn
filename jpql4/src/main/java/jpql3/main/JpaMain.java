package jpql3.main;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import jpql3.domain.Member;
import jpql3.domain.Team;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JpaMain {

    public static void main(final String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        try{
            et.begin();

            Team teamA = new Team("TeamA");
            Team teamB = new Team("TeamB");
            em.persist(teamA);
            em.persist(teamB);
            Member member1 = new Member(10, "kim", "bosung", teamA);
            Member member2 = new Member(20, "first1", "last1", teamA);
            Member member3 = new Member(30, "first2", "last2", teamB);
            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

            em.flush();
            em.clear();

            int i = 3;
            if(i==1) {


                String memberQuery = "SELECT m FROM Member m WHERE m = :member";
                Member findMember = (Member) em.createQuery(memberQuery, Member.class)
                        .setParameter("member", member1)
                        .getSingleResult();
                log.info(findMember.toString());

                String teamQuery = "SELECT m FROM Member m WHERE m.team = :team";
                List<Member> members = em.createQuery(teamQuery, Member.class)
                        .setParameter("team", teamA)
                        .getResultList();
                log.info(members.toString());
            }

            if(i==2) {
                List<Member> members = em.createNamedQuery("Member.findByFirstName", Member.class)
                        .setParameter("firstName", "kim")
                        .getResultList();
                log.info(members.toString());
            }

            if(i==3) {
                String query = "UPDATE Member m set m.age = m.age+1";

                Member findMember = em.find(Member.class, member1.getId());

                int resultCount = em.createQuery(query).executeUpdate();
                
                // 벌크 연산 전에 조회한 데이터를 사용할 경우, 데이터 불일치 발생
                log.info(findMember.toString());

                em.clear(); // 데이터 불일치를 해결하기 위한 영속성 컨텍스트 초기화

                findMember = em.find(Member.class, member1.getId());
                log.info(findMember.toString());

                log.info(resultCount+"");   // 업데이트된 갯수
            }


            et.commit();
        } catch (final Exception e) {
            et.rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    /**
     **엔티티 직접 사용 - 기본 키 값
     * JPQL에서 엔티티를 직접 사용하면 SQL에서 해당 엔티티의 기본 키 값(@Id)을 사용
     * 
     * [JPQL]
     *  - SELECT COUNT(m.id) FROM Member m
     *  - SELECT COUNT(m) FROM Member m
     * [SQL]( 위 두 JPQL 모두 아래의 SQL 실행 )
     *  - SELECT COUNT(m.id) as cnt FROM Member m
     * 
     **Named 쿼리 - 정적 쿼리 ( Entity 클래스에 정의해서 사용. 현재는 Member 클래스에 샘플 적용 )
     *  - 미리 정의해서 이름을 부여해두고 사용하는 JPQL
     *  - 정적 쿼리
     *  - 어노테이션, XML에 정의
     *  - 애플리케이션 로딩 시점에 초기화하고 재사용 ( JPQL을 파싱해서 캐시로 가지고 있는다. -> JPQL에서 SQL로의 변환 비용이 줄어든다 )
     *  - 애플리케이션 로딩 시점에 쿼리를 검증 ( 쿼리 검증. 매우 중요 )
     * 
     * Named 쿼리 - XML에 정의 -> 스프링부트의 레파지토리에 정의하는게 Named쿼리인가??...
     * O.O -> 이름없는 Named Query라고 부름.
     * 
     **벌크 연산
     * 
     * 재고가 10개 미만인 모든 상품의 가격 10% 상승시,
     * JPA 변경 감지 기능으로 실행하려면 너무 많은 SQL을 싱행하게 된다.
     * 1. 재고가 10개 미만인 상품을 리스트로 조회
     * 2. 상품 엔티티의 가격을 10% 증가
     * 3. 트랜잭션 커밋 시점에 변경감지 동작
     * 4. 변경된 데이터가 100건이라면 100번의 UPDATE SQL을 실행
     * 
     * 쿼리 한방에 여러 테이블 로우 변경
     * executeUpdate()의 결과는 영향받은 엔티티 수 반환
     * 
     * 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리입력
     * -> 벌크 연산 전 flush()는 수행되지만,
     * -> 벌크연산 후에도 컨텍스트에는 벌크연산이 실행되기 전의 데이터가 남아있음!
     * -> 즉, DB와 어플리케이션의 데이터 불일치 발생.
     * 
     *  - 방법1. 벌크 연산을 먼저 실행
     *  - 방법2. 벌크 연산 수행 후, 영속성 컨텍스트 초기화
     *  -> Spring Data JPA에서는 레파지토리의 메서드에 @Modifying을 적용하면 알아서 해줌.
     * 
     */
}