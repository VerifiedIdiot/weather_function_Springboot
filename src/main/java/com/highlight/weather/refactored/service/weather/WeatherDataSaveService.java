package com.highlight.weather.refactored.service.weather;

import com.highlight.weather.refactored.dto.WeatherDto;
import com.highlight.weather.refactored.entity.Weather;
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
            List<Weather> weathers = new ArrayList<>();
            for (Map.Entry<String, List<List<String>>> entry : weatherData.entrySet()) {
                for (List<String> data : entry.getValue()) {
                    try {
                        WeatherDto weatherDto = new WeatherDto();
                        // 데이터 추출 및 DTO 설정
                        weatherDto.setRegion(entry.getKey());
                        weatherDto.setWeatherDate(Integer.parseInt(data.get(0)));
                        weatherDto.setMorningTemperature(Integer.parseInt(data.get(1)));
                        weatherDto.setMorningRainPercent(Integer.parseInt(data.get(2)));
                        weatherDto.setMorningWeatherCondition(data.get(3));
                        weatherDto.setAfternoonTemperature(Integer.parseInt(data.get(4)));
                        weatherDto.setAfternoonRainPercent(Integer.parseInt(data.get(5)));
                        weatherDto.setAfternoonWeatherCondition(data.get(6));

                    Weather weather = weatherDto.toEntity();
                    weathers.add(weather);
                } catch (Exception e) {
                        logger.error("Error parsing weather data: " + e.getMessage(), e);
                    // 로그를 남기고, 오류 데이터는 스킵하거나 추가 조치를 취할 수 있음
                    }
                }
            }
            deleteAllWeatherData();
            weatherRepository.saveAll(weathers);

            logger.info("Weather data saved successfully");
        } catch (Exception e) {
            logger.error("Error saving weather data: " + e.getMessage(), e);
        }
    }
}
