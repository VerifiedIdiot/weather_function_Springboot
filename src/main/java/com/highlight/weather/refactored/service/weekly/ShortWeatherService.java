package com.highlight.weather.refactored.service.weekly;

import com.highlight.weather.refactored.dto.weekly.enumClass.CityEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("refactoredShortWeatherService")
public class ShortWeatherService extends WeatherAbstract {
    @Value("${api.weatherLocation.url}")
    private String weatherLocationUrl;

    @Value("${api.weatherApi.key}")
    private String weatherApiKey;

    @Value("${api.weatherShortDays.url}")
    private String weatherShortDays;
    // 추상클래스에서 직접 주입하는것보다는 생성자를 통해 주입하여 의존성 관리를 스프링이 하게하자

    public WebClient webClient;

    public ShortWeatherService(WebClient webClient) {
        super(webClient);
    }

    private final Logger logger = LogManager.getLogger(this.getClass());

    public Map<String, String> getLocationCode() {
        try {

            logger.info("지역코드 가져오기 시작");

            CompletableFuture<String> futureResponse = sendGetRequest(weatherLocationUrl, weatherApiKey, Collections.emptyMap());
            String response = futureResponse.join(); // 또는 futureResponse.get();


            Map<String, String> locationCode = new HashMap<>();
            String[] lines = response.split("\n");
            for (String line : lines) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 5) {
                    String regId = parts[0];
                    String regName = parts[4];
                    // CityEnum에 해당하는 이름만 매핑
                    for (CityEnum city : CityEnum.values()) {
                        if (city.name().equals(regName)) {
                            locationCode.put(regName, regId);
//                            System.out.println(regName + " : " + regId);
                            break;
                        }
                    }
                }
            }
            logger.info("지역코드 가져오기 성공");
            return locationCode;
        } catch (Exception e) {
            logger.warn("지역코드 가져오기 실패: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }


    public Map<String, List<List<String>>> completeShort(Map<String, String> locationCode) {
        try {

            Map<String, Integer> shortDateParams = shortDaysParam();
            Map<String, List<List<String>>> completeShort = new HashMap<>();

            LocalDate today = LocalDate.now();
            LocalDate twoDaysLater = today.plusDays(2);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            logger.info("단기예보 취합 시작");
//            System.out.println(locationCode);
            for (Map.Entry<String, String> entry : locationCode.entrySet()) {
                String cityName = entry.getKey();
                String regCode = entry.getValue();

//                System.out.println(cityName + " : " + regCode);


                Map<String, String> queryParams = shortQueryParams(regCode, shortDateParams);
                CompletableFuture<String> futureResponse = sendGetRequest(weatherShortDays, weatherApiKey, queryParams);
                String response = futureResponse.join(); // 또는 futureResponse.get();
                String[] lines = response.split("\n");
                List<String> filteredLines = new ArrayList<>();

                for (String line : lines) {

                    if (!line.contains("#") && !line.contains("-99")) {

                        filteredLines.add(line);
                    }
                }

                Map<String, List<String>> morningData = new LinkedHashMap<>();
                Map<String, List<String>> afternoonData = new LinkedHashMap<>();

                for (String line : filteredLines) {
//                    System.out.println(line);
                    List<String> fields = parseLine(line);
                    if (!fields.isEmpty()) {
                        String dateStr = fields.get(2).substring(0, 8);
                        LocalDate date = LocalDate.parse(dateStr, formatter);

                        if (!date.isBefore(today) && !date.isAfter(twoDaysLater)) {
                            if (fields.get(2).endsWith("0000")) {
                                morningData.put(dateStr, Arrays.asList(fields.get(12), fields.get(13), fields.get(16)));
                            } else if (fields.get(2).endsWith("1200")) {
                                afternoonData.put(dateStr, Arrays.asList(fields.get(12), fields.get(13), fields.get(16)));
                            }
                        }
                    }
                }

                List<List<String>> cityWeather = new ArrayList<>();
                for (String date : morningData.keySet()) {
                    List<String> combinedWeather = new ArrayList<>();
                    combinedWeather.add(date);
                    combinedWeather.addAll(morningData.get(date));
                    if (afternoonData.containsKey(date)) {
                        combinedWeather.addAll(afternoonData.get(date));
                    } else {
                        combinedWeather.addAll(Arrays.asList("", "", ""));
                    }
                    cityWeather.add(combinedWeather);
                }

                completeShort.put(cityName, cityWeather);
            }

            logger.info("단기예보 취합 성공");
            return completeShort;
        } catch (Exception e) {
            logger.warn("단기예보 취합 실패: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }


    private List<String> parseLine(String line) {
        try {
            List<String> dataFields = new ArrayList<>();
            Matcher matcher = Pattern.compile("[^\\s\"]+|\"([^\"]*)\"").matcher(line);

            while (matcher.find()) {
                if (matcher.group(1) != null) {
                    dataFields.add(matcher.group(1));
                } else {
                    dataFields.add(matcher.group());
                }
            }
            return dataFields;
        } catch (Exception e) {
            logger.warn("라인 파싱 실패: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
