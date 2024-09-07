package pl.blackwater.core.enums;

import lombok.Getter;

@Getter
public enum  ChestType {
    GEN_NORMAL("gennormal"),UNLIMITED("unlimited"),STATIC("static");

    private String type;

    ChestType(String type){
        this.type = type;
    }
    public static ChestType getByType(String type){
        for(ChestType chest : values()){
            if(chest.getType().equalsIgnoreCase(type)){
                return chest;
            }
        }
        return null;
    }
}
