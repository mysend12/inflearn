package entity.domain;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Item {

    private String name;
    private String price;
    private String stockQuantity;
}