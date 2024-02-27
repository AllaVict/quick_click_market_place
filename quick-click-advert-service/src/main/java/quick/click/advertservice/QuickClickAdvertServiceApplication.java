package quick.click.advertservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan("quick.click.advertservice")
public class QuickClickAdvertServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickClickAdvertServiceApplication.class, args);
    }

}
