package com.softserveinc.ita.kaiji;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

import java.util.Locale;
import java.util.Properties;

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
        "com.softserveinc.ita.kaiji.ajax", "com.softserveinc.ita.kaiji.sse"})
public class ContextConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    Environment env;

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        resolver.setOrder(1);
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

    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions("/WEB-INF/defs/definitions.xml");
        return tilesConfigurer;
    }

    @Bean
    public UrlBasedViewResolver tilesViewResolver() {
        UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
        viewResolver.setViewClass(TilesView.class);
        viewResolver.setOrder(0);
        return viewResolver;
    }

    @Bean
    public JavaMailSenderImpl javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("mail.smtp.host"));
        mailSender.setPort(Integer.valueOf(env.getProperty("mail.smtp.port")));
        mailSender.setUsername(env.getProperty("email.address"));
        mailSender.setPassword(env.getProperty("email.password"));

        mailSender.setJavaMailProperties(mailProperties());
        return  mailSender;
    }

    private Properties mailProperties() {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol","smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        return properties;
    }
}
