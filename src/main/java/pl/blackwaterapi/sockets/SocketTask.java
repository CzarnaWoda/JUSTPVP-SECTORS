package pl.blackwaterapi.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTask implements Runnable
{
    private ServerSocket ss;
    private boolean enable;
    private SocketConfiguration config;
    private Thread t;
    
    public SocketTask(SocketConfiguration config) {
        super();
        this.enable = true;
        this.config = config;
        try {
            this.ss = new ServerSocket(config.getPort());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        (this.t = new Thread(this)).start();
    }
    
    @SuppressWarnings("unused")
	@Override
    public void run() {
        while (this.enable) {
            try {
                Socket clientSocket = this.ss.accept();
                WebSocketTask webSocketTask = new WebSocketTask(new SocketConnection(this.config.getPassword(), clientSocket));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @SuppressWarnings("deprecation")
	public void close() {
        this.enable = false;
        this.t.stop();
        try {
            this.ss.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
