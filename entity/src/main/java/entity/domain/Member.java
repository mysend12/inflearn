package entity.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "MEMBER")
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    
    private String name;
    
    private String city;

    private String zipcode;

    private String street;

    // Member에 orders를 넣는 것은 좋은 생각이 아니다.
    // 보통 Order를 이용해서 쿼리를 날리는데 Member를 이용해서 쿼리를 날리는 경우가 생긴다면 설계상의 문제가 있을 확률이 높음.
    // 즉, Order 객체만 Member를 가지고 있으면 되지 Member객체는 Orders를 알 필요가 없다.

    // 다만, 공부하는 입장이니까 그냥 만들자.
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}