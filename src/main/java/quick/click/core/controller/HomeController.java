package quick.click.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static quick.click.commons.constants.Constants.Endpoints.HOME_URL;

@CrossOrigin
@RestController
@RequestMapping()
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    public static final String BASE_URL = HOME_URL;

    @GetMapping(BASE_URL)
    public ResponseEntity<?> showHomePage() {

        LOGGER.debug("In showHomePage - Received GET request to show home page");

        return new ResponseEntity<>("This is the home page", HttpStatus.OK);

    }

}