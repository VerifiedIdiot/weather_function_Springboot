package com.highlight.weather.refactored.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentWeatherResponseDto {

    private String condition;

    private String humidity;

    private String rain;

    private String temperature;

    private String wind;
}
