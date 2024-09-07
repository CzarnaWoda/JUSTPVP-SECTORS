package pl.blackwaterapi.sockets;

import java.net.Socket;

public class SocketConnection
{
    private String pass;
    private Socket socket;
    
    public String getPass() {
        return this.pass;
    }
    
    public Socket getSocket() {
        return this.socket;
    }
    
    public SocketConnection(String pass, Socket socket) {
        super();
        this.pass = pass;
        this.socket = socket;
    }
}
