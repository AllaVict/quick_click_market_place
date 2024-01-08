package quick.click.http.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class HomeController {

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    public static final String INDEX = "index";

    public static final String BASE_URL = "/home";

    @GetMapping(BASE_URL)
    public String showHomePage() {

        LOG.debug("In showHomePage - Received GET request to show home page");

        return INDEX;
    }

    @GetMapping("/for-signed-users")
    public String forSignedUsers() {

        LOG.debug("In forSignedUsers - Received GET request to show for-signed-users page");

        return "for-signed-users";
    }


}