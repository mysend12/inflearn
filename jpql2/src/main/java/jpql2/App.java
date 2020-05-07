package jpql2;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import jpql2.domain.Address;
import jpql2.domain.Member;
import jpql2.domain.MemberDTO;
import jpql2.domain.Team;

public class App {
    public static void main( String[] args ) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        try{
            et.begin();
            // Member member = new Member();
            // member.setUsername("username1");
            // member.setAge(10);
            // em.persist(member);
            Team team = new Team();
            team.setName("teamA");
            Team team2 = new Team();
            team2.setName("teamB");
            em.persist(team);
            em.persist(team2);

            for(int i=0; i<50; i++){
                Member m = new Member();
                m.setAge(i);
                m.setUsername("username"+i);
                if(i%2==0)
                    m.setTeam(team);
                else
                    m.setTeam(team2);
                em.persist(m);
            }

            em.flush();
            em.clear();

            // List<Team> result = em.createQuery("select m.team from Member m", Member.class).getResultList(); // 아래와 같은 쿼리지만 조인을 직접 명시해주는 것이 좋다.
            List<Team> result = em.createQuery("select t from Member m join m.team t", Team.class).getResultList(); // 엔티티 프로젝션
            List<Address> result2 = em.createQuery("select o.address from Order o", Address.class).getResultList(); // 임베디드 타입 프로젝션
            List<Object[]> result3 = em.createQuery("select m.username, m.age from Member m").getResultList(); // 스칼라 타입 프로젝션 ->뒤에 타입을 명시하면 안됨. Object[]로 나온다.
            List<MemberDTO> result4 = em.createQuery("select new jpql2.domain.MemberDTO(m.username, m.age) from Member m").getResultList(); // 스칼라 타입 프로젝션 -> new 생성자를 이용한 호출.
            
            Object[] objArr = result3.get(0);
            System.out.println("username = " + objArr[0]);
            System.out.println("age = " + objArr[1]);
            System.out.println(result4.get(0));
            System.out.println("-----------------------------------------------------------------------------------------------------------");
            em.flush();
            em.clear();
            
            // member = new Member();
            // member.setUsername("username2");
            // member.setAge(11);
            // em.persist(member);
            em.flush();
            em.clear();


            // 페이징.
            List<Member> list = em.createQuery("select m from Member m order by m.age desc", Member.class)
                .setFirstResult(0)  // 시작 지점
                .setMaxResults(10)  // 몇 개를 가져올지?
                .getResultList();

            list.forEach(mem -> System.out.println(mem));
            System.out.println("-----------------------------------------------------------------------------------------------------------");
            
            // 조인.
            em.flush();
            em.clear();
            String innerJoinQuery = "select m from Member m inner join m.team t";                           // 내부조인
            String outerJoinQuery = "select m from Member m left join m.team t";                            // 외부조인
            String setaJoinQuery = "select m from Member m, Team t where m.username = t.name";              // 세타조인

            String filteringJoinQuery = "select m from Member m left join m.team t ON t.name = 'teamA'";    // 조인 대상 필터링
            String strangeQuery = "select m from Member m left join Team t on m.username = t.name";         // 연관관계 없는 엔티티 외부 조인
            List<Member> memberList = em.createQuery(strangeQuery, Member.class).getResultList();
            memberList.forEach(mem -> System.out.println(mem));
            
            System.out.println("-----------------------------------------------------------------------------------------------------------");
            
            // 서브 쿼리
            String scalaQuery = "select (select m1 from Member m1) from Member m join Team t ON m.username = t.name";   // 스칼라 쿼리, 예시라서 대충 짠 말도 안되는 쿼리...결과X
            List<Member> memberList2 = em.createQuery(scalaQuery, Member.class).getResultList();


            memberList2.forEach(m -> System.out.println(m));

            et.commit();
        }catch(Exception e){
            e.printStackTrace();
            et.rollback();
        }finally{
            em.close();
            emf.close();
        }
    }

    /**
     **프로젝션: SELECT 절에 조회할 대상을 지정하는 것.
     * 대상 = 엔티티, 임베디드 타입, 스칼라 타입(숫자/문자 등 기본 데이터 타입)
     * 
     * 여러 값 조회
     * 1. Query 타입으로 조회
     * 2. Object[] 타입으로 조회
     * 3. new 명령어로 조회
     *  - 단순 값을 DTO로 바로 조회 -> 생성자 이용(순서와 타입 일치하는 생성자 이용), 패키지 명을 전부 적어야한다.
     
     **페이징 API
     * JPA는 페이징을 다음 두 API로 추상화
     * setFirstResult(int startPosition): 조회 시작 위치(0부터 시작)
     * setMaxResults(int maxResult): 조회할 데이터 수
     
     **조인
     * ON절을 활용한 조인(JPA 2.1 부터 지원)
     * 1. 조인 대상 필터링
     *  -> ex) 회원과 팀 조인, 팀 이름이 A인 팀만 조인
     *  -> JPQL: SELECT m, t FROM Member m LEFT JOIN m.team ON t.name = 'A'
     *  -> SQL: SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.TEAM_ID=t.id and t.name='A'
     * 
     * 2. 연관관계 없는 엔티티 외부 조인(하이버네이트 5.1부터)
     *  -> ex) 회원의 이름과 팀의 이름이 같은 대상 외부 조인
     *  -> JPQL: SELECT m, t FROM Member m LEFT JOIN Team t ON m.username = t.name
     *  -> SQL: SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.username = t.name
     * 
     **서브 쿼리
     * [NOT] EXISTS (subquery): 서브쿼리에 결과가 존재하면 참
     *  - {ALL | ANY | SOME} (subquery)
     *  - ALL: 모두 만족하면 참
     *  - ANY, SOME: 같은 의미, 조건을 하나라도 만족하면 참
     * [NOT] IN (subquery): 서브쿼리의 결과 중 하나라도 같은 것이 있으면 참
     * 
     * JPA 표준 스펙에서는 WHERE, HAVING 절에서만 서브 쿼리 사용 가능
     * 하이버네이트에서는 SELECT 절에서도 가능(스칼라 쿼리)
     * FROM 절에서의 서브 쿼리는 JPQL에서 불가능 ( 조인으로 풀어서 해결 -> 안되면 쿼리를 두 번 날려서 해결 -> 네이티브 쿼리 이용... )
     * 
     */
}
