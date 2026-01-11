package com.rest_api.apigateway.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Controller {
    private static final Logger logger= LoggerFactory.getLogger(Controller.class);

    @GetMapping("/test")
    public String test() {

        logger.info("Test running");
        return "OK";
    }
}
