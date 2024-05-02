package com.highlight.weather.refactored.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.highlight.weather.refactored.dto.CurrentWeatherResponseDto;
import com.highlight.weather.refactored.service.currentWeather.CurrentWeatherService;
import com.highlight.weather.refactored.utils.Views;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


@Slf4j
@Tag(name = "날씨_refactored", description = "날씨 관련 API_refactored")
@RestController
@RequiredArgsConstructor
@RequestMapping("/weather-refactored2")
public class CurrentWeatherController {

    private final CurrentWeatherService currentWeatherService;
    // DB에서 AXIOS API 요청에 맞춰 각 지역별 일주일 치 날씨 정보 형태로 가공해서 return하는 컨트롤러

    @GetMapping("/get")
    @Operation(summary = "날씨정보 조회", description = "사용자 위치 기반 실시간 날씨 정보를 조회합니다.")
    public ResponseEntity<CurrentWeatherResponseDto> getCurrentWeather (@RequestParam("x") String x, @RequestParam("y") String y) {
        try {
            CurrentWeatherResponseDto weatherResponseDto = currentWeatherService.getCurrentWeather(x, y);
            return ResponseEntity.ok(weatherResponseDto);
        } catch (Exception e) {
            Logger.getLogger("컨트롤러 에러 발생" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


}
