package me.ggulmool.todo.config;

import me.ggulmool.todo.security.BasicAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/webjars/**",
                "/js/**",
                "/css/**")
                .addResourceLocations(
                        "classpath:/META-INF/resources/webjars/",
                        "classpath:/static/js/",
                        "classpath:/static/css/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Bean
    public BasicAuthInterceptor basicAuthInterceptor() {
        return new BasicAuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(basicAuthInterceptor());
    }
}
