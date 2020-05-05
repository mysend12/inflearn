# inflearn - https://github.com/mysend12/inflearn/tree/master
인프런 인강

자바 ORM 표준 JPA 프로그래밍 - 기본편

강의 목표
 - 객체와 테이블 설계 매핑 ( 실무 노하우 + 성능을 고려한 설계 )
 - JPA의 내부 동작 방식을 이해 ( 언제 어떤 SQL을 만들어내는지 )
 
 
학습방법 ( 6단계의 실전 예제 )
 - 이론 + 라이브 코딩
 - 강의는 실무적
 - 책은 참고서

JPA 쿼리 방법
 - 1. JPQL(JPA Criteria, QueryDSL) -> 대부분 JPQL로 해결 가능
 - 2. Native SQL(JPA를 사용해도 DB에 종속적인 쿼리가 필요할 때 사용)
 - 3. JDBC API를 직접 사용(MyBatis, SpringJdbcTemplate)

JPQL 소개 -> SQL을 추상화. Entity 객체를 중심으로 개발
 -> 검색 시에도 테이블이 아닌 객체를 대상으로 검색
 -> 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
 -> 어플리케이션이 필요한 데이터만 DB에서 조회하려면, 검색조건이 포함된 SQL 필요

Criteria
