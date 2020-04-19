package entities.main;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import entities.domain.Member;
import entities.domain.Team;
import lombok.extern.slf4j.Slf4j;

/**
 * JpaMain
 * 
 * resources/META-INF/persistence.xml을 참고한다.
 
 * 엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유하자.
 * 엔티티 매니저는 쓰레드 간에 공유하면 안된다 ( 사용하고 버려야 한다. )
 * JPA의 모든 데이터 변경은 트랜잭션 안에서 실행한다.
  
 * JPQL, JPA에서의 SQL이라고 생각하면 된다.
 * 
 */
@Slf4j
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Team team = new Team("team1");
            em.persist(team);

            tx.commit();
        } catch (Exception e){
            tx.rollback();
        }finally{
            em.close();
        }
        emf.close();
        
    }
}