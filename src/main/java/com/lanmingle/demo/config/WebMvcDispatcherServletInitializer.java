package com.lanmingle.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.MultipartConfig;

@MultipartConfig
public class WebMvcDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    //----------------------------------------------------------------------------------------------------------------------------------

    public static final String CHARACTER_ENCODING_UTF_8 = "UTF-8";

    //----------------------------------------------------------------------------------------------------------------------------------

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //----------------------------------------------------------------------------------------------------------------------------------

    public WebMvcDispatcherServletInitializer() {
        logger.debug("WebMvcDispatcherServletInitializer instantiation.");
    }

    //----------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected Class<?>[] getRootConfigClasses() {
        logger.debug("getRootConfigClasses.");

        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        logger.debug("getServletConfigClasses.");

        return new Class<?>[]{WebMvcApplicationConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        logger.debug("getServletMappings.");

        return new String[]{"/"};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        logger.debug("customizeRegistration.");

        registerMultipartConfig(registration);
    }

    @Override
    protected Filter[] getServletFilters() {
        logger.debug("getServletFilters.");

        return new Filter[]{characterEncodingFilter(), multipartFilter()};
    }

    //----------------------------------------------------------------------------------------------------------------------------------

    private void registerMultipartConfig(ServletRegistration.Dynamic registration) {
        MultipartConfig multipartConfig = getClass().getAnnotation(MultipartConfig.class);
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(multipartConfig);
        registration.setMultipartConfig(multipartConfigElement);

        logger.debug("registerMultipartConfig multipartConfigElement is :{}",multipartConfigElement);
        logger.debug("registerMultipartConfig registration is :{}",registration);
    }

    private CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setForceEncoding(true);
        filter.setEncoding(CHARACTER_ENCODING_UTF_8);

        logger.debug("characterEncodingFilter filter is :{}",filter);
        return filter;
    }

    private MultipartFilter multipartFilter() {
        MultipartFilter filter = new MultipartFilter();

        logger.debug("multipartFilter filter is :{}",filter);
        return filter;
    }

}
