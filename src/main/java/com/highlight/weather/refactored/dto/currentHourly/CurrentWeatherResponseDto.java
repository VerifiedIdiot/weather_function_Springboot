package com.highlight.weather.refactored.dto.currentHourly;

import lombok.*;

// view 혹은 프론트 서버로 실질적으로 사용자에게 전달하고 싶은 정보들입니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentWeatherResponseDto {
    // 날씨 상태 (흐림, 맑음 등)
    private String condition;
    // 습도
    private String humidity;
    // 비올 확률
    private String rain;
    // 온도
    private String temperature;
    // 풍향
    private String wind;
}
