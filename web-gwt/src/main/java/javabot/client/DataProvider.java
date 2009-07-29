package javabot.client;

public interface DataProvider {
    /**
     * An interface allow a widget to accept or report failure when a row data is issued for update.
     */
    interface RowDataAcceptor {
        void accept(int startRow, String[][] rows);

        void failed(Throwable caught);
    }

    void updateRowData(final int startRow, final int i, final RowDataAcceptor acceptor);
}
