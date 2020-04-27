package supermappin.domain;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
// @DiscriminatorValue("B")
public class Book extends Item{

    private String author;
}