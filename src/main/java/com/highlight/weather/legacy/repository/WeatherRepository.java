package com.highlight.weather.legacy.repository;

import com.highlight.weather.legacy.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("legacyWeatherRepository")
public interface WeatherRepository extends JpaRepository <Weather, Long> {
}
