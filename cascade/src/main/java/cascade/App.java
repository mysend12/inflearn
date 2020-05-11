package cascade;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import cascade.domain.Child;
import cascade.domain.Parent;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Parent parent = new Parent();
            Child child1 = new Child();
            Child child2 = new Child();

            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);

            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
            // orphanRemoval옵션을 true로 줄 경우 remove시 알아서 delete쿼리가 날아간다.
            findParent.getChildren().remove(0); 

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }

    }

    /**
     * 영속성 전이(CASCADE)와 고아 객체
     * parent를 persist할때 자동으로 child들도 persist 되게 만들고 싶을때 사용 -> cascade
     * @OneToMany를 저장할 때, cascade = CascadeType.ALL 설정을 해두면 자동으로 설정된다.
     
     * 연관관계를 매핑하는 것과 아무 관련이 없다.
     * -> 엔티티를 영속화할 때 편리함을 제공하는것 뿐, 테이블/객체와는 아무 연관관계가 없다.
     * 
     * 사용하면 안되는 경우: 다른 엔티티에서도 같은 객체를 관리하는 경우.
     * 즉, 단일 엔티티에 종속적인 경우, 라이프사이클이 유사한 경우에 사용(소유자가 하나일 때만 사용)
     
     * 고아 객체: 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제하는 기능.
     * orphanRemoval = true
     * cascade와 마찬가지로 참조하는 곳이 하나일 때만 사용! -> 특정 엔티티가 개인 소유할 때만.
     
     * 영속성 전이 + 고아 객체, 생명 주기
     * CascadeType.ALL + orphanRemovel=true 모두 설정할 경우,
     * 부모 엔티티를 통해서 자식 새명주기를 관리할 수 잇다.
     * Child의 생명주기를 Parent가 관리한다.
     *  -> 도메인 주도 설계(DDD)의 Aggregate Root개념을 구현할 때 유용하다.
     *  -> 대충 생명주기는 부모 엔티티가 관리하고 자식 엔티티는 Repository를 만들지 않는다는 개념
     */
}
