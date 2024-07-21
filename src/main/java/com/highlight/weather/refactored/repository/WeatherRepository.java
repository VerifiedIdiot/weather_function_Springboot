package com.highlight.weather.refactored.repository;

import com.highlight.weather.refactored.entity.WeeklyWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("refactoredWeatherRepository")
public interface WeatherRepository extends JpaRepository<WeeklyWeather, Long> {
}
