package javabot.wicket.panels;

import javabot.dao.FactoidDao;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

// User: joed
// Date: May 21, 2007
// Time: 1:14:08 PM

// 
public class FactoidCount extends Label {

    @SpringBean
    static FactoidDao dao;


    public FactoidCount(String id) {
        super(id, new FCounter());
    }


    private static class FCounter extends AbstractReadOnlyModel {

        public FCounter() {

        }

        public Object getObject() {
            return dao.count();
        }
    }
}


