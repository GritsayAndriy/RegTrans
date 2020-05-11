package com.regtrans.config;

import com.regtrans.dao.DaoInterface;
import com.regtrans.dao.DriverDao;
import com.regtrans.model.Driver;
import com.regtrans.model.TimeSheet;
import com.regtrans.model.Transport;
import com.regtrans.service.DriverService;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;


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

//    @Bean
//    public DriverService driverService(){
//        DriverService driverService = new DriverService();
//        DaoInterface<Driver> driverDao = new DriverDao();
//        driverDao.setSessionFactory(sessionFactory());
//        driverService.setDriverDao(driverDao);
//        return driverService;
//    }
}
