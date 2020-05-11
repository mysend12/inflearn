package hello.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


    /**
     * 객체와 테이블 매핑: @Entity, @Table
     * 필드와 컬럼 매핑: @Column
     * 기본 키 매핑: @Id
     * 연관관계 매핑: @ManyToOne, @JoinColumn
     
     * @Entity가 붙은 클래스는 JPA가 관리
     * JPA를 사용해서 테이블과 매핑할 클래스는 @Entity 필수
     
     * 주의사항
     * 기본 생성자 필수(public or protected) -> 동적으로 객체를 다룰 때 필요함.
     * 저장할 필드에 final 사용X.
     
     * 데이터베이스 스키마 자동 생성
     * Application 로딩시점에 테이블을 생성해주는 기능 지원. -> 운영이 아니라 개발 시 로컬에서 유용.
     * 자동으로 생성된 DDL은 운영서버에서 사용하지 않거나, 다듬어서 사용할 것.
     * 테이블이 이미 존재하면 지우고 다시 생성한다.
     
     * <property name="hibernate.hbm2ddl.auto" value="create" />
     * create: 기존테이블 삭제 후 다시 생성
     * create-drop: 어플리케이션 실행 시, 테이블 생성 후 종료 시 테이블을 drop한다.(테스트 시에 사용)
     * update: 변경분만 반영(alter) -> 추가하는 것만 변경, 지우는건 안됨
     * validate: 엔티티와 테이블이 정상 매핑되었는지 확인
     * none: 사용하지 않음 -> 매칭되는게 없어서 실행이 안되는 것. 관례상 none이라고 함.
     
     * 개발 초기: create or update
     * 테스트 서버: update or validate
     * 스테이징/운영 서버: validate or none ( 스키마 자동 생성기능 반드시 끄기!, 테이블 날아가면 답없음 )
     
     * DDL 생성 기능
     * 컬럼명, 유니크, 길이 설정 가능(안적으면 default값, 컬럼명은 필드명과 같음)
     * @Column(name = "컬림이름", unique = true, length = 10)
     *  -> DDL 생성에만 영향을 주고 JPA 실행 로직에는 영향 X.
     
     * 
     */
    
@Entity
@Table(name = "t")  // table 이름 t와 매핑된다. 아무것도 안쓰면 클래스이름과 매핑됨.
public class Team {

    @Id
    private Long id;
    private String teamName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Team(Long id, String teamName) {
        this.id = id;
        this.teamName = teamName;
    }

    public Team() {
    }



}