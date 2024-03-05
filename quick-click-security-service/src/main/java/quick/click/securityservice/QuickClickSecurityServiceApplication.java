package quick.click.securityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import quick.click.securityservice.commons.config.AppProperties;

@SpringBootApplication
@ComponentScan({"quick.click.securityservice", "quick.click.advertservice" })
@EnableConfigurationProperties(AppProperties.class)
public class QuickClickSecurityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickClickSecurityServiceApplication.class, args);
    }

}
