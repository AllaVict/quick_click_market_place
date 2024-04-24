package quick.click.core.utils;

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

import jakarta.annotation.PostConstruct;
import quick.click.commons.exeptions.ResourceNotFoundException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@Order(1)
public class DatabaseInitService implements CommandLineRunner {

    public static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ResourceLoader resourceLoader;

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

    @Override
    public void run(String... args) throws Exception {
        try {
            initDatabase();
        } catch (Exception exception) {

        }
    }
}