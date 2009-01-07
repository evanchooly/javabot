package javabot.dao;

import javabot.Javabot;
import org.springframework.context.ApplicationContext;
import org.unitils.UnitilsTestNG;
import org.unitils.spring.annotation.SpringApplicationContext;

public class BaseServiceTest extends UnitilsTestNG {
    @SpringApplicationContext("classpath:test-application-config.xml")
    private ApplicationContext springApplicationContext;

    private ApplicationContext getSpringApplicationContext() {
        return springApplicationContext;
    }

    protected Javabot getJavabot() {
        return new Javabot(springApplicationContext);
    }
} 