package javabot.ws;

import java.util.List;

import javabot.dao.ClazzDao;
import javabot.operations.JavadocOperation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class JavadocServiceImpl implements ApplicationContextAware, JavadocService {
    @Autowired
    private ClazzDao dao;
    private JavadocOperation operation = new JavadocOperation();

    @Override
    public List<String> lookup(String request) {
        return operation.handle(request);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(operation);
    }
}
