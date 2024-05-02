# 날씨기능 (스프링부트)
- 사이드 프로젝트에서 구현했던 날씨 기능을 모듈화 및 마이그레이션
- 불필요한 라이브러리 및 코드 제거
 
## 사용 스택
- Java
- Spring Boot
- Hibernate JPA
- MySQL
- Swagger

## 스웨거 주소
- Swagger: (http://localhost:8080/swagger-ui/index.html#)

## 사전 준비 사항 
- MySQL서버 설정, API KEY 발급(https://apihub.kma.go.kr/) 

## application.properties 설정
![image](https://github.com/VerifiedIdiot/weather_function/assets/107241795/03e4ce5e-535d-48ff-85d0-8536f8541035)

## 프로젝트 구조
 - 프로젝트 구조는 과거 사이드프로젝트에서 모듈화만 진행한 legacy, 마이그레이션과 리팩토링이 진행된 refactored로 구분이된다.
![image](https://github.com/VerifiedIdiot/weather_function/assets/107241795/f11adb04-4c73-4b7f-8a57-49882924dd71)
 
## 변경 사항
- Java 11 -> Java 17
- Spring Boot 2.5.5 -> Spring Boot 3.2.5
- RestTemplate -> RestClient -> WebClient
- springfox-swagger2:2.9.2 -> springdoc-openapi-starter-webmvc-ui:2.0.2
- Async방식의 API 요청
- 캐싱추가

## 개선 사항
- Swagger 2.0에서 springdoc-openapi로의 업그레이드로 OpenAPI Specification 3.0 및 Spring 통합을 개선하여 API 문서 생성 및 관리를 향상 
- 스프링부트 3.x.x 부터 더이성 지원지 중단된 RestTemplate 대신 RestClient사용 및 코드간소화 -> 비동기방식인 WebClient로 대체
- 로직이 실행되는동안 API 응답 지연으로 인해 대부분의 시간이 할애됨, 그래서 비동기 요청처리 @Async를 통해 쓰레드를 조절하여 시간을 단축
- 데이터 insert 시 flush & clear 사용 대신 saveAll로 일괄처리
- 일주일 날씨정보를 프론트에서 요청시에 여전히 동일한 날씨정보인 경우 캐싱을 통해 반복된 DB조회 방지

# 결과 

# 변경전 
일주일 날씨정보를 얻기위해 동기적으로 처리한경우 api응답이 소요된시간은 다음과 같다
![image](https://github.com/VerifiedIdiot/weather_function/assets/107241795/ea2ac179-b2f3-48ef-a345-d0da769c2b82)
</br>
DB에서 일주일에 해당하는 날씨정보를 프론트에서 응답받는데 소요된 시간은 다음과 같다
![image](https://github.com/VerifiedIdiot/weather_function/assets/107241795/0d930346-32c6-4bfa-aa57-e0fe347f13d3)


# 변경후 
일주일 날씨정보를 얻기위해 비동기적으로 처리한경우 api응답이 소요된시간은 다음과 같다
![image](https://github.com/VerifiedIdiot/weather_function/assets/107241795/a271c35f-eeb1-47d8-87c3-d56017cf0ee8)
</br>
서버에서 미리 캐싱된 일주일에 해당하는 날씨정보를 프론트에서 응답받는데 소요된 시간은 다음과 같다
![image](https://github.com/VerifiedIdiot/weather_function/assets/107241795/5952ca20-322c-4c3f-a830-a219739cb8ea)

## 마무리
 - 캐싱 대신 Redis를 사용하려하였으나, 키-값 처리에 특화되어있고 보통 키-값의 구조로 대표되는 세션이나 API 쿼리파라미터 변수에 사용됨, 억지로 우겨넣는것은 주객전도였다
 - 결국에 외부 API 서버의 퀄리티가 좋지않아 같은 로직일 지라도 처리되는 시간이 많게는 10배 이상 차이난다, 명세서에 공공기관API를 사용하라고 명시되지않는이상 신뢰도가 높은 해외 유료API를 사용하자
 - 비교적 저수준인 JDBC, HttpClient를 사용하였으면 어땠을까??
 - 모듈화가 성공적으로 끝났으니 도커의 컨테이너에 등록하면 어떨까??




