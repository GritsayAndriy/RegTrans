package com.regtrans.config;

import com.regtrans.dao.DaoInterface;
import com.regtrans.dao.DriverDao;
import com.regtrans.model.*;
import com.regtrans.service.DriverService;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.spring.SpringFxWeaver;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;


@org.springframework.context.annotation.Configuration
public class AppConfig {

    @Bean
    public SessionFactory sessionFactory() {
        SessionFactory sessionFactory;
        try {
            Configuration configuration = new Configuration().configure();
            configuration.addAnnotatedClass(Transport.class);
            configuration.addAnnotatedClass(Driver.class);
            configuration.addAnnotatedClass(TimeSheet.class);
            configuration.addAnnotatedClass(TypeFuel.class);
            configuration.addAnnotatedClass(Trailer.class);
            StandardServiceRegistryBuilder builder =
                    new StandardServiceRegistryBuilder()
                            .applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(builder.build());
        } catch (Exception e) {
            System.out.println("Исключение!" + e);
            return null;
        }
        return sessionFactory;
    }

    @Bean
    public Desktop getBeanDesktop(){
        return Desktop.getDesktop();
    }



}
