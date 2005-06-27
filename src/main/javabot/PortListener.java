package javabot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.rickyclarkson.java.lang.Debug;
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
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                InputStreamReader streamReader =
                    new InputStreamReader(inputStream);
                BufferedReader reader =
                    new BufferedReader(streamReader);
                String line = reader.readLine();
                if(password.equals(line)) {
                    Debug.printDebug("About to exit");
                    _quit = true;
                }
                socket.close();
            }
            System.exit(0);
        }
        catch(IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
