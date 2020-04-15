"# inflearn" 

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
