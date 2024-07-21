package com.highlight.weather.refactored.dto.currentHourly;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// 현재위치기반 날씨 응답결과는 중첩된 객체 형태이며, category(온도, 습도, 풍향, 하늘정보, 비올 확률 등이 해당)
// obsrValue는 각 카테고리별 값입니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentWeatherApiReqDto {
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
        @JsonProperty("obsrValue")
        private String obsrValue;
    }
}
