package ar.com.challenge.heroes;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
   @Override
   public void addViewControllers(ViewControllerRegistry registry) {
      registry.addViewController("/signin").setViewName("signin");
      registry.addViewController("/").setViewName("home");
      registry.addViewController("/general_error").setViewName("general_error");

   }
}