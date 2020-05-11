package type.types;

import java.util.Objects;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
// @Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    
    private String city;
    private String street;
    private String zipcode;

    @Override
    public boolean equals(Object object) {
        if(this == object) return true;
        if(object == null || !(object instanceof Address))
            return false;
        Address address = (Address) object;
        return city.equals(address.getCity()) && 
            street.equals(address.getStreet()) && 
            zipcode.equals(address.getZipcode())
        ;
    }

    @Override
    public int hashCode(){
        return Objects.hash(city, street, zipcode);
    }

}