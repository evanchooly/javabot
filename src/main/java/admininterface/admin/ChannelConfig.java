package admininterface.admin;

import javabot.dao.ChannelConfigDao;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

// Author: joed
// Date  : Jun 12, 2007

public class ChannelConfig extends Panel {

    @SpringBean
    ChannelConfigDao dao;


    public ChannelConfig(String id) {
        super(id);

        //Insert a form and an editable table with
        //Some columns and checkboxes.


    }

}

