package com.highlight.weather.refactored.service.weekly;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service("refactoredWeatherAbstract")
@RequiredArgsConstructor
public abstract class WeatherAbstract {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHH");
    //    private final RestClient restClient;
    private final WebClient webClient; // RestClient 대신 WebClient사용하여 논브로킹을 사용한다


    @Async("weatherTaskExecutor")
    protected CompletableFuture<String> sendGetRequest(String url, String apiKey, Map<String, String> queryParams) {
        // 동적 URI 생성 파라미터로 전달받은 URI 정보를 바탕으로 새로운 인스턴스를 생성해서 간편하다 !!
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        queryParams.forEach(builder::queryParam);
        return webClient.get()
                .uri(builder.toUriString())
                .header("authKey", apiKey)
                .retrieve()
                .toEntity(String.class)
                .map(HttpEntity::getBody) // ResponseEntity에서 body 추출
                .toFuture(); // CompletableFuture로 변환
    }

    private int formatDate(LocalDateTime dateTime) {
        return Integer.parseInt(dateTime.format(DATE_TIME_FORMATTER));
    }

    protected Map<String, Integer> shortDaysParam() {
        LocalDate today = LocalDate.now();

        LocalDateTime yesterdayNoon = LocalDateTime.of(today.minusDays(1), LocalTime.of(12, 0));
        LocalDateTime todayNoon = LocalDateTime.of(today, LocalTime.of(12, 0));

        Map<String, Integer> shortDateParams = new HashMap<>();
        shortDateParams.put("today", formatDate(yesterdayNoon));
        shortDateParams.put("2DaysAfter", formatDate(todayNoon));
//        System.out.println("오늘  : " + formatDate(yesterdayNoon) + "이틀 뒤 : " + formatDate(todayNoon));
        return shortDateParams;
    }

    protected Map<String, String> shortQueryParams(String regCode, Map<String, Integer> shortDateParams) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("reg", regCode);
        queryParams.put("tmfc1", String.valueOf(shortDateParams.get("today")));
        queryParams.put("tmfc2", String.valueOf(shortDateParams.get("2DaysAfter")));
        queryParams.put("help", "0");

        return queryParams;
    }

    protected Map<String, Integer> middleDaysParam() {
        LocalDate now = LocalDate.now();
        Map<String, Integer> dateParams = new HashMap<>();
        dateParams.put("today", formatDate(LocalDateTime.of(now, LocalTime.MIDNIGHT)));
        dateParams.put("tomorrow", formatDate(LocalDateTime.of(now.plusDays(1), LocalTime.MIDNIGHT)));
        dateParams.put("sevenDaysAfter", formatDate(LocalDateTime.of(now.plusDays(6), LocalTime.MIDNIGHT)));

        return dateParams;
    }

    protected Map<String, String> middleQueryParams(String regCode, Map<String, Integer> dateParams) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("reg", regCode);
        queryParams.put("tmef1", String.valueOf(dateParams.get("tomorrow")));
        queryParams.put("tmef2", String.valueOf(dateParams.get("sevenDaysAfter")));
        queryParams.put("help", "0");

        return queryParams;
    }
}
