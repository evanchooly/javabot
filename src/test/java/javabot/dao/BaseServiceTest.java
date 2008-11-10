package javabot.dao;

import org.springframework.context.ApplicationContext;
import org.unitils.UnitilsTestNG;
import org.unitils.spring.annotation.SpringApplicationContext;

public class BaseServiceTest extends UnitilsTestNG {
    @SpringApplicationContext("classpath:test-application-config.xml")
    ApplicationContext springApplicationContext;
}


 