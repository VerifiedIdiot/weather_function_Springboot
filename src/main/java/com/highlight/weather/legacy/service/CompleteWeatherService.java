package com.highlight.weather.legacy.service;

import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CompleteWeatherService {
    public Map<String, List<List<String>>> getCompleteWeather(Map<String, List<List<String>>> completeShort, Map<String, List<List<String>>> completeMiddle) {
        try {
            System.out.println("완전한 날씨 데이터 취합 시작");
            Map<String, List<List<String>>> completeWeather = new HashMap<>();

            // completeShort와 completeMiddle의 데이터 병합
            completeShort.forEach((key, value) -> completeWeather.merge(key, value, this::mergeLists));
            completeMiddle.forEach((key, value) -> completeWeather.merge(key, value, this::mergeLists));

            System.out.println("데이터 병합 됨");
            // 날짜별로 데이터 정렬
            sortWeatherData(completeWeather);
//            System.out.println(completeWeather);
            return completeWeather;
        } catch (Exception e) {
            System.out.println("완전한 날씨 데이터 취합 실패: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    // 중복된 날짜 데이터를 제거하고 두 리스트를 병합하는 메서드
    private List<List<String>> mergeLists(List<List<String>> list1, List<List<String>> list2) {
        Map<String, List<String>> map = new HashMap<>();

        // list1과 list2를 순회하면서 맵에 추가
        // item의 0번 인덱스의 값이 중복이면 put X, 존재하지 않는 값이면 put
        for (List<String> item : list1) {
            map.putIfAbsent(item.get(0), item);
        }
        for (List<String> item : list2) {
            map.putIfAbsent(item.get(0), item);
        }

        // 맵의 값들을 리스트로 반환하여, 각 날짜에 대한 첫 번째 항목만 유지
        return new ArrayList<>(map.values());
    }

    // 날짜 순으로 데이터를 정렬하는 메서드
    private void sortWeatherData(Map<String, List<List<String>>> weatherData) {
        for (Map.Entry<String, List<List<String>>> entry : weatherData.entrySet()) {
            entry.getValue().sort(Comparator.comparing(o -> o.get(0)));
        }
    }
}