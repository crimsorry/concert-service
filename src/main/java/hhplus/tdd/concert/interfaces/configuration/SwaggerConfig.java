package hhplus.tdd.concert.interfaces.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@SecurityScheme(
        type = SecuritySchemeType.APIKEY,
        name = "waitingToken",
        description = "대기열 Token 입력해주세요.",
        in = SecuritySchemeIn.HEADER)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .security(getSecurityRequirement());
    }

    private List<SecurityRequirement> getSecurityRequirement() {
        List<SecurityRequirement> requirements = new ArrayList<>();
        requirements.add(new SecurityRequirement().addList("waitingToken"));
        return requirements;
    }

    private Info apiInfo() {
        return new Info()
                .title("콘서트 예약 서비스")
                .description("항해99 3~5주차 대기열 시스템")
                .version("1.0.0");
    }
}