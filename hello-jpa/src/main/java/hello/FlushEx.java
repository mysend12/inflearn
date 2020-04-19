package hello;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class FlushEx {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        /**
         * JPA에서는 트랜잭션 단위가 매우 중요함. 데이터를 변경하는 모든 작업은 트랜잭션 단위 안에서 해야한다.
         */
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member(9L, "Member4");
            em.persist(member);

            // 쿼리가 날아가는 것을 보고 싶을 때 사용.
            // 쓰기지연 SQL 저장소에 있는 것들이 데이터베이스에 반영되는 과정(1차 캐시를 지우지는 않는다.).
            em.flush(); 
            
            System.out.println("=========================================");
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
        /**
         * flush()
         * 영속성 컨텍스트의 변경내용을 데이터베이스에 반영. (영속성 컨텍스트의 변경내용을 데이터베이스에 동기화)
         * 트랜잭션이라는 작업 단위가 중요하다. -> 커밋 직전에만 동기화를 하면 된다.
         
         * Transcation이 발생하면 flush가 발생한다.
         * 1. 변경 감지
         * 2. 수정된 Entity쓰기 지연 SQL 저장소에 등록
         * 3. 쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송(CRUD)
         
         * 영속성 컨텍스트를 플러시하는 방법
         * 1. em.flush() -> 직접 호출 ( 쓸일 별로 없음. 테스트시 사용? )
         * 2. commit ( flush 자동 호출 )
         * 3. JPQL 쿼리 실행 ( flush 자동 호출 )
         
         * JPQL 쿼리 실행 시, 플러시가 자동으로 호출되는 이유
         * 중간에 JPQL을 실행할 때, 쓰기지연 SQL 저장소에 데이터가 있을 경우, DB가 갱신(?)되지 않았으므로 사용자가 원하지 않는 결과가 나온다.
         * JPQL을 실행하기 전에 DB를 업데이트(??)하고 실행시켜야 정확한 값이 나온다.
         
         * 플러시 모드 옵션
         * em.setFlushMode(FlushModeType.COMMIT) 등으로 플러시 모드를 껏다 킬수 있다.
         * FlushModeType.AUTO ( 커밋이나 쿼리를 실행할 때 플러시 (defalut) )
         * FlushModeType.COMMIT ( 커밋할 때만 플러시 )
         * 쓰기지연 SQL 저장소에 있는 값이 현재 select할 값과 전혀 상관이 없을 경우, flush를 쓸 필요가 없으므로 성능을 향상시킬 수 있다.
         * 하지만, 거의 쓸일 없고 실수하면 안되니까 AUTO로 쓰자.
         
         */
    }
