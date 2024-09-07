package pl.blackwaterapi.sockets;

public class SocketConfiguration
{
    private int port;
    private String password;
    
    public SocketConfiguration(int port, String password) {
        super();
        this.port = port;
        this.password = password;
    }
    
    public int getPort() {
        return this.port;
    }
    
    String getPassword() {
        return this.password;
    }
}
