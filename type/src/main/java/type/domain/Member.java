package type.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import type.types.Address;
import type.types.Period;

@Getter
@Setter
@Entity
public class Member {
    
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;

    // 기간 Period
    @Embedded
    private Period workPeriod;

    // 주소
    @Embedded
    private Address homeAddress;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "city", column=@Column(name = "WORK_CITY")),
        @AttributeOverride(name = "street", column=@Column(name = "WORK_STREET")),
        @AttributeOverride(name = "zipcode", column=@Column(name = "WORK_ZIPCODE"))
    })
    private Address worAddress;

    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD", 
                joinColumns = @JoinColumn(name = "MEMBER_ID"))
    @Column(name = "FOOD_NAME") // 값이 String 하나밖에 없기 때문에..
    private Set<String> favoriteFoods = new HashSet<>(); 

    // @ElementCollection
    // @CollectionTable(name = "ADDRESS", 
    //             joinColumns = @JoinColumn(name = "MEMBER_ID"))
    // private List<Address> addressHistory = new ArrayList<>();
    @JoinColumn(name = "MEMBER_ID")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressEntity> addressHistory = new ArrayList<>();
}