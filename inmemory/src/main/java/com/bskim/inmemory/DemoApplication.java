package com.bskim.inmemory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	/**
	 * spring-boot-starter-jdbc 의존성을 추가할 경우, hikariCP가 포함
	 * DataSource, JdbcTemplate 빈이 자동으로 추가된다.
	 * 데이터베이스 설정을 하지 않을 경우, spring은 자동으로 in-memory database를 사용한다.
	 * 
	 * 
	 * spring에서 지원하는 jdbcTemplate를 사용하면 jdbc 쿼리를 실행할 때 예외처리를 알아서 해주므로, 훨씬 안전하게 사용할 수 있다.
	 * jdbc 에러를 확인할 때 가독성이 높은 로그를 확인할 수 있다.(에러로그를 잘만들어 놨다?)
	 * 
	 * 
	 * 
	 */
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
