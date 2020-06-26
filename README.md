# Getting Started

### Run

> 방법 1
>
> % ./gradlew build
>
> % java -jar build/libs/spread-0.0.1-SNAPSHOT-boot.jar
>
> 방법 2
>
> %  ./gradlew bootrun

### Feature

* Spring Boot 2.3.1.RELEASE
* Spring Data Jpa 2.3.1
* Junit jupiter 5.6.2

### Solution
  * 인증 서버와 게이트웨이가 존재한다는 가정하에 내부에서 호출하는 시나리오입니다.
  * MSA에 적합하도록 작은 단위의 `Master` > `Detail`의 구조로 구성하였습니다.
  * DBMS는 H2를 사용하였고 개발과 테스트를 위하여 `memory` 테스트 시엔 `file` 을 사용하였습니다.
  * 좀 더 빠르고 정확한 개발을 위하여 `Http Request`를 활용하였습니다.
  * 동시에 데이터를 가져가는 경우에 대한 전략으로`@Version`을 사용하였습니다.
  * 예외 처리는 `HttpStatus`로 표현하여 `Response Header` 로 응답 가능하도록 구성하였습니다.
  
### Reference Documentation
For further reference, please consider the following sections:

* [Spring Web](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Junit jupiter](https://junit.org/junit5/docs/current/user-guide/)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)



