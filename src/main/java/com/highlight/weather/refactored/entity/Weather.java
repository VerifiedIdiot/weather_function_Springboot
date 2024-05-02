package com.highlight.weather.refactored.entity;

import com.highlight.weather.refactored.dto.WeatherDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity(name = "refactoredWeather")
@Table(name = "weather_refactored_tb")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Weather {
    @Id
    @Column(name = "weather_refactored_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weather_refactored_seq")
    private Long id;
    //    @Column(unique = true)
    private String region ;

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


    public WeatherDto toDto() {
        return WeatherDto.builder()
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
