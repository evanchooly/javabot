package javabot.wicket.panels;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import java.text.SimpleDateFormat;

import javabot.dao.model.Logs;

/**
 * ServerMessagePanel
 * <p/>
 * Created by: Andrew Lombardi
 * Copyright 2006 Mystic Coders, LLC
 */
public class ServerMessagePanel extends Panel {

    public ServerMessagePanel(String id, IModel model) {
        super(id, model);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        Logs log = (Logs)model.getObject();

        add(new Label("date", "["+timeFormat.format( log.getUpdated())+"]"));

        String message = "*** " + log.getNick() + " " + log.getMessage();
        add(new Label("message", message));        
    }
}
