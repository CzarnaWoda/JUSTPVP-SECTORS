package pl.justsectors.packets.impl.money;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class PayTransactionPacket extends RedisPacket {

    private String senderName;
    private String receiverName;
    private int moneyValue;

    public PayTransactionPacket(String senderName, String receiverName, int moneyValue) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.moneyValue = moneyValue;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public int getMoneyValue() {
        return moneyValue;
    }
}
