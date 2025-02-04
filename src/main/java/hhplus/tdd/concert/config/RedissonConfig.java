package hhplus.tdd.concert.config;

import hhplus.tdd.concert.config.property.RedisProperty;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RedissonConfig {

    private final RedisProperty redisProperty;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://"+ this.redisProperty.getHost()+":"+ this.redisProperty.getPort());

        return Redisson.create(config);
    }
}