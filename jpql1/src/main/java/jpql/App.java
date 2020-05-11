package jpql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import jpql.domain.Member;

public class App {

    public static void main( String[] args ){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            Member member = new Member();
            member.setUsername("user");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("------------------------------------------------------");

            TypedQuery<Member> typeQuery = em.createQuery("select m from Member m", Member.class);
            Query query = em.createQuery("select m.username, m.age from Member m");

            // List<Member> resultList = typeQuery.getResultList();
            // resultList.forEach(m -> System.out.println(m.getUsername()));

            TypedQuery<Member> typeQuery1 = em.createQuery("select m from Member m where m.id = :id", Member.class);
            typeQuery1.setParameter("id", 1L);
            Member m = typeQuery1.getSingleResult();
            System.out.println("username: "+m.getUsername());


            tx.commit();
        } catch(Exception e){
            e.printStackTrace();
            tx.rollback();
        } finally{
            em.close();
            emf.close();
        }


    }
    /**
     * JPQL 문법
     * 엔티티와 속성은 대소문자 구분 O (ex. Member, Order ...)
     * JPQL 키워드는 대소문자 구분 X (ex. select, from ...)
     * 테이블명이 아닌 엔티티 이름 사용
     * 별칭은 필수
      
     * TypeQuery: 반환타입이 명확할때 사용
     * Query: 반환타입이 명확하지 않을때 사용
     
     * query.getResultList 시, 무조건 리스트 형식으로 반환(값이 없을경우 빈 리스트 반환)
     * 하지만, getSingleResult()는 결과가 정확히 하나여야함.
     * 결과 = 0: javax.persistence.NoResultException
     * 결과 > 1: javax.persistence.NonUniqueResultException
     
     * 파라미터 바인딩
     * 
     */
}
