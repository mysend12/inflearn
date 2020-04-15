package com.bskim.hateoas;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class BasicController {

    /**
     * Spring boot start web에 jacson 라이브러리 포함
     * ObjectMapper 제공.
     
     * spring-boot-starter-hateoas 의존성 추가.
     * 잘 사용은 안되지만 사용되는 예로는 github api가 있음.
     
     * 2.2.4 버전 이후인지 hateoas.Resouce가 hateoas.EntityModel로 바뀜
     * ResourceSupport is now RepresentationModel
     * Resource is now EntityModel
     * Resources is now CollectionModel
     * PagedResources is now PagedModel
     
     * test 결과
     * Body = {"prefix":"hi,","name":"bosung","_links":{"self":{"href":"http://localhost/hello"}}}
     
     * 
     * ObjectMapper를 커스터마이징 하고자 할때는 properties에서 spring.jackson.~~를 이용하여 가능.
     
     
     * HATEOAS
     * REST API의 상태 정보(URL)를 관리하기 위한 메커니즘
     * 
     * 도입 이유(REST API의 단점)
     * 1. END포인트 URL이 정해지면 이를 변경하기 어렵다.
     *  - api의 url을 변경할 경우, 모든 클라이언트의 url까지 수정해야 하므로 다른 api를 지속적으로 추가해야 한다.
     *  - 즉, api 관리가 어려워진다.
     * 
     * 2. 전달받은 정적 자원의 상태에 따른 요소를 서버 단에서 구현하기 어렵기 때문에 클라이언트 단에서 이를 처리해야 한다.
     * 
     * HATEOAS를 사용했을 경우의 장점!
     * 클라이언트가 명시적으로 링크를 작성하지 않고도 서버 측에서 받은 링크의 레퍼런스를 통해 어플리케이션의 상태 및 전이를 표현할 수 있는 기술!
     * 즉, 서버 단에서 URL을 보내주면 클라이언트 단에서는 받은 URL을 그대로 사용하면 된다.
     * 
     * => 이 경우, API의 URL을 변경 시 서버 단의 설정 파일이나 코드 수정만으로 유지 보수가 가능해진다!
     */

    @GetMapping("/hello")
    public EntityModel<Sample> hello(){
        Sample sample = new Sample();
        sample.setName("bosung");
        sample.setPrefix("hi,");

        EntityModel<Sample> sampleResource = new EntityModel<>(sample);
        sampleResource
            .add(linkTo(methodOn(BasicController.class).hello()).withSelfRel());
        return sampleResource;
    }


}