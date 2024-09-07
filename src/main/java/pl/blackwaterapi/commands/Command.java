package pl.blackwaterapi.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwaterapi.data.APIConfig;
import pl.blackwaterapi.objects.CommandLog;
import pl.blackwaterapi.utils.Util;

import java.util.Arrays;

public abstract class Command extends org.bukkit.command.Command
{
    private String name;
    private String usage;
    private String desc;
    private String permission;

	public Command(String name, String desc, String usage, String permission, String... aliases) {
        super(name, desc, usage, Arrays.asList(aliases));
        this.name = name;
        this.usage = usage;
        this.desc = desc;
        this.permission = permission;
    }
    
    public boolean execute(CommandSender sender, String label, String[] args) {
	    if(sender.hasPermission("api.commandlog")){
	        final CommandLog commandLog = new CommandLog("/"+label.replace("-","") + " " + StringUtils.join(args," ").replace("-",""),sender.getName(),System.currentTimeMillis());
	        commandLog.insert();
            Core.getCommandLogStorage().getCommandsLog().add(commandLog);
        }
	    if(sender instanceof Player && APIConfig.SUPERADMINSYSTEM_ADMINUUID.contains(((Player) sender).getUniqueId().toString())){
            return this.onExecute(sender, args);
        }
        if (!sender.hasPermission(this.permission)) {
            return Util.sendMsg(sender, Util.replaceString("&4%V% &cNie posiadasz uprawnien &8(&4" + this.permission + "&8)"));
        }
        return this.onExecute(sender, args);
    }
    
    public abstract boolean onExecute(CommandSender p0, String[] p1);

    public String getName() {
        return this.name;
    }

    public String getUsage() {
        return this.usage;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getPermission() {
        return this.permission;
    }
}
