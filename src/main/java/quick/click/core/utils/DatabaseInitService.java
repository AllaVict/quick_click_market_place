package quick.click.core.utils;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Service for initializing the database with predefined SQL statements for Postgres.
 * This service loads SQL from a file and executes it upon application startup.
 * It implements CommandLineRunner to ensure that it runs after the application context is fully loaded.
 */
//@Service
//@Order(1)
public class DatabaseInitService implements CommandLineRunner {

    public static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitService.class);

   // @Autowired
    private JdbcTemplate jdbcTemplate;

  // @Autowired
    private ResourceLoader resourceLoader;

    /**
     * Loads SQL statements from a resource file and executes them to initialize the database.
     * This method is automatically called after dependency injection is done to perform any initialization.
     */
    @PostConstruct
    public void initDatabase() {
        try {
            final Resource resource = resourceLoader.getResource("classpath:init_postgres.sql");
            final String sql = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);
            jdbcTemplate.execute(sql);
            LOGGER.debug("Database initialized successfully.");
        } catch (IOException e) {
            LOGGER.debug("Error initializing database: " + e.getMessage());
        }
    }

    /**
     * Runs the initDatabase method to execute the SQL script at application startup.
     * It is part of the CommandLineRunner interface which is invoked with command line arguments, if any.
     *
     * @param args command line arguments passed to the application.
     * @throws Exception if there is any issue executing the database init script.
     */
    @Override
    public void run(String... args) throws Exception {
        try {
            initDatabase();
        } catch (Exception exception) {

        }
    }
}