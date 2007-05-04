package javabot.dao;

import org.springframework.context.ApplicationContext;
import org.unitils.UnitilsTestNG;
import org.unitils.spring.annotation.SpringApplicationContext;

//

// Author: joed

// Date  : Apr 15, 2007

//@SpringApplicationContext({"test-application-config.xml", "test-ds-config.xml"})
@SpringApplicationContext("test-application-config.xml")
public class BaseServiceTest extends UnitilsTestNG {

    @SpringApplicationContext
    ApplicationContext springApplicationContext;

}


 