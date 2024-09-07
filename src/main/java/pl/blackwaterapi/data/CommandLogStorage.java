package pl.blackwaterapi.data;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import pl.blackwater.core.Core;
import pl.blackwaterapi.configs.ConfigCreator;
import pl.blackwaterapi.objects.CommandLog;

import java.util.ArrayList;
import java.util.List;

public class CommandLogStorage extends ConfigCreator {

    @Getter
    public List<CommandLog> commandsLog;

    public CommandLogStorage() {
        super("commandlogs.yml", "command logs", Core.getPlugin());

        final FileConfiguration file = getConfig();

        commandsLog = new ArrayList<>();

        for(String s : getConfig().getStringList("logs")){
            getCommandsLog().add(CommandLog.recover(s));
        }
    }
}
