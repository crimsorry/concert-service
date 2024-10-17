package hhplus.tdd.concert.interfaces.configuration;

import hhplus.tdd.concert.interfaces.interceptor.WaitingCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private WaitingCheckInterceptor waitingCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(waitingCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/v1/user/*/queue/token");
    }
}