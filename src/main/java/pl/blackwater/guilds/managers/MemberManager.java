package pl.blackwater.guilds.managers;

import lombok.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.*;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.guilds.data.*;
import pl.blackwaterapi.*;
import pl.blackwaterapi.utils.*;
import pl.justsectors.redis.channels.RedisChannel;

import java.sql.*;
import java.util.*;

public class MemberManager {


    public static Member createMember(Player p, Guild g, String position){
        return createMember(p.getUniqueId(), g, position);
    }

    public static Member createMember(UUID uuid, Guild g, String position){
        return new Member(uuid,g.getName(),g.getTag(),position.toUpperCase());
    }

}
