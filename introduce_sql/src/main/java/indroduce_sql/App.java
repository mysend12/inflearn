package indroduce_sql;

import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.List;

import javax.persistence.EntityManager;

import javax.persistence.EntityManagerFactory;

import indroduce_sql.domain.Member;

public class App {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            
            // Criteria
            // CriteriaBuilder cb = em.getCriteriaBuilder();
            // CriteriaQuery<Member> query = cb.createQuery(Member.class);

            // Root<Member> m = query.from(Member.class);

            // CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
            // List<Member> resultList = em.createQuery(cq).getResultList();

            Member member = new Member();
            member.setUsername("user1");
            em.persist(member);

            em.flush();

            // 네이티브 쿼리
            List<String> resultList = em.createNativeQuery("select USERNAME FROM MEMBER").getResultList();
            System.out.println(resultList.size());
            
            for(int i=0;i<resultList.size();i++)
                System.out.println(resultList.get(0));



            tx.commit();
        }catch(Exception e){
            tx.rollback();
        }finally{
            em.close();
        }
        emf.close();
    }

/***
 * 
 **Criteria
 * 문자가 아닌 자바코드로 JPQL을 작성할 수 있다.
 * JPQL 빌더 역할
 * JPA 공식 기능
 * 단점: 너무 복잡하고 실용성이 없다.{@link App}
 *  -> 결론: Criteria 대신 QueryDSL 사용 권장
 
 **QueryDSL
 * 문자가 아닌 자바코드로 JPQL을 작성
 * JPQL 빌더 역할
 * 컴파일 시점에 문법 오류를 찾을 수 있다.
 * 동적 쿼리를 작성하는 것이 편리하다.
 * 실무 사용 권장.
 * -> 레퍼런스가 엄청 잘 만들어져있다.
 
 **네이티브 SQL
 * JPQL로 짤 수 없는 쿼리의 경우 네이티브 SQL 이용.
 * 
 * -> JPA를 사용하면서 JDBC 커넥션이나 스프링 JdbcTemplate, 마이바티스 등 함께 사용 가능
 * 단, 영속성 컨텍스트를 적절한 시점에 강제로 플러시하는 것이 필요하다.
 
 * JPQL은 객체지향 쿼리 언어. 엔티티 객체를 대상으로 쿼리
 * SQL을 추상화해서 특정 데이터베이스에 SQL을 의존하지 않는다.
 */
}
