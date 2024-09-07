package pl.blackwater.core.events;


public enum EventType {

    KILLS("K"),BREAK_STONE ("WK"),OPEN_CHEST("OS"),PUMPKIN("P");

    private String keyVaule;

    EventType(String keyVaule){
        this.keyVaule = keyVaule;
    }

    public String getKeyVaule() {
        return keyVaule;
    }
}

