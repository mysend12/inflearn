package entity.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Category {

    @Id @GeneratedValue
    private Long id;
    private String name;


    
    // 셀프 조인...
    @ManyToOne
    @JoinColumn(name ="PARENT_ID")
    private Category parent;
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "CATEGORY_ITEM", 
            joinColumns = @JoinColumn(name = "CATEGORY_ID"),    // 내가 조인해야 하는 컬럼
            inverseJoinColumns = @JoinColumn(name = "ITEM_ID")  // 반대편 테이블이 조인해야하는 컬럼
    )
    private List<Item> items = new ArrayList<>();
    
}