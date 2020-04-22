package entity.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Team {

    @Id @Column(name = "TEAM_ID")
    @GeneratedValue
    private Long id;
    private String name;

    // Member의 team변수와 연결되어있다. 1:M에서 뭐랑 연결되어 있는지 적어주기.
    @OneToMany(mappedBy = "team")   
    private List<Member> members = new ArrayList<>();   // 관례상 arrayList로 초기화 해둔다.
}