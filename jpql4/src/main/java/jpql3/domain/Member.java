package jpql3.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@ToString(exclude = "team")
@NamedQuery(
    name = "Member.findByFirstName",
    query = "SELECT m FROM Member m WHERE m.firstName = :firstName"
)
public class Member {
    
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private Integer age;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;

    @JoinColumn(name = "TEAM_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }

    public Member(Integer age, String firstName, String lastName) {
        this(age, firstName, lastName, null);
    }
    public Member(Integer age, String firstName, String lastName, Team team) {
        if(team != null)
            changeTeam(team);

        this.age = age;
        this.firstName = firstName;
        this.lastName = lastName;
    
    }
}