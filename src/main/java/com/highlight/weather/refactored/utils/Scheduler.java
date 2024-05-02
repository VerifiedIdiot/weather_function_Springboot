package com.highlight.weather.refactored.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.highlight.weather.refactored.service.weather.CompleteWeatherService;
import com.highlight.weather.refactored.service.weather.MiddleWeatherService;
import com.highlight.weather.refactored.service.weather.ShortWeatherService;
import com.highlight.weather.refactored.service.weather.WeatherDataSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Scheduler {


    private final MiddleWeatherService middleWeatherService;
    private final ShortWeatherService shortWeatherService;

    private final CompleteWeatherService completeWeatherService;

    private final WeatherDataSaveService weatherDataSaveService;
    private final CacheManager cacheManager;

    // 초 분 시 일 월 요일
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void executeWeatherTasks() throws JsonProcessingException {
        try {
            System.out.println("날씨 스케쥴러 시작 ! ! ! !");
            // 데이터 insert하기전 날씨테이블의 모든 레코드 삭제 , 이는 최신화된 정보만 보관을 위함


            // 지역별 코드
            Map<String, String> locationCode = shortWeatherService.getLocationCode();

            // 단기예보
            Map<String, List<List<String>>> completeShort = shortWeatherService.completeShort(locationCode);

            // 중기예보
            Map<String, List<List<String>>> middleTemp = middleWeatherService.getMiddleTemp(locationCode);
            Map<String, List<List<String>>> middleCondition = middleWeatherService.getMiddleCondition(locationCode);
            Map<String, List<List<String>>> completeMiddle = middleWeatherService.getCompleteMiddle(middleTemp, middleCondition);

            // 단기예보 + 중기예보
            Map<String, List<List<String>>> completeWeather = completeWeatherService.getCompleteWeather(completeShort, completeMiddle);
//            weatherDataSaveService.deleteAllWeatherData();
            // 각 도시별 일주일 날씨 정보 db에 insert
            weatherDataSaveService.saveWeatherData(completeWeather);
            cacheManager.getCache("weather").clear();
            System.out.println("날씨 정보 insert 작동 ! ! ! ! !");
        } catch (ResourceAccessException e) {
            // 로그에 예외 정보 기록
            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }
    // 어플리케이션 최초실행시 스케쥴러를 한번 시작
//    @PostConstruct
//    public void init() {
//        // 서비스 시작 시 한 번 실행할 작업
//        try {
//            executeWeatherTasks();
//
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//    }
}
