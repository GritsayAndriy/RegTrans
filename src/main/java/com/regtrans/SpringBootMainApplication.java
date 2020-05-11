package com.regtrans;

import com.regtrans.service.DriverService;
import javafx.application.Application;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.spring.SpringFxWeaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication()
public class SpringBootMainApplication {

    private static Logger logger = LoggerFactory.getLogger(SpringBootMainApplication.class);

    @Autowired
    DriverService driverService;

    public static void main(String[] args) {
        Application.launch(JavaFxApplication.class, args);
    }

}
