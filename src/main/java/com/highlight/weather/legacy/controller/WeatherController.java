package com.highlight.weather.legacy.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.highlight.weather.legacy.dto.WeatherDto;
import com.highlight.weather.legacy.service.*;

import com.highlight.weather.legacy.utils.Views;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Map;


@Slf4j
@Tag(name = "날씨_legacy", description = "날씨 관련 API_legacy")
@RestController
@Controller("legacyWeatherController")
@RequiredArgsConstructor
@RequestMapping("/weather-legacy")
public class WeatherController {

    private final ShortWeatherService shortWeatherService;
    private final MiddleWeatherService middleWeatherService;
    private final CompleteWeatherService completeWeatherService;
    private final WeatherDataSaveService weatherDataSaveService;
    private final WeatherToFrontService weatherToFrontService;


    @PostMapping("/insert")
    @Operation(
            summary = "날씨데이터 Insert",
            description = "기상청API허브에서 날씨정보를 받아와 데이터베이스에 Insert합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공적으로 Insert되었습니다.."
                    )
            }
    )
    public ResponseEntity<?> insertForcasts() {
        try {
            System.out.println("대한민국 날씨정보 insert 시작");
            // 시간을 기록하기위한 시간 메서드
            long startTime = System.currentTimeMillis();
            // 지역별 코드

            Map<String, String> locationCode = shortWeatherService.getLocationCode();

            // 단기예보
            Map<String, List<List<String>>> completeShort = shortWeatherService.completeShort(locationCode);

            // 중기예보
//            Map<String, String> locationCode = middleWeatherService.getLocationCode();
            Map<String, List<List<String>>> middleTemp = middleWeatherService.getMiddleTemp(locationCode);
            Map<String, List<List<String>>> middleCondition = middleWeatherService.getMiddleCondition(locationCode);
            Map<String, List<List<String>>> completeMiddle = middleWeatherService.getCompleteMiddle(middleTemp,middleCondition);

            // 단기예보 + 중기예보
            Map<String, List<List<String>>> completeWeather = completeWeatherService.getCompleteWeather(completeShort, completeMiddle);
            weatherDataSaveService.deleteAllWeatherData();
            // 각 도시별 일주일 날씨 정보 db에 insert
            weatherDataSaveService.saveWeatherData(completeWeather);

            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime) / 1000;

            System.out.println("Duration: " + duration + " seconds");
//            return ResponseEntity.ok("단기 확인 바람");
            return ResponseEntity.ok(completeWeather);
        } catch (Exception e) {

            return ResponseEntity.notFound().build();
        }
    }

    // DB에서 AXIOS API 요청에 맞춰 각 지역별 일주일 치 날씨 정보 형태로 가공해서 return하는 컨트롤러
    @GetMapping("/get")
    @Operation(summary = "날씨정보 조회", description = "데이터베이스에서 일주일에 해당하는 날씨정보를 요청 및 조회합니다.")
    @JsonView(Views.Public.class)
    public ResponseEntity<Map<String, List<WeatherDto>>> getForcasts () {

        Map<String, List<WeatherDto>> weeklyWeather = weatherToFrontService.getWeatherData();
        return ResponseEntity.ok(weeklyWeather);
    }



//    @GetMapping("/get-by-client")
//    @Operation(summary = "RestClient 확인용", description = "RestTemplate 대신 RestClient를 사용하여 API를 사용해봅니다")
//    public ResponseEntity<?> getByRestClient () {
//
//        Map<String, String> locationCode = restClientService.getCodes();
//        return ResponseEntity.ok(locationCode);
//    }
}