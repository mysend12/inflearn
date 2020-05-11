package proxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import proxy.domain.Member;

public class App {
    public static void main( String[] args ) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Member member = new Member();
            // member.setUsername("username");

            // em.persist(member);

            // Member member1 = new Member();
            // member1.setUsername("member1");
            // Member member2 = new Member();
            // member2.setUsername("member2");
            // em.persist(member1);
            // em.persist(member2);

            // em.flush();
            // em.clear();

            // Member findMember1 = em.getReference(Member.class, member1.getId());  // proxy 클래스
            // Member findMember2 = em.getReference(Member.class, member1.getId());
            
            // // 타입체크는 instanceof 사용할 것
            // System.out.println("fineMember1 == Member: "+ (findMember1 instanceof Member));
            // System.out.println("fineMember2 == Member: "+ (findMember2 instanceof Member));

            System.out.println("--------------------------------------------------------------------------------");

            Member member = new Member();
            member.setUsername("username11");
            Member member2 = new Member();
            member2.setUsername("username22");
            Member member3 = new Member();
            member3.setUsername("username33");
            em.persist(member);
            em.persist(member2);
            em.persist(member3);

            em.flush();
            em.clear();

            Member m1 = em.find(Member.class, member.getId());
            System.out.println("m1: "+ m1.getClass());
            
            // JPA는 같은 영속성 컨텍스트 안에서 조회하면 항상 같은 객체라는 것을 보장한다.
            // 즉, 이미 m1이 호출된 상태이므로 m2도 reference를 이용해 호출했더라도 프록시가 아닌 실제 엔티티가 된다.
            // -> 또한, 성능상으로도 이미 1차 캐시에 있는 엔티티인데 굳이 프록시객체로 가져올 이유가 없다
            Member m2 = em.getReference(Member.class, member.getId());
            System.out.println("m2: "+ m2.getClass());
            System.out.println("m1 == m2: " + (m1 == m2)); // true 보장.

            System.out.println("--------------------------------------------------------------------------------");

            Member mem1 = em.getReference(Member.class, member2.getId());   // proxy
            Member mem2 = em.find(Member.class, member2.getId());           // find이지만 위에 proxy가 있으므로 proxy로 객체 반환
            System.out.println("mem1: "+mem1.getClass());
            System.out.println("mem2: "+mem2.getClass());
            System.out.println("mem1 == mem2: "+ (mem1 == mem2));

            em.detach(mem1);    // mem1을 영속성 컨텍스트에서 제외시켜도, mem2가 영속성 컨텍스트 안에 있으므로 조회 가능.
            
            System.out.println("mem1: "+ mem1.getClass());
            System.out.println("mem1 username: "+ mem1.getUsername());
            System.out.println("--------------------------------------------------------------------------------");

            Member mem3 = em.getReference(Member.class, member3.getId());

            em.detach(mem3);    // mem3를 영속성 컨텍스트에서 제외
            System.out.println("mem3: "+ mem3.getClass());

            // username을 select할 수 없어서 에러 발생. 
            // 다만, 이전에 mem3의 username을 조회한 적이 있다면 select 쿼리를 날릴 필요없이 이미 값이 있으므로 정상동작한다.
            // System.out.println("mem3 username: " +mem3.getUsername());  
            
            // 아래를 실행하기 위해서는 88줄을 주석처리 해야함.
            System.out.println("--------------------------------------------------------------------------------");

            // 프록시 인스턴스의 초기화 여부를 확인, false
            System.out.println(emf.getPersistenceUnitUtil().isLoaded(mem3));    
            mem3 = em.getReference(Member.class, member3.getId());
            // 프록시 인스턴스의 초기화 여부를 확인, false -> select한 적이 없으므로 초기화 상태가 아니라고 판단하는듯
            System.out.println(emf.getPersistenceUnitUtil().isLoaded(mem3));
            
            // 프록시 인스턴스의 초기화 여부를 확인, true -> username의 값을 가져올때 select하므로 초기화 상태..
            System.out.println(mem3.getUsername());
            System.out.println(emf.getPersistenceUnitUtil().isLoaded(mem3));

            System.out.println("--------------------------------------------------------------------------------");

            

            tx.commit();
        } catch (Exception e) {
            System.out.println("rollback..........");
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    /**
     * em.find(): 데이터베이스를 통해서 실제 엔티티 객체 조회
     * em.getReference(): 데이터베이스 조회를 미루는 가짜(프록시) -> 데이터베이스 조회를 미루는 가짜 엔티티 객체 조회(프록시)
     * hibernate가 내부적으로 프록시 라이브러리를 사용해서 실제 엔티티 클래스를 상속받아 만들어진다 ( 엔티티와 겉 모양이 같다 )
     * 이론상, 실제 엔티티 클래스처럼 사용하면 된다.
     *  -> 프록시 객체는 실제 객체의 참조(target)를 보관
     *  -> 프록시 객체를 호출하면 프록시 객체는 실제 객체의 메서드를 호출한다.
     
     * 프록시 객체의 특징
     * 프록시 객체는 처음 사용할 때 한 번만 초기화
     * 프록시 객체를 초기화 할 때, 프록시 객체는 실제 엔티티로 바뀌는 것이 아니다. 초기화되면 프록시 객체를 통해서 실제 엔티티에 접근 가능.
     * 프록시 객체는 원본 엔티티를 상속, 타입 체크시 instanceof 사용 (== 사용 X)
     * 영속성 컨텍스트에 있는 엔티티를 호출할 경우, em.getReference()를 호출해도 프록시가 아니라 실제 엔티티를 호출한다.
     * 준영속 상태일 때, 프록시를 초기화 하면 문제 발생할 수 있다. 코드 참조
     */

}