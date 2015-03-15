package com.lanmingle.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequestMapping
public class HomeController {

    //----------------------------------------------------------------------------------------------------------------------------------

    public static final String HOME_VIEW = "home";

    //----------------------------------------------------------------------------------------------------------------------------------

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //----------------------------------------------------------------------------------------------------------------------------------


    public HomeController() {
        logger.debug("HomeController instantiation.");
    }

    @RequestMapping("/")
    public String gotoHome() {
        logger.debug("gotoHome view is :{}.", HOME_VIEW);
        return HOME_VIEW;
    }

}
