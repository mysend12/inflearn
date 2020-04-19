package entities.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class Member {

    @Id                                 // pk mapping
    private Long id;

    // column명 명시, nullbale: false는 NOTNULL조건, 
    // unique는 이름(랜덤 유니크명이 생성)을 알아보기 힘들기 때문에 사용X,
    // columnDefinition: 특정 DB에 종속적인 DDL을 만들거나 DDL을 직접 명시하고자 할때 사용
    @Column(name = "name", updatable = false, nullable = false, 
            unique = true, 
            columnDefinition = "varchar(100) default 'EMPTY'")
    private String username;

    private Integer age;

    // enum 타입을 사용하고 싶을때, default값은 ORDINAL
    // ORDINAL은 enum 순서를 데이터베이스에 저장한다
    // (ORDINAL은 INTEGER로 들어감, 요구사항이 변경됬을 경우 꼬일 확률이 높아지기 때문에 비추.)
    // STRING은 문자열을 DB에 저장한다. 몇 자 아끼려다가 꼬이면 답없는 에러가 발생하기 때문에 그냥 STRING으로 쓰자.
    @Enumerated(EnumType.STRING)        
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)   // 날짜 타입(날짜, 시간, 날짜+시간 3개로 구분)
    private Date createTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob                                // varchar를 넘어서는 큰 컨텐츠를 사용하고자 할때 사용
    private String description;

    @Transient                          // DB랑 관계없이 메모리에서만 사용하고 싶을 때 사용.
    private int temp;

    // java 8 이상일 경우 이걸 이용하자.
    private LocalDate localDate;        // 알아서 날짜 타입으로 지정됨.
    private LocalDateTime localDateTime;// 알아서 날짜+시간 타입으로 지정됨.

    public Member(){
        
    }

}