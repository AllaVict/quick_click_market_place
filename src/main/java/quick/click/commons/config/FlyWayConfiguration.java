package quick.click.commons.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlyWayConfiguration {
    @Bean
    public FlywayMigrationStrategy repairFlyway() {
        return flyway -> {
            // repair each script's checksum
            flyway.repair();
            // before new migrations are executed
            flyway.migrate();
        };
    }
}
