package it.univaq.swa.soccorsoweb.config;

import it.univaq.swa.soccorsoweb.security.interceptor.LoginValidationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginValidationInterceptor loginValidationInterceptor;

    public WebMvcConfig(LoginValidationInterceptor loginValidationInterceptor) {
        this.loginValidationInterceptor = loginValidationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginValidationInterceptor)
                .addPathPatterns("/auth/open/login");  // Solo per login
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
