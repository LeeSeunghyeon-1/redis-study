package redisstudy.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EnableCaching //@Cacheable과 같은 캐싱 어노테이션의 사용을 인식하기 위함
@SpringBootApplication
//@EntityScan(basePackages = "domain.coupon.entity")
@EntityScan(basePackages = "domain")
//@EnableJpaRepositories(basePackages = {"com.example.study.domain.coupon.repository"})
public class StudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyApplication.class, args);
	}

}
