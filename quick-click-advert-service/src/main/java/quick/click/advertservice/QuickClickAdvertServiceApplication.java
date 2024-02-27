package quick.click.advertservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("quick.click")
public class QuickClickAdvertServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickClickAdvertServiceApplication.class, args);
    }

}
