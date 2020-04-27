package supermappin.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
// @DiscriminatorValue("A")
public class Album extends Item {
    private String artist;
}