package pl.blackwaterapi.sockets;

import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

public class WebSocketTask implements Runnable
{
    private static AtomicInteger ai;
    private SocketConnection conn;
    
    public WebSocketTask(SocketConnection conn) {
        super();
        this.conn = conn;
        Thread t = new Thread(this, "WebSocketTask #" + WebSocketTask.ai.getAndIncrement());
        t.start();
    }
    
    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(this.conn.getSocket().getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.conn.getSocket().getInputStream()));
            String password = in.readLine();
            if (!this.conn.getPass().equals(password)) {
                out.println("WRONG PASSWORD");
                out.flush();
            }
            else {
                out.println("PASSWORD OK");
                out.flush();
                WebSocketEvent event = new WebSocketEvent(in.readLine());
                Bukkit.getPluginManager().callEvent(event);
                out.println(event.getResponse());
                out.println("DONE");
                out.flush();
            }
            if (this.conn.getSocket().isConnected()) {
                this.conn.getSocket().close();
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    static {
        WebSocketTask.ai = new AtomicInteger(0);
    }
}
