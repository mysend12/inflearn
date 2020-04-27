package supermappin.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass // 공통적으로 컬럼만 상속받고 싶을때
public abstract class BaseEntity {

    @Column(name = "INSERT_MEMBER")
    private String createdBy;
    @Column(name = "INSERT_DATE")
    private LocalDateTime createdDate;
    private String modifiedBy;
    private LocalDateTime lastModifiedDate;
}