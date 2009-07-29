package javabot.client;

import com.google.gwt.user.client.ui.Composite;

public class LogWidget extends Composite {
    private DynamicTableWidget dynaTable;

    public LogWidget() {
        String[] columns = new String[]{"Name", "Description", "Schedule"};
        String[] styles = new String[]{"name", "desc", "sched"};
        final DataProvider dataProvider = new LogProvider();
        dynaTable = new DynamicTableWidget(dataProvider, columns, styles);
        initWidget(dynaTable);
    }

    @Override
    protected void onLoad() {
        dynaTable.refresh();
    }

}
