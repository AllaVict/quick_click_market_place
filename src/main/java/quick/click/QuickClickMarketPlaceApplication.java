package quick.click;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.Objects;

@SpringBootApplication
@ConfigurationPropertiesScan("quick.click")
public class QuickClickMarketPlaceApplication{

    public static void main(final String[] args) {

        SpringApplication.run(QuickClickMarketPlaceApplication.class, args);

    }

}
