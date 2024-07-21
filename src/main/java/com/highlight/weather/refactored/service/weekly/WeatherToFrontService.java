package com.highlight.weather.refactored.service.weekly;

import com.highlight.weather.refactored.dto.weekly.WeeklyWeatherDto;
import com.highlight.weather.refactored.entity.WeeklyWeather;
import com.highlight.weather.refactored.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("refactoredWeatherToFrontService")
public class WeatherToFrontService {
    @Autowired
    private WeatherRepository weatherRepository;

    @Cacheable("weather")
    public Map<String, List<WeeklyWeatherDto>> getWeatherData() {
        List<WeeklyWeather> weeklyWeatherList = weatherRepository.findAll();
        Map<String, List<WeeklyWeatherDto>> weatherDataMap = new HashMap<>();

        for (WeeklyWeather weeklyWeather : weeklyWeatherList) {
            WeeklyWeatherDto weeklyWeatherDto = weeklyWeather.toDto();
            String region = weeklyWeatherDto.getRegion();

            // 해당 지역에 대한 리스트가 맵에 존재하는지 확인
            List<WeeklyWeatherDto> weeklyWeatherDtos = weatherDataMap.get(region);
            if (weeklyWeatherDtos == null) {
                // 리스트가 존재하지 않으면 새로운 리스트를 생성하고 맵에 추가
                weeklyWeatherDtos = new ArrayList<>();
                weatherDataMap.put(region, weeklyWeatherDtos);
            }

            // 리스트에 WeatherDto 객체 추가
            weeklyWeatherDtos.add(weeklyWeatherDto);
        }

        return weatherDataMap;
    }


}
