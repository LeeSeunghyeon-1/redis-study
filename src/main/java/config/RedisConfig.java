package config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableRedisRepositories
@Configuration
public class RedisConfig {
    //host 와 port 지정
    @Value("${spring.data.redis.host}")
    public String host;

    @Value("${spring.data.redis.port}")
    public int port;

    private static final String REDISSON_HOST_PREFIX = "redis://";
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + host + ":" + port);

        return Redisson.create(config);
    }
}
