package com.highlight.weather.legacy.service;

import com.highlight.weather.legacy.enumClass.CityEnum;
import com.highlight.weather.legacy.enumClass.RegionEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnumMapperService {
    public static Map<String, List<String>> getRegionToCitiesMap() {
        Map<String, List<String>> regionToCities = new HashMap<>();

        // SEOUL_GYEONGGI 지역에 서울, 수원, 인천 추가
        regionToCities.put(RegionEnum.SEOUL_GYEONGGI.name(), Arrays.asList(CityEnum.서울.name(), CityEnum.수원.name(), CityEnum.인천.name()));

        // GANGWON_YOUNGSEO 지역에 춘천 추가
        regionToCities.put(RegionEnum.GANGWON_YOUNGSEO.name(), List.of(CityEnum.춘천.name()));

        // GANGWON_YOUNGDONG 지역에 강릉 추가
        regionToCities.put(RegionEnum.GANGWON_YOUNGDONG.name(), List.of(CityEnum.강릉.name()));

        // CHUNGCHEONG_BUKDO 지역에 청주 추가
        regionToCities.put(RegionEnum.CHUNGCHEONG_BUKDO.name(), List.of(CityEnum.청주.name()));

        // CHUNGCHEONG_NAMDO 지역에 대전 추가
        regionToCities.put(RegionEnum.CHUNGCHEONG_NAMDO.name(), List.of(CityEnum.대전.name()));

        // GYEONGSANG_BUKDO 지역에 안동, 대구 추가
        regionToCities.put(RegionEnum.GYEONGSANG_BUKDO.name(), Arrays.asList(CityEnum.안동.name(), CityEnum.대구.name()));

        // GYEONGSANG_NAMDO 지역에 부산, 울산 추가
        regionToCities.put(RegionEnum.GYEONGSANG_NAMDO.name(), Arrays.asList(CityEnum.부산.name(), CityEnum.울산.name()));

        // JEOLLA_BUKDO 지역에 전주 추가
        regionToCities.put(RegionEnum.JEOLLA_BUKDO.name(), List.of(CityEnum.전주.name()));

        // JEOLLA_NAMDO 지역에 광주, 여수, 목포 추가
        regionToCities.put(RegionEnum.JEOLLA_NAMDO.name(), Arrays.asList(CityEnum.광주.name(), CityEnum.여수.name(), CityEnum.목포.name()));

        // JEJU 지역에 제주 추가
        regionToCities.put(RegionEnum.JEJU.name(), List.of(CityEnum.제주.name()));

        return regionToCities;
    }
}
