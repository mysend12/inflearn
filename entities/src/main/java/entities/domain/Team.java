package entities.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(              // 시퀀스 매핑(생성) 전략
    name = "TEAM_SEQ_GENERATOR",
    sequenceName = "TEAT_SEQ",
    initialValue = 1,
    allocationSize = 1
)
public class Team {

    /**
     * @GeneratedValue
     * GenerationType.AUTO: 기본 키 생성을 DB에 위임(Auto_increment)
     * GenerationType.SEQUENCE: 시퀀스를 생성하여 사용.
     * GenerationType.TABLE: table전략
     * GenerationType.IDENTITY: id값을 DB에 들어가는 시점에 알 수 있다.
     *  -> 영속성 컨텍스트(1차 캐시)를 사용할 때 에러사항이 생길 수 있기 때문에 JPA가 persist(entity) 시점에 바로 insert쿼리를 날린다.
     *  -> 값을 넣고 바로 return을 받아 키 값을 영속성 컨텍스트에 저장한다. ( select문을 따로 날리진 않음 )
     
     * generator를 사용하면 해당 시퀀스를 사용
     
     * TABLE 전략
     * 키 생성 전용 테이블을 하나 만들어서 데이터베이스 시퀀스를 흉내내는 전략 -> 모든 DB에 적용이 가능하지만 성능적인 문제가 발생.
     * 클래스에 @TableGenerator(name, table, pkColumnValue, allocationSize)를 적고 사용

     * 권장하는 식별자 전략
     * 1. 기본 키 제약 조건: NotNull, unique, 바뀌면 안된다.
     *  -> 미래까지 이 조건을 만족하는 자연키는 찾기 어려움으로 대리키(대체키)를 사용하자.
     
     * 권장: Long + 대체키 + 키 생성 전략 사용
     * 비즈니스를 키로 끌고오지 말자! -> 시퀀스나 auto_increment 등을 사용하자.
     
     * GenerationType.SEQUENCE는 DB에 nextcall 쿼리(??)를 날려서 시퀀스를 호출한다.
     * allocationSize = 50(default), db에서 nextcall을 사용하여 시퀀스 값을 가져올 때, 
     * 2번 사용해서 미리 50개를 가져와서 메모리에서 시퀀스를 사용한다.
     * -> DB의 시퀀스는 51이 되고, 애플리케이션에서 1~50까지 값을 증가시키며 넣어주는 형식.
     * -> 만약 너무 많은 시퀀스를 가져오고 사용하지 않으면, DB에 그만큼의 숫자구멍이 생길 수 있기 때문에 사용할 만큼의 적절한 숫자 유지!
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEAM_SEQ_GENERATOR")
    private Long id;

    // @Column(name = "create_time", nullable = false)
    // private LocalDate createDate;

    private String teamName;

    public Team(){}
    public Team(String teamName){this.teamName = teamName;}


}