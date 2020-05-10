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

            if(1==2) {
                Team team = new Team("teamA");
                em.persist(team);
                Member member = new Member(27, "kim", "bosung", team);
                em.persist(member);

            // 단일 값 연관 경로. 묵시적 내부조인 발생(탐색 O) -> 실무에서는 왠만하면 사용하지 말자.
            String singleValueQuery = "SELECT m.team.name FROM Member m"; 
            
            // 컬렉션 값 연관 경로. 묵시적 내부조인 발생(탐색 X) -> t.members는 가능하지만, t.members.id는 불가능
            // -> t.members.size는 가능(리스트이기 때문에, size 값을 가져올 수는 있다)
            String collectionValueQuery = "SELECT t.members FROM Team t";
            String collectionValueQuerySize = "SELECT t.members.size FROM Team t";

            List<String> results = em.createQuery(singleValueQuery, String.class).getResultList();
            Integer sizeResult = em.createQuery(collectionValueQuerySize, Integer.class)
                            .getSingleResult();

            results.forEach(result -> log.info(result));
            log.info(""+sizeResult);
            }

            if(1==2){
                Team teamA = new Team("TeamA");
                Team teamB = new Team("TeamB");
                Member member1 = new Member(28,"first1", "last1", teamA);
                Member member2 = new Member(28, "first2", "last2", teamA);
                Member member3 = new Member(29, "first3", "last3", teamB);
                em.persist(teamA);
                em.persist(teamB);
                em.persist(member1);
                em.persist(member2);
                em.persist(member3);

                em.flush();
                em.clear();

                String query = "SELECT m FROM Member m join";
                // 회원들, 조회 (SQL 1)
                // 회원1, 팀A (SQL 2)
                // 회원2, 팀A (1차 캐시)
                // 회원3, 팀B (SQL 3)

                // 회원 100명 -> N + 1


                String fetchQuery = "SELECT m FROM Member m join fetch m.team";
                // 쿼리 한방에 모든 데이터를 가져옴
                
                List<Member> result = em.createQuery(fetchQuery, Member.class).getResultList();
                
                for(Member m : result){
                    log.info("member = " + m + ", " + m.getTeam());
                }
                
                String collectionFetchQuery = "SELECT t FROM Team t join fetch t.members";
                // 데이터가 뻥튀기 되서 나온다.
                // -> 팀 A에 멤버가 2명이 있기 때문에 팀 A가 두번 나온다.
                // -> 팀 A에 멤버가 100명이면 같은 결과 100개...
                // -> 물론, 영속성 컨텍스트에서는 같은 객체 1개를 100줄로 호출하는 것임.(객체 100개를 만드는게 아님)

                String collectionFetchQueryDistinct = "SELECT DISTINCT t FROM Team t join fetch t.members";
                // -> SQL 상에서는 distinct가 적용되지 않는다 (DB입장에서는 팀A - 멤버1, 팀B - 멤버2 이런 형식이므로)
                // -> 그래서 distinct를 사용하면, sql상에서 1번, 애플리케이션에서(컬렉션에서) 중복을 제거한다.
                // -> 그래서, 결과는 2개(팀A, 팀B)
                List<Team> result2 = em.createQuery(collectionFetchQueryDistinct, Team.class).getResultList();
                for(Team t : result2){
                    log.info("team = " + t + ", " + t.getMembers().toString());
                }
            }
                
            if(1==1){
                Team teamA = new Team("TeamA");
                Team teamB = new Team("TeamB");
                Member member1 = new Member(28,"first1", "last1", teamA);
                Member member2 = new Member(28, "first2", "last2", teamA);
                Member member3 = new Member(29, "first3", "last3", teamB);
                em.persist(teamA);
                em.persist(teamB);
                em.persist(member1);
                em.persist(member2);
                em.persist(member3);

                em.flush();
                em.clear();

                String fetchQuery = "SELECT t FROM Team t join fetch t.members";
                // 페이징시, 데이터를 전부 가져오고 메모리에서 페이징함;;
                // WARN: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
                
                String query = "SELECT t FROM Team t";
                // 팀 조회
                // 팀A의 멤버 조회
                // 팀B의 멤버 조회

                List<Team> result = em.createQuery(query, Team.class)
                                .setFirstResult(0)
                                .setMaxResults(2)
                                .getResultList();
                
                for(Team t : result){
                    log.info("team = " + t + ", "+ "member = " + t.getMembers().toString());
                }

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
     * 경로 표현식: .(점)을 찍어 객체 그래프를 탐색하는 것
     * m.username -> 상태 필드
     * m.team t -> 단일 값 연관 필드
     * m.orders o -> 컬렉션 값 연관 필드(orders가 컬렉션임)
     * 
     * 상태 필드 (state field): 값을 저장하기 위한 필드
     * 연관 필드(associtation field): 연관관계를 위한 필드
     *  - 단일 값 연관 필드: @ManyToOne, @OneToOne. 대상이 엔티티(ex: m.team)
     *  - 컬렉션 값 연관 필드: @OneToMany, @ManyToMany, 대상이 컬렉션(ex: m.orders)
     * 
     * 경로 표현식 특징
     * 1. 상태필드: 경로 탐색의 끝, 탐색X
     * 2. 단일 값 연관 경로: 묵시적 내부 조인(inner join) 발생, 탐색O
     * 3. 컬렉션 값 연관 경로: 묵시적 내부 조인 발생, 탐색X
     *  - FROM 절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통해서 탐색이 가능하다.?
     * 
     * 조인은 SQL 튜닝에 중요 포인트이다.
     * 하지만, 묵시적 조인은 조인이 일어나는 상황을 파악하기 힘들기 때문에, 튜닝을 어렵게 만드는 주요 요인.
     *  -> 결론: 묵시적 조인을 사용하지 말자!! -> 잘못하면 계속해서 이너조인 등 이상한 쿼리가 나오고, 유지보수도 힘들어진다.
     
     **fetch join
     *  -> SQL 조인이 아닌 JPQL에서 성능 최적화를 위해 제공하는 기능.
     *  -> 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능.
     *  join fetch 명령어 사용.
     * Eager Loading과 비슷하지만, 이것을 동적(?) 사용 가능??
     * 
     * 컬렉션 fetch join -> 데이터 뻥튀기 발생 가능 -> DISTINCT 사용하여 뻥튀기를 없앨 수 있다.
     * -> JPQL의 DISTINCT를 사용하여 중복 제거
     * 1. SQL에서 DISTINCT를 적용
     * 2. 애플리케이션에서 중복 제거
      
     * 페치 조인의 특징과 한계
     * fetch 조인의 특징과 한계
     * 
     * 1. 페치 조인 대상에는 별칭을 줄 수 없다.
     *  -> 하이버네이트에서는 가능하지만, 가급적 사용하지 말자.
     *  -> 만약, 팀A에 멤버가 5명인데 3명만 조회 후 조작할 경우 매우 위험한 상황(남은 2명이 지워지거나 예상치 못한 쿼리)이 발생할 수 있다.
     *  -> 팀A의 멤버 데이터 중 일부만 조작하고 싶은 경우, 멤버 데이터를 따로 조회하는 것이 맞다.
     * 
     * 2. 둘 이상의 컬렉션은 페치 조인 할 수 없다.
     *  -> 1:N:M의 관계가 되어버려서 뻥튀기의 뻥튀기(제곱 수준)이 되버리거나 데이터가 맞지 않는다.
     * 
     * 3. 컬렉션을 페치 조인하면, 페이징 API(setFirstResult, setMaxResults)를 사용할 수 없다.
     *  -> 1:1, M:1 같은 단일 값 연관 필드들은 페이징이 안된다.
     *  -> 1:다의 관계에서는 페이징을 할 경우, 뻥튀기 값들로 인하여 정상동작을 할 수 없기 때문에 페이징이 불가능.
     *  -> 1:다에서는 경고 로그를 남기고 데이터를 전부(1건이든 100만건이든 다 가져옴..) 가져와서 메모리에서 페이징(매우 위험함)
     
     * @BatchSize(size = 100)
     *  - 컬렉션 위에 넣거나,
     *  - persistence.xml에 hibernate.default_batch_fetch_size의 값을 설정하여 글로벌 설정으로 만들 수 있다.
     * @BatchSize(size = 100)를 컬렉션에 넣어 주면, LAZY 로딩으로 끌고 올 때 100개의 데이터를 가져온다
     *  -> team을 조회할 때, N+1 만큼의 쿼리가 아니라 테이블의 수만큼 쿼리가 나간다.
     *  
     * 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적이다. (모든 것을 페치 조인으로 해결하는 것은 좋지 않다)
     *  -> 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 하면,
     *  -> 페치 조인보다는 일반 조인을 사용하고 필요한 데이터들만 조회하여 DTO로 반환하는 것이 효과적이다.
     * 
     **다형성 쿼리 ( 싱글 테이블 전략의 예, 다른 전략도 똑같지만, SQL 쿼리만 달라진다.)
     *
     * Item, Album, Movie, Book 과 같이 다형성을 이용한 엔티티들이 있을 경우,
     * SELECT i FROM Item i WHERE TYPE(i) IN (Book, Movie)
     *  -> SQL: SELECT i FROM i WHERE i.DTYPE in('B', 'M')
     * 
     * TREAT(JPA 2.1 이상)
     * SELECT i FROM Item i WHERE TREAT(i as Book).auther = 'kim'
     *  -> SQL: SELECT i.* FROM Item i WHERE i.DTYPE = 'B' and i.auther = 'kim'
     */
}