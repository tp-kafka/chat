package tp.kafka.chat.context;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for configuring web-related settings in the Spring application.
 * This class implements the {@code WebMvcConfigurer} interface to provide custom
 * configuration for adding view controllers that redirect to the Swagger UI.
 */
@Configuration
class WebConfiguration implements WebMvcConfigurer {
  
	@Override
    public void addViewControllers(ViewControllerRegistry registry ) {
        registry.addViewController( "" ).setViewName( "redirect:/swagger-ui.html");
        registry.addViewController( "/" ).setViewName( "redirect:/swagger-ui.html");
    }
}

