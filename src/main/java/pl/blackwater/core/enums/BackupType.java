package pl.blackwater.core.enums;

import lombok.Getter;

@Getter
public enum BackupType {
    MANUAL("manual"), AUTOMATIC("automatic"), CLOSESERVER("closeserver"), GLOBAL("global");

    private String name;

    BackupType(String name) {
        this.name = name;
    }
    public static BackupType getByName(String name){
        for(BackupType type : values()){
            if(type.getName().equalsIgnoreCase(name)){
                return type;
            }
        }
        return null;
    }
}
