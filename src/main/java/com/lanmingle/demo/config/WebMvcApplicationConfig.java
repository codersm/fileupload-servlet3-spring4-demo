package com.lanmingle.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.lanmingle.demo.server", "com.lanmingle.demo.controller"})
@PropertySource("classpath:/config/web-mvc-application-config.properties")
public class WebMvcApplicationConfig extends WebMvcConfigurerAdapter {

    //----------------------------------------------------------------------------------------------------------------------------------

    private static final int CACHE_PERIOD = 31556926;

    //----------------------------------------------------------------------------------------------------------------------------------

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //----------------------------------------------------------------------------------------------------------------------------------

    @Value("${thymeleaf.template.resolver.prefix}")
    private String thymeleafTemplateResolverPrefix; // -> /WEB-INF/views/
    @Value("${thymeleaf.template.resolver.suffix}")
    private String thymeleafTemplateResolverSuffix;// -> .html
    @Value("${thymeleaf.template.resolver.character.encoding}")
    private String thymeleafTemplateResolverCharacterEncoding; // -> UTF-8
    @Value("${thymeleaf.template.resolver.template.mode}")
    private String thymeleafTemplateResolverTemplateMode; // -> HTML5
    @Value("${thymeleaf.template.resolver.cache}")
    private Boolean thymeleafTemplateResolverCache; // -> true|false default true

    @Value("${thymeleaf.view.resolver.character.encoding}")
    private String thymeleafViewResolverCharacterEncoding; // -> UTF-8
    @Value("${thymeleaf.view.resolver.content.type}")
    private String thymeleafViewResolverContentType; // -> text/html;charset=UTF-8

    //----------------------------------------------------------------------------------------------------------------------------------

    public WebMvcApplicationConfig() {
        logger.debug("WebMvcApplicationConfig instantiation.");
    }

    //----------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();

        logger.debug("configureDefaultServletHandling configurer is :{}", configurer);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);

        logger.debug("configureContentNegotiation configurer is :{}", configurer);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/")
                .setCachePeriod(CACHE_PERIOD);

        logger.debug("addResourceHandlers registry is :{}", registry);
    }

    //----------------------------------------------------------------------------------------------------------------------------------

    @Bean(name = "webMvcApplicationPropertySourcesPlaceholderConfigurer")
    public static PropertySourcesPlaceholderConfigurer webMvcApplicationPropertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(false);

        return propertySourcesPlaceholderConfigurer;
    }

    //----------------------------------------------------------------------------------------------------------------------------------

    @Bean(name = "thymeleafTemplateResolver")
    public ServletContextTemplateResolver thymeleafTemplateResolver() {

        logger.debug("thymeleafTemplateResolver thymeleafTemplateResolverPrefix is :{}", thymeleafTemplateResolverPrefix);
        logger.debug("thymeleafTemplateResolver thymeleafTemplateResolverSuffix is :{}", thymeleafTemplateResolverSuffix);
        logger.debug("thymeleafTemplateResolver thymeleafTemplateResolverCharacterEncoding is :{}", thymeleafTemplateResolverCharacterEncoding);
        logger.debug("thymeleafTemplateResolver thymeleafTemplateResolverTemplateMode is :{}", thymeleafTemplateResolverTemplateMode);
        logger.debug("thymeleafTemplateResolver thymeleafTemplateResolverCache is :{}", thymeleafTemplateResolverCache);

        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix(thymeleafTemplateResolverPrefix);
        templateResolver.setSuffix(thymeleafTemplateResolverSuffix);
        templateResolver.setCharacterEncoding(thymeleafTemplateResolverCharacterEncoding);
        templateResolver.setTemplateMode(thymeleafTemplateResolverTemplateMode);
        templateResolver.setCacheable(thymeleafTemplateResolverCache);

        logger.debug("thymeleafTemplateResolver templateResolver is :{}", templateResolver);

        return templateResolver;
    }

    @Bean(name = "thymeleafTemplateEngine")
    public SpringTemplateEngine thymeleafTemplateEngine(@Qualifier("thymeleafTemplateResolver") ITemplateResolver templateResolver) {

        logger.debug("thymeleafTemplateEngine templateResolver is :{}", templateResolver);

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        logger.debug("thymeleafTemplateEngine templateEngine is :{}", templateEngine);

        return templateEngine;
    }

    @Bean(name = "thymeleafViewResolver")
    public ThymeleafViewResolver thymeleafViewResolver(@Qualifier("thymeleafTemplateEngine") SpringTemplateEngine springTemplateEngine) {

        logger.debug("thymeleafViewResolver thymeleafViewResolverCharacterEncoding is :{}", thymeleafViewResolverCharacterEncoding);
        logger.debug("thymeleafViewResolver thymeleafViewResolverContentType is :{}", thymeleafViewResolverContentType);

        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(springTemplateEngine);
        viewResolver.setCharacterEncoding(thymeleafViewResolverCharacterEncoding);
        viewResolver.setContentType(thymeleafViewResolverContentType);
        viewResolver.setOrder(Ordered.LOWEST_PRECEDENCE);

        logger.debug("thymeleafViewResolver viewResolver is :{}", viewResolver);

        return viewResolver;
    }

    //----------------------------------------------------------------------------------------------------------------------------------

}
