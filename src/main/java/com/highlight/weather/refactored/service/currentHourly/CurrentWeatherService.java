package com.highlight.weather.refactored.service.currentHourly;

import com.highlight.weather.refactored.dto.currentHourly.CurrentWeatherApiReqDto;
import com.highlight.weather.refactored.dto.currentHourly.CurrentWeatherResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.UnknownContentTypeException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
// 현재위치기만 실시간 날씨정보 파싱을 수행하는 서비스 클래스입니다.

@Service
@RequiredArgsConstructor
@Log4j2
public class CurrentWeatherService {

    @Value("${api.currentWeather.url}")
    private String currentWeatherUrl;

    @Value("${api.currentWeather.apiKey}")
    private String currentWeatherApiKey;

    @Autowired
    private final RestClient restClient;

    private final Logger logger = LogManager.getLogger(this.getClass());

    public ResponseEntity<?> getCurrentWeather(String x, String y) {
        try {
            Map<String, String> dateTimeParams = dateTimeMethod();
            return sendGetRequest(x, y, dateTimeParams);
        } catch (Exception e) {
            logger.error("Service error: " + e.getMessage(), e);
        }
        return null;
    }

    // 인자가 너무 많은경우 Map<K,V>로 받을 수 있지만 같은 취준생들에게 공유하기 위해서 다 변수로 받음
    // 가독성이 중요하면 아래와 같이 바꿔주세요 여러분
    //     UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url);
    //    queryParams.forEach(builder::queryParam);
    // 만약 인자들이 처음부터 끝까지 사용하기로 정해져있고 key를 알필요 없으면 list가 순회하는게 더 빠르니 이 경우 List를 사용하자
    private ResponseEntity<?> sendGetRequest(String x, String y, Map<String, String> dateTimeParams) throws URISyntaxException {
        String encodedServiceKey = URLEncoder.encode(currentWeatherApiKey, StandardCharsets.UTF_8);  // 서비스 키 인코딩
        String url = UriComponentsBuilder.fromHttpUrl(currentWeatherUrl)
                .queryParam("ServiceKey", encodedServiceKey)
                .queryParam("nx", x)
                .queryParam("ny", y)
                .queryParam("base_date", dateTimeParams.get("baseDate"))
                .queryParam("base_time", dateTimeParams.get("baseTime"))
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 10)
                .queryParam("dataType", "JSON")
                .build(false)// 자동 인코딩 방지하여 쿼리파마리터의 값 그대로 url 구성
                .toString();
        try {

            CurrentWeatherApiReqDto currentWeatherApiReqDto = restClient.get()
                    .uri(new URI(url))
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .toEntity(CurrentWeatherApiReqDto.class)
                    .getBody();
            Map<String, String> weatherData = parseWeatherData(currentWeatherApiReqDto);
            CurrentWeatherResponseDto currentWeatherResponseDto = ToDtoFromMap(weatherData);
            return ResponseEntity.ok(currentWeatherResponseDto);
        } catch (UnknownContentTypeException e) {
            // 공공기관 API는 개발자가 Content-Type을 JSON으로 설정하더라도 알림,에러 등의 정보를 XML로 반환합니다(공식문서 X)
            // 생각을 해보면 공공기관에서 데이터 송수신 매체로 XML로 사용한 역사가 JSON보다 오래되었습니다.
            logger.error("Error response is not JSON, Type will be : " + e.getContentType());
            ResponseEntity<?> response = restClient.get()
                    .uri(new URI(url))
                    .retrieve()
                    .toEntity(String.class);
            return response;
        } catch (RestClientException e) {
            logger.error("Error on RestClient runtime :" + e.getCause());
            throw new RuntimeException("RestClient 에러 : " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException("알 수 없는 에러가 발생: ", e);
        }
    }

    private Map<String, String> dateTimeMethod() {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDateTime tracedTime = now.getMinute() < 30 ? now.minusMinutes(30) : now;
        String time = tracedTime.format(DateTimeFormatter.ofPattern("HHmm"));

        Map<String, String> dateTimeParams = new HashMap<>();
        dateTimeParams.put("baseDate", date);
        dateTimeParams.put("baseTime", time);

        return dateTimeParams;
    }

    private Map<String, String> parseWeatherData(CurrentWeatherApiReqDto currentWeatherApiReqDto) {
        try {
            Map<String, String> weatherData = new HashMap<>();
            currentWeatherApiReqDto.getResponse().getBody().getItems().getItem().forEach(item -> {
                String category = item.getCategory();
                String obsrValue = item.getObsrValue();

                switch (category) {
                    case "T1H":
                        weatherData.put("temperature", obsrValue + "°");
                        break;
                    case "REH":
                        weatherData.put("humidity", obsrValue + "%");
                        break;
                    case "RN1":
                        weatherData.put("rain", obsrValue + "mm");
                        break;
                    case "PTY":
                        Map<String, String> conditions = Map.of(
                                "0", "맑음", "1", "비", "2", "비/눈", "3", "눈",
                                "5", "빗방울", "6", "빗방울 눈날림", "7", "눈날림");
                        weatherData.put("condition", conditions.getOrDefault(obsrValue, obsrValue));
                        break;
                    case "WSD":
                        weatherData.put("wind", obsrValue + "m/s");
                        break;
                }
            });
            return weatherData;
        } catch (NullPointerException e) {
            logger.error("Error parsing JSON response: " + e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    //builder
    private static CurrentWeatherResponseDto ToDtoFromMap(Map<String, String> weatherData) {
        return CurrentWeatherResponseDto.builder()
                .condition(weatherData.get("condition"))
                .humidity(weatherData.get("humidity"))
                .rain(weatherData.get("rain"))
                .temperature(weatherData.get("temperature"))
                .wind(weatherData.get("wind"))
                .build();
    }
}