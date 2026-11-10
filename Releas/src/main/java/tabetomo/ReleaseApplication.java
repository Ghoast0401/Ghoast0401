package tabetomo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(
    scanBasePackages = {
        "tabetomo",
        "com.Tabetomo"
    }
)
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.Tabetomo")
@EntityScan(basePackages = "com.Tabetomo")
public class ReleaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReleaseApplication.class, args);
    }
}