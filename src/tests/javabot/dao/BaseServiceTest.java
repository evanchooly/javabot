package javabot.dao;

import org.springframework.context.ApplicationContext;
import org.unitils.UnitilsTestNG;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.testng.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javabot.operations.BotOperation;
import javabot.BotEvent;
import javabot.Message;
import javabot.ApplicationException;

import java.util.List;
//

// Author: joed

// Date  : Apr 15, 2007

//@SpringApplicationContext({"test-application-config.xml", "test-ds-config.xml"})
@SpringApplicationContext("test-application-config.xml")
public class BaseServiceTest extends UnitilsTestNG {

    @SpringApplicationContext
    ApplicationContext springApplicationContext;
    
}


 