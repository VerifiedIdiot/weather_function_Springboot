//package com.highlight.weather.utils;
//
//
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.highlight.weather.service.CompleteWeatherService;
//import com.highlight.weather.service.MiddleWeatherService;
//import com.highlight.weather.service.ShortWeatherService;
//import com.highlight.weather.service.WeatherDataSaveService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.ResourceAccessException;
//
//import javax.annotation.PostConstruct;
//import java.util.List;
//import java.util.Map;
//
//@Component
//@RequiredArgsConstructor
//public class Scheduler {
//
//
//    private final MiddleWeatherService middleWeatherService;
//    private final ShortWeatherService shortWeatherService;
//
//    private final CompleteWeatherService completeWeatherService;
//
//    private final WeatherDataSaveService weatherDataSaveService;
//
//
//
//    @PostConstruct
//    public void init() {
//        // 서비스 시작 시 한 번 실행할 작업
//        try {
//            executeWeatherTasks();
////            executeStrayTasks();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            // 다른 예외에 대한 처리
//            e.printStackTrace();
//        }
//    }
//
//
//
//    // 초 분 시 일 월 요일
//    @Scheduled(cron = "0 0 6 * * ?") // 매일 아침 6시에 실행
//    public void executeWeatherTasks() throws JsonProcessingException {
//        try {
//            System.out.println("날씨 스케쥴러 시작 ! ! ! !");
//            // 데이터 insert하기전 날씨테이블의 모든 레코드 삭제 , 이는 최신화된 정보만 보관을 위함
//
//
//            // 지역별 코드
//            Map<String, String> locationCode = shortWeatherService.getLocationCode();
//
//            // 단기예보
//            Map<String, List<List<String>>> completeShort = shortWeatherService.completeShort(locationCode);
//
//            // 중기예보
//            Map<String, List<List<String>>> middleTemp = middleWeatherService.getMiddleTemp(locationCode);
//            Map<String, List<List<String>>> middleCondition = middleWeatherService.getMiddleCondition(locationCode);
//            Map<String, List<List<String>>> completeMiddle = middleWeatherService.getCompleteMiddle(middleTemp, middleCondition);
//
//            // 단기예보 + 중기예보
//            Map<String, List<List<String>>> completeWeather = completeWeatherService.getCompleteWeather(completeShort, completeMiddle);
//            weatherDataSaveService.deleteAllWeatherData();
//            // 각 도시별 일주일 날씨 정보 db에 insert
//            weatherDataSaveService.saveWeatherData(completeWeather);
//            System.out.println("날씨 정보 insert 작동 ! ! ! ! !");
//        } catch (ResourceAccessException e) {
//            // 로그에 예외 정보 기록
//            e.printStackTrace();
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//
//    }
//
//
//
//}
