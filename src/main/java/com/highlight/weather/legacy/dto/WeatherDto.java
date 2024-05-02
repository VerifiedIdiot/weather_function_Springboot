package com.highlight.weather.legacy.dto;

import com.highlight.weather.legacy.entity.Weather;
import com.highlight.weather.legacy.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;



@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherDto {


    private long id;

    private String region;
    @JsonView(Views.Public.class)
    private int weatherDate;
    @JsonView(Views.Public.class)
    private int morningTemperature;
    @JsonView(Views.Public.class)
    private int morningRainPercent;
    @JsonView(Views.Public.class)
    private String morningWeatherCondition;
    @JsonView(Views.Public.class)
    private int afternoonTemperature;
    @JsonView(Views.Public.class)
    private int afternoonRainPercent;
    @JsonView(Views.Public.class)
    private String afternoonWeatherCondition;
    public Weather toEntity() {
        return Weather.builder()
                .region(this.getRegion())
                .weatherDate(this.getWeatherDate())
                .morningTemperature(this.getMorningTemperature())
                .morningRainPercent(this.getMorningRainPercent())
                .morningWeatherCondition(this.getMorningWeatherCondition())
                .afternoonTemperature(this.getAfternoonTemperature())
                .afternoonRainPercent(this.getAfternoonRainPercent())
                .afternoonWeatherCondition(this.getAfternoonWeatherCondition())
                .build();
    }
}
