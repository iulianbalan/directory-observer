package com.advicer.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main class containing main function
 *
 * @author Iulian Balan
 */
@Slf4j
public class MainExecutable {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/spring-context.xml");
        IntegrationManager integrationManager = (IntegrationManager) applicationContext.getBean("integrationManager");
        integrationManager.operate(args);
    }

}
