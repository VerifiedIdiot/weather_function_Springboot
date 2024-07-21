package com.highlight.weather.refactored.service.weekly;

import com.highlight.weather.refactored.dto.weekly.WeeklyWeatherDto;
import com.highlight.weather.refactored.entity.WeeklyWeather;
import com.highlight.weather.refactored.repository.WeatherRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
@Async
@Service("refactoredWeatherDataSaveService")
public class WeatherDataSaveService {
    private static final Logger logger = LogManager.getLogger(WeatherDataSaveService.class);
    @Autowired
    private WeatherRepository weatherRepository;

    public WeatherDataSaveService() {
    }

    private void deleteAllWeatherData() {
        long count = weatherRepository.count();
        if (count > 0) {
            weatherRepository.deleteAll();
            logger.info("모든 날씨 데이터가 성공적으로 삭제되었습니다.");
        } else {
            logger.info("삭제할 날씨 데이터가 없습니다.");
        }
    }

    @Transactional
    public void saveWeatherData(Map<String, List<List<String>>> weatherData) {
        try {
            logger.debug("Starting to save weather data");
            List<WeeklyWeather> weeklyWeathers = new ArrayList<>();
            for (Map.Entry<String, List<List<String>>> entry : weatherData.entrySet()) {
                for (List<String> data : entry.getValue()) {
                    try {
                        WeeklyWeatherDto weeklyWeatherDto = new WeeklyWeatherDto();
                        // 데이터 추출 및 DTO 설정
                        weeklyWeatherDto.setRegion(entry.getKey());
                        weeklyWeatherDto.setWeatherDate(Integer.parseInt(data.get(0)));
                        weeklyWeatherDto.setMorningTemperature(Integer.parseInt(data.get(1)));
                        weeklyWeatherDto.setMorningRainPercent(Integer.parseInt(data.get(2)));
                        weeklyWeatherDto.setMorningWeatherCondition(data.get(3));
                        weeklyWeatherDto.setAfternoonTemperature(Integer.parseInt(data.get(4)));
                        weeklyWeatherDto.setAfternoonRainPercent(Integer.parseInt(data.get(5)));
                        weeklyWeatherDto.setAfternoonWeatherCondition(data.get(6));

                        WeeklyWeather weeklyWeather = weeklyWeatherDto.toEntity();
                        weeklyWeathers.add(weeklyWeather);
                    } catch (Exception e) {
                        logger.error("Error parsing weather data: " + e.getMessage(), e);
                        // 로그를 남기고, 오류 데이터는 스킵하거나 추가 조치를 취할 수 있음
                    }
                }
            }
            deleteAllWeatherData();
            weatherRepository.saveAll(weeklyWeathers);

            logger.info("Weather data saved successfully");
        } catch (Exception e) {
            logger.error("Error saving weather data: " + e.getMessage(), e);
        }
    }
}
