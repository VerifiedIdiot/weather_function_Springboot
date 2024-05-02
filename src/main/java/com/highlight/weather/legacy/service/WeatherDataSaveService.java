package com.highlight.weather.legacy.service;

import com.highlight.weather.legacy.dto.WeatherDto;
import com.highlight.weather.legacy.entity.Weather;
import com.highlight.weather.legacy.repository.WeatherRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;



import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Log4j2
@Service

public class WeatherDataSaveService {

    @Autowired
    private WeatherRepository weatherRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public WeatherDataSaveService() {
    }


    public void deleteAllWeatherData() {
        weatherRepository.deleteAll();
    }

    @Transactional
    public void saveWeatherData(Map<String, List<List<String>>> weatherData) {
        try {
            System.out.println("단기 + 중기 예보 insert 시작");
            int count = 0;
            for (Map.Entry<String, List<List<String>>> entry : weatherData.entrySet()) {
                for (List<String> data : entry.getValue()) {
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

                    weatherRepository.save(weather);

                    // 세션 flush 및 clear (JPA/Hibernate 사용 시)
                    if (++count % 50 == 0) { // 예를 들어, 50개의 레코드마다 flush 및 clear 수행
                        entityManager.flush();
                        entityManager.clear();
                    }
                }

            }
            System.out.println("단기 + 중기 예보 insert 성공");
        } catch (Exception e) {
            // 예외 처리 및 로그 출력
            System.err.println("Error saving weather data: " + e.getMessage());
            // 실제 사용 시에는 로그 프레임워크 (예: Logback, SLF4J) 사용 권장
        }
    }
}
