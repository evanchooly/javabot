package javabot.client;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.event.dom.client.ClickEvent;

public class ChannelBox extends Composite {
    public ChannelBox() {
        FlexTable table = new FlexTable();
        
    }
/*
    public void onClick(ClickEvent event) {
        Object source = event.getSource();
        if (source == gotoNext) {
            startRow += getDataRowCount();
            refresh();
        } else if (source == gotoPrev) {
            startRow -= getDataRowCount();
            if (startRow < 0) {
                startRow = 0;
            }
            refresh();
        } else if (source == gotoFirst) {
            startRow = 0;
            refresh();
        }
    }
*/
}
