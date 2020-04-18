package hello;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MEMBER") // table명과 class명이 다를 경우 명시해줘야 한다.
public class Member {

    /**
     * @Colume명과 필드명이 다를 경우 @Colume(name="") 사용
     */

    @Id // pk가 뭔지는 jpa에게 알려줘야 한다.
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Member [id=" + id + ", name=" + name + "]";
    }
}