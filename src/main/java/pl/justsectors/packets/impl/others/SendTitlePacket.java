package pl.justsectors.packets.impl.others;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class SendTitlePacket extends RedisPacket {

    private String title;
    private String subtitle;

    public SendTitlePacket(){}

    public SendTitlePacket(String title, String subtitle){
        this.title = title;
        this.subtitle =subtitle;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getTitle() {
        return title;
    }
}
