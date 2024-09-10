package com.highlight.weather.refactored.controller.weekly;

import com.fasterxml.jackson.annotation.JsonView;
import com.highlight.weather.refactored.dto.weekly.WeeklyWeatherDto;
import com.highlight.weather.refactored.service.weekly.*;
import com.highlight.weather.refactored.utils.Views;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



@Tag(name = "날씨_refactored", description = "날씨 관련 API_refactored")
@RestController
@Controller("refactoredWeatherController")
@RequiredArgsConstructor
@RequestMapping("/refactored-weekly")
public class WeatherController {


    private final ShortWeatherService shortWeatherService;
    private final MiddleWeatherService middleWeatherService;
    private final CompleteWeatherService completeWeatherService;
    private final WeatherDataSaveService weatherDataSaveService;
    private final WeatherToFrontService weatherToFrontService;
    private final Logger logger = LogManager.getLogger(this.getClass());

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
    @PostMapping("/insert")
    public ResponseEntity<?> insertForcasts() {
        try {
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
//            Map<String, List<List<String>>> completeMiddle = middleWeatherService.getCompleteMiddle(middleTemp, middleCondition);
//
////             단기예보 + 중기예보
//            Map<String, List<List<String>>> completeWeather = completeWeatherService.getCompleteWeather(completeShort, completeMiddle);

//             각 도시별 일주일 날씨 정보 db에 insert
//            weatherDataSaveService.saveWeatherData(completeWeather);

            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime) / 1000;
            logger.info("Duration: " + duration + " seconds");
            return ResponseEntity.ok(middleCondition);

        } catch (Exception e) {

            return ResponseEntity.notFound().build();
        }
    }

    // DB에서 AXIOS API 요청에 맞춰 각 지역별 일주일 치 날씨 정보 형태로 가공해서 return하는 컨트롤러
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/get")
    @Operation(summary = "날씨정보 조회", description = "데이터베이스에서 일주일에 해당하는 날씨정보를 요청 및 조회합니다.")
    @JsonView(Views.Public.class)
    public ResponseEntity<Map<String, List<WeeklyWeatherDto>>> getForcasts() {

        Map<String, List<WeeklyWeatherDto>> weeklyWeather = weatherToFrontService.getWeatherData();
        return ResponseEntity.ok(weeklyWeather);
    }

}
