package com.highlight.weather.refactored.dto.weekly;

import com.fasterxml.jackson.annotation.JsonView;
import com.highlight.weather.refactored.entity.WeeklyWeather;
import com.highlight.weather.refactored.utils.Views;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyWeatherDto {


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

    public WeeklyWeather toEntity() {
        return WeeklyWeather.builder()
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
