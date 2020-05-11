package type.types;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
// @Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Period {
    
    @Column(name = "START_DATE")
    private LocalDateTime startDate;
    @Column(name = "END_DATE")
    private LocalDateTime endDate;
    
    @Override
    public boolean equals(Object object) {
        if(this == object) return true;
        if(object == null || !(object instanceof Period))
            return false;
        Period period = (Period) object;
        return startDate.equals(period.getStartDate()) && 
            endDate.equals(period.getEndDate());
    }

    @Override
    public int hashCode(){
        return Objects.hash(startDate, endDate);
    }
}