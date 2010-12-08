using inet

class FantomBot {
    private Str host;
    private Int port;
    private Str? password;
    private Bool connected := false;
    private TcpSocket? socket;
    private OutputQueue? outQueue;
    private InputQueue? inQueue;

    new make(Str hostname, Int portNum := 6667, Str? passwd := null) {
        host = hostname;
        port = portNum;
        password = passwd;
    }

    Void connect() {
        if (isConnected()) {
            throw IOErr.make("The bot is already connected to an IRC server.  Disconnect first.");
        }

        socket := TcpSocket.make()
        socket.connect(IpAddr.make(host), port, 30sec);

        InStream in := socket.in();
        OutStream out := socket.out();
        outQueue := OutputQueue.make(out);
        inQueue := InputQueue.make(in);

        if (password != null && password != "") {
            outQueue.send("PASS " + password);
        }
        
        connected = true;
    }

    Bool isConnected() {
        return connected; 
    }
}
