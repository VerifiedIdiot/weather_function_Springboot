package com.highlight.weather.legacy.service;

import com.highlight.weather.legacy.dto.WeatherDto;
import com.highlight.weather.legacy.entity.Weather;
import com.highlight.weather.legacy.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherToFrontService {
    @Autowired
    private WeatherRepository weatherRepository;

    public Map<String, List<WeatherDto>> getWeatherData() {
        List<Weather> weatherList = weatherRepository.findAll();
        Map<String, List<WeatherDto>> weatherDataMap = new HashMap<>();

        for (Weather weather : weatherList) {
            WeatherDto weatherDto = weather.toDto();
            String region = weatherDto.getRegion();

            // 해당 지역에 대한 리스트가 맵에 존재하는지 확인
            List<WeatherDto> weatherDtos = weatherDataMap.get(region);
            if (weatherDtos == null) {
                // 리스트가 존재하지 않으면 새로운 리스트를 생성하고 맵에 추가
                weatherDtos = new ArrayList<>();
                weatherDataMap.put(region, weatherDtos);
            }

            // 리스트에 WeatherDto 객체 추가
            weatherDtos.add(weatherDto);
        }

        return weatherDataMap;
    }


}
