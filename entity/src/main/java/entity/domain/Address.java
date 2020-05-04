package entity.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    
    private String city;
    private String street;
    private String zipcode;

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;
            
        Address other = (Address) obj;
        if (!getCity().equals(other.getCity()) || 
            !getStreet().equals(other.getStreet()) ||
            !getZipcode().equals(other.getZipcode())
        ) return false;

        return true;
    }

    
}