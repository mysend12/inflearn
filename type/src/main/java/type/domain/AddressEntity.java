package type.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import type.types.Address;

@Entity
@Getter
@Setter
@Table(name = "ADDRESS")
@NoArgsConstructor
public class AddressEntity {
    
    @Id @GeneratedValue
    private Long id;
    private Address address;

    public AddressEntity(Address address){
        this.address = address;
    }

    public AddressEntity(String city, String street, String zipcode){
        this.address = new Address(city, street, zipcode);
    }

    public String getCity(){
        return address.getCity();
    }
    public String getStreet(){
        return address.getStreet();
    }
    public String getZipcode(){
        return address.getZipcode();
    }
}