package com.highlight.weather.refactored.utils;

// 해당 클래스는 @ResController와 이하 @Json 어노테이션과 상호작용함
// 예로 @JsonView(Views.Public.class)를 DTO내 특정 필드들과 특정 RestController에 발라놓으면
// 서로만 상호작용하여 , 원하는 필드들만 json 직렬화 처리됨
public class Views {
    public static class Public { }
    public static class Internal extends Public { }
}
