package com.highlight.weather.refactored.dto.currentHourly;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

// 현재위치기반 날씨 응답결과는 중첩된 객체 형태이며, category(온도, 습도, 풍향, 하늘정보, 비올 확률 등이 해당)
// fcstDate는 날짜, fcstTime은 시간 fcstValue는 각 카테고리별 내용에 대응되는 값 입니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class HourlyWeatherApiReqDto {
    @JsonProperty("response")
    private Response response;


    @Getter
    public static class Response {
        @JsonProperty("body")
        private Body body;

    }

    @Getter
    public static class Body {
        @JsonProperty("items")
        private Items items;

    }

    @Getter
    public static class Items {
        @JsonProperty("item")
        private List<Item> item;

    }

    @Getter
    public static class Item {
        @JsonProperty("category")
        private String category;
        @JsonProperty("fcstDate")
        private String fcstDate;
        @JsonProperty("fcstTime")
        private String fcstTime;
        @JsonProperty("fcstValue")
        private String fcstValue;


    }
}