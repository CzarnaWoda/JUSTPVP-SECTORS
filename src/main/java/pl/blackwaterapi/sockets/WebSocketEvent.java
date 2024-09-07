package pl.blackwaterapi.sockets;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WebSocketEvent extends Event
{
    private static HandlerList handlerList;
    private String message;
    private String response;
    
    public WebSocketEvent(String message) {
        super(true);
        this.response = "";
        this.message = message;
    }
    
    public HandlerList getHandlers() {
        return WebSocketEvent.handlerList;
    }
    
    public static HandlerList getHandlerList() {
        return WebSocketEvent.handlerList;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public String getResponse() {
        return this.response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    static {
        WebSocketEvent.handlerList = new HandlerList();
    }
}
