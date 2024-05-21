package quick.click.core.utils;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.AUTH_URL;
import static quick.click.security.core.controller.UserAuthorizedController.BASE_URL;

@CrossOrigin
@RestController
@Tag(name = "UtilsController Controller", description = "UserAuthorized API")
@RequestMapping(BASE_URL)
public class UtilsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UtilsController.class);

    public static final String BASE_URL = VERSION_1_0 + AUTH_URL;



}