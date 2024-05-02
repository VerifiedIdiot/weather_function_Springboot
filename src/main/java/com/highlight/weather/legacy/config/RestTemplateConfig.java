package com.highlight.weather.legacy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


// 스프링 5 이후로는 RestTemplate는 deprecated 되고 유지만 된다고한다.
// 해당 방식을 대체하는것으로 RestClient가 있는데 WebClient를 동기적으로 쓰는 방식이다.
@Configuration
public class RestTemplateConfig {



    // 스프링 2.X.X 에서 사용하던 RestTemplate()
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}