package javabot.client;

import java.util.List;
import java.text.SimpleDateFormat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import javabot.model.Logs;

public class LogProvider implements DataProvider {
    private LogServiceAsync logService;
    private String channel;

    public LogProvider() {
        logService = (LogServiceAsync) GWT.create(LogService.class);
        String moduleRelativeURL = GWT.getModuleBaseURL() + "javabot";
        ((ServiceDefTarget) logService).setServiceEntryPoint(moduleRelativeURL);
    }

    public void updateRowData(final int startRow, final int i, final RowDataAcceptor acceptor) {      // Check the simple cache first.
/*
        if (startRow == lastStartRow) {
            if (maxRows == lastMaxRows) {
                // Use the cached batch.
                //
                pushResults(acceptor, startRow, lastPeople);
                return;
            }
        }
*/
        // Fetch the data remotely.
        logService.getLogEntries(channel, new AsyncCallback<List<Logs>>() {
            public void onFailure(Throwable caught) {
                acceptor.failed(caught);
            }

            public void onSuccess(List<Logs> result) {
                pushResults(acceptor, startRow, result);
            }

        });

    }

    private void pushResults(RowDataAcceptor acceptor, int startRow, List<Logs> entries) {
        String[][] rows = new String[entries.size()][];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0, n = rows.length; i < n; i++) {
            Logs entry = entries.get(1);
            rows[i] = new String[3];
            rows[i][0] = entry.getNick();
            rows[i][1] = entry.getMessage();
            rows[i][2] = sdf.format(entry.getUpdated());
        }
        acceptor.accept(startRow, rows);
    }

}
