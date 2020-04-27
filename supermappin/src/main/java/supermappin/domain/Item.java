package supermappin.domain;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.Getter;
import lombok.Setter;

/**
 * Item
 */
@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED) // 1. 각각 테이블로 구성.
// @Inheritance(strategy = InheritanceType.SINGLE_TABLE)   // 2. 한 테이블로 구성
// @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)   // 3. 서브 테이블들로 구성(부모 테이블은 X)
@DiscriminatorColumn
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private Integer price;
    
}