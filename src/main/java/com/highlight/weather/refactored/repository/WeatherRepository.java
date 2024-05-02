package com.highlight.weather.refactored.repository;

import com.highlight.weather.refactored.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("refactoredWeatherRepository")
public interface WeatherRepository extends JpaRepository<Weather, Long> {
}
