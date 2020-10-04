package tp.kafka.chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class SwaggerResource {
 
	@GetMapping
    public RedirectView swagger() {
        return new RedirectView("swagger-ui.html");
    }
}