package javabot.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import javabot.client.DataProvider.RowDataAcceptor;

public class DynamicTableWidget extends Composite {
    private DataProvider dataProvider;
    private Grid grid = new Grid();
    private final DockPanel outer = new DockPanel();
    private int startRow;
    private final RowDataAcceptor acceptor = new RowDataAcceptor() {
        public void accept(final int startRow, final String[][] rows) {
            grid.resizeRows(rows[0].length);
            int destColCount = grid.getCellCount(0);
            int destRowIndex = 1; // skip navbar row
            for (String[] row : rows) {
                for (int srcColIndex = 0; srcColIndex < destColCount; ++srcColIndex) {
                    String cellHTML = row[srcColIndex];
                    grid.setText(++destRowIndex, srcColIndex, cellHTML);
                }
            }
            // Synchronize the nav buttons.
//            navbar.gotoNext.setEnabled(!isLastPage);
//            navbar.gotoFirst.setEnabled(startRow > 0);
//            navbar.gotoPrev.setEnabled(startRow > 0);
            // Update the status message.
            //
//            setStatusText((startRow + 1) + " - " + (startRow + srcRowCount));
        }

        public void failed(final Throwable caught) {
        }
    };

    public DynamicTableWidget(final DataProvider provider, final String[] columns, final String[] styles) {
        this.dataProvider = provider;
        initWidget(outer);
        grid.setStyleName("table");
//        outer.add(navbar, DockPanel.NORTH);
        outer.add(grid, DockPanel.CENTER);
        initTable(columns, styles, 1);
        setStyleName("DynaTable-DynaTableWidget");
    }

    private void initTable(String[] columns, String[] columnStyles, int rowCount) {
        // Set up the header row. It's one greater than the number of visible rows.
        //
        grid.resize(rowCount + 1, columns.length);
        for (int i = 0, n = columns.length; i < n; i++) {
            grid.setText(0, i, columns[i]);
            if (columnStyles != null) {
                grid.getCellFormatter().setStyleName(0, i, columnStyles[i] + " header");
            }
        }
    }

    public void refresh() {
        dataProvider.updateRowData(startRow, grid.getRowCount() - 1, acceptor);
    }
}
