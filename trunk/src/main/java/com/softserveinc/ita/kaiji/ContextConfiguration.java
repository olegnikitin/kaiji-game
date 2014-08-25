package com.softserveinc.ita.kaiji;

import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Locale;

/**
 * Root configuration
 *
 * @author Sydorenko Oleksandra
 * @version 1.0
 * @since 20.08.14
 */

@Configuration
@Import({DBConfiguration.class, SecurityConfiguration.class})
@EnableWebMvc
@PropertySource("classpath:email.properties")
@ComponentScan({"com.softserveinc.ita.kaiji.web", "com.softserveinc.ita.kaiji.rest",
        "com.softserveinc.ita.kaiji.service", "com.softserveinc.ita.kaiji.model", "com.softserveinc.ita.kaiji.dto",
        "com.softserveinc.ita.kaiji.ajax"})
public class ContextConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public LocalValidatorFactoryBean validatorFactoryBean() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSourceBean());
        return validator;
    }

    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource messageSourceBean() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:i18n/common", "classpath:i18n/errors", "classpath:i18n/pages/registration-form",
                "classpath:i18n/pages/start-page", "classpath:i18n/pages/header", "classpath:i18n/pages/create-game",
                "classpath:i18n/pages/system-configuration", "classpath:i18n/pages/play-game", "classpath:i18n/pages/join-game",
                "classpath:i18n/pages/statistics", "classpath:i18n/pages/login");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean(name = "localeResolver")
    public CookieLocaleResolver localeResolverBean() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(new Locale("en"));
        return resolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setParamName("lang");
        registry.addInterceptor(localeInterceptor);
    }

    @Bean
    public Validator mvcValidator() {
        return validatorFactoryBean();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
