# inflearn - https://github.com/mysend12/inflearn/tree/master

스프링 부트 개념과 활용 ( 백기선님 강좌, 주석은 BasicController 혹은 DemoApplication에... )

inflearn

 - spring web MVC 1~6 부


thymeleaf/thymeleaf ( spring web MVC 7~9부 )
 - thymeleaf, htmlutil(test코드)
 - exception 처리 ( exceptionHandler, 404.html )


hateoas ( 응답 정보에 url을 포함해서 보낼 수 있는 기술? )
 - api의 url(주소)변경 시, 클라이언트 단의 소스까지 수정하거나 이게 번거롭다면 새로운 api를 추가해야 한다.
 - 하지만 hateoas를 이용한다면 응답 정보에 api의 주소가 포함되어 있기 때문에 클라이언트 단에서는 받은 url을 그대로 사용하면 된다.
 - 즉, api의 url 변경 시, 서버 단에서의 수정만으로 손쉽게 url 변경이 가능해진다.


inmemory ( h2 database 이용, 스프링 데이터는 데이터베이스를 설치해야 하므로 대부분 생략. )


cors


security


actuator
 - https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-endpoints 참고하여 적절하게 설정하여 사용할 것.
 - 개발중인 서버에 spring-boot-starter-actuator 의존성을 추가한다.
 - 기본적으로 /actuator를 이용하여 text로 운영정보를 확인할 수 있지만 보기 안좋다.
 - 콘솔창에서 jconsole을 입력하여 나름대로 보기좋게 볼 수 있다.


spring boot admin(actuator를 쉽게 볼수 있고 운영 중에 프로젝트의 로깅 레벨을 쉽게 바꿀 수 있다.)
 - 프로젝트를 하나 생성하여, spring-boot-admin-starter-server 의존성을 주입하고 DemoApplication에 @EnableAdminServer를 추가한다.
 - 기존에 개발했던 프로젝트에 actuator와 spring-boot-admin-starter-client 의존성을 추가 후, application.properties에 spring.boot.admin.client.url=http://localhost:8000 를 추가한다.(뒤의 url은 admin 주소)
