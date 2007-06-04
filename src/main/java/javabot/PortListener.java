package javabot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PortListener implements Runnable {
    private static Log log = LogFactory.getLog(PortListener.class);
    private Thread thread = new Thread(this);
    private int port;
    private String password;
    private boolean _quit;

    public PortListener(int portNum, String botPassword) {
        port = portNum;
        password = botPassword;
    }

    public void start() {
        thread.start();
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(!_quit) {
                BufferedReader reader = null;
                try {
                    Socket socket = serverSocket.accept();
                    InputStream inputStream = socket.getInputStream();
                    InputStreamReader streamReader = new InputStreamReader(inputStream);
                    reader = new BufferedReader(streamReader);
                    String line = reader.readLine();
                    log.debug(password);
                    if(password.equals(line)) {
                        log.debug("About to exit");
                        _quit = true;
                    }
                    socket.close();
                } finally {
                    reader.close();
                }
            }
            System.exit(0);
        }
        catch(IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
