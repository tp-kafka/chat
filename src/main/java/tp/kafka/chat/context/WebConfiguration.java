package tp.kafka.chat.context;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
class WebConfiguration implements WebMvcConfigurer {
  
	@Override
    public void addViewControllers(ViewControllerRegistry registry ) {
        registry.addViewController( "" ).setViewName( "redirect:/swagger-ui.html");
        registry.addViewController( "/" ).setViewName( "redirect:/swagger-ui.html");
    }
}

