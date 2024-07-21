package com.highlight.weather.refactored.dto.currentHourly;


import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HourlyWeatherResponseDto {
    // 해당 Map의 Key는 yyyyMMddHH00 형식입니다.
    private Map<String, WeatherDetail> forecastDateTime;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder

    public static class WeatherDetail {
        // 온도
        private String temperature;
        // 습도
        private String humidity;
        // 날씨 상태
        private String weatherCondition;
        // 하늘 상태
        private String sky;
        // 비 올 확률
        private String rainChance;
        // 강수량
        private String rainAmount;
        // 풍향
        private String windSpeed;
    }
}
