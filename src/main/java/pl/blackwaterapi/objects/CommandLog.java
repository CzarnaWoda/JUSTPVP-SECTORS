package pl.blackwaterapi.objects;

import lombok.Data;
import lombok.NonNull;
import pl.blackwater.core.Core;
import pl.blackwaterapi.store.Entry;
import pl.blackwaterapi.utils.Logger;

@Data
public class CommandLog implements Entry {

    @NonNull private String command;
    @NonNull private String admin;
    @NonNull private long time;

    @Override
    public void insert() {
        Core.getCommandLogStorage().addToListField("logs",convert());
    }

    @Override
    public void update(boolean p0) {
        Logger.warning("Cant update this object");
    }

    @Override
    public void delete() {
        Core.getCommandLogStorage().removeToListField("logs",convert());
    }

    private String convert(){
        return command+"-"+admin+"-"+time;
    }

    public static CommandLog recover(String commandLog){
        final String[] array = commandLog.split("-");
        final String command = array[0];
        final String admin = array[1];
        final long time = Long.parseLong(array[2]);

        return new CommandLog(command,admin,time);
    }
}
