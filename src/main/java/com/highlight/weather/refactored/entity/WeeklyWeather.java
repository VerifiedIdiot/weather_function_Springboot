package com.highlight.weather.refactored.entity;

import com.highlight.weather.refactored.dto.weekly.WeeklyWeatherDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity(name = "weatherFunction")
@Table(name = "weekly_weather_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyWeather {
    @Id
    @Column(name = "weekly_weather_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weekly_weather_seq")
    private Long id;
    private String region;

    private int weatherDate;

    private int morningTemperature;

    private int morningRainPercent;

    private String morningWeatherCondition;

    private int afternoonTemperature;

    private int afternoonRainPercent;

    private String afternoonWeatherCondition;


    private Date regDate;


    @PrePersist // DB에 INSERT 되기 전에 실행되는 메소드
    public void prePersist() {
        regDate = new Date();
    }


    public WeeklyWeatherDto toDto() {
        return WeeklyWeatherDto.builder()
                .id(this.getId())
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
