package com.highlight.weather.refactored.controller.currentHourly;


import com.highlight.weather.refactored.service.currentHourly.CurrentWeatherService;
import com.highlight.weather.refactored.service.currentHourly.HourlyWeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// 현재위치기반 실시간 날씨정보, 현재 위치기반 한시간단위 날씨정보 (최대 24시간)
// 대한민국 대표 지역 (울릉도 X) 일주일간 오전 오후 날씨에 대한 정보를 return받을 수 있는 엔드포인트입니다.

@Tag(name = "날씨_refactored", description = "날씨 관련 API_refactored")
@RestController
@RequiredArgsConstructor
@RequestMapping("/refactored-daily")
public class WeatherController {

    private final CurrentWeatherService currentWeatherService;
    private final HourlyWeatherService hourlyWeatherService;

    @GetMapping("/get-current")
    @Operation(summary = "날씨정보 조회", description = "사용자 위치 기반 실시간 날씨 정보를 조회합니다.")
    public ResponseEntity<?> getCurrentWeather(@RequestParam("x") String x, @RequestParam("y") String y) {
        return ResponseEntity.ok(currentWeatherService.getCurrentWeather(x, y).getBody());
    }

    @GetMapping("/get-hourly")
    @Operation(summary = "날씨정보 조회", description = "사용자 위치 기반 한시간 단위 하루의 날씨 정보를 조회합니다(최대 24시간).")
    public ResponseEntity<?> getHourlyWeather(@RequestParam("x") String x, @RequestParam("y") String y) {
        return ResponseEntity.ok(hourlyWeatherService.getHourlyWeather(x, y).getBody());
    }


}
