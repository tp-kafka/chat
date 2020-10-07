package tp.kafka.chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
public class SwaggerResource {
 
    @GetMapping
    @Hidden
    public RedirectView swagger() {
        return new RedirectView("swagger-ui.html");
    }
}