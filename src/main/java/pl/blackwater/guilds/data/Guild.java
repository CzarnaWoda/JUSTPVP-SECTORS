package pl.blackwater.guilds.data;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.utils.ListUtil;
import pl.blackwater.guilds.managers.AllianceManager;
import pl.blackwater.guilds.managers.MemberManager;
import pl.blackwaterapi.store.Entry;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.MathUtil;
import pl.blackwaterapi.utils.TimeUtil;
import pl.justsectors.redis.channels.RedisChannel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Data
public class Guild implements Entry {

    private String tag;
    private String name;
    private UUID owner;
    private boolean pvp;
    private boolean preDeleted;
    private long createTime;
    private long expireTime;
    private int playersLimit;
    private int guildSoul;
    private transient Cache<UUID, Long> invites = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.SECONDS).build();
    private final Map<UUID, Member> guildMembers = new ConcurrentHashMap<>();
    private int winWars,loseWars,drawWars;
    private List<String> declareWar = new ArrayList<>();
    private int life;

    public void setup()
    {
        invites = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.SECONDS).build();
    }

    public Guild(String tag, String name, Player owner){
        this.tag = tag;
        this.name = name;
        this.owner = owner.getUniqueId();
        this.pvp = false;
        this.preDeleted = false;
        this.createTime = System.currentTimeMillis();
        this.expireTime = System.currentTimeMillis() + TimeUtil.DAY.getTime(7);
        this.playersLimit = 5;
        this.guildSoul = 0;
        this.winWars = 0;
        this.loseWars = 0;
        this.drawWars = 0;
        this.life = 3;
    }

    public Guild(String tag, String name, UUID ownerID){
        this.tag = tag;
        this.name = name;
        this.owner = ownerID;
        this.pvp = false;
        this.preDeleted = false;
        this.createTime = System.currentTimeMillis();
        this.expireTime = System.currentTimeMillis() + TimeUtil.DAY.getTime(7);
        this.playersLimit = 5;
        this.guildSoul = 0;
        this.winWars = 0;
        this.loseWars = 0;
        this.drawWars = 0;
        this.life = 3;
    }


    @Override
    public void insert() {
        RedisChannel.INSTANCE.GUILDS.putAsync(getTag(), GsonUtil.toJson(this));
    }

    @Override
    public void update(boolean b) {
        insert();
    }

    @Override
    public void delete() {
        for(Alliance a : AllianceManager.getGuildAlliances(this)){
            a.delete();
        }
        RedisChannel.INSTANCE.GUILDS.removeAsync(this.tag);
    }


    private Member getPreOwnerMember(){
        for(Member m : getGuildMembers().values()){
            if(m.getPosition().equalsIgnoreCase("PREOWNER")){
                return m;
            }
        }
        return null;
    }
    private Member getOwnerMember(){
        for(Member m : getGuildMembers().values()){
            if(m.getPosition().equalsIgnoreCase("OWNER")){
                return m;
            }
        }
        return null;
    }

    public Set<Member> getOnlineMembers() {
        final Set<Member> members = new HashSet<>();
        for (Member guildMember : this.guildMembers.values()) {
            final User user = UserManager.getUser(guildMember.getUuid());
            if(user != null && user.isOnline()){
                members.add(guildMember);
            }
        }
        return members;
    }



    public void setOwner(Player player){
        Member ownerMember = getOwnerMember();
        Member realOwner = getMember(player.getUniqueId());
        assert ownerMember != null;
        ownerMember.setPosition("NORMAL");
        assert realOwner != null;
        realOwner.setPosition("OWNER");
        this.owner = player.getUniqueId();
    }

    public void setOwner(final UUID playerUUID){
        Member ownerMember = getOwnerMember();
        Member realOwner = getMember(playerUUID);
        assert ownerMember != null;
        ownerMember.setPosition("NORMAL");
        assert realOwner != null;
        realOwner.setPosition("OWNER");
        this.owner = playerUUID;
    }


    public boolean isOwner(UUID u){
        return  getOwner().equals(u);
    }
    public boolean canExpire(UUID u){
        Member m = getMember(u);
        assert m != null;
        return (m.getPosition().equalsIgnoreCase("OWNER") || m.getPosition().equalsIgnoreCase("MOD") || m.getPosition().equalsIgnoreCase("PREOWNER"));
    }
    public boolean canInvite(UUID u){
        Member m = getMember(u);
        assert m != null;
        return (m.getPosition().equalsIgnoreCase("OWNER") || m.getPosition().equalsIgnoreCase("PREOWNER"));
    }
    public boolean canKick(UUID u){
        Member m = getMember(u);
        assert m != null;
        return (m.getPosition().equalsIgnoreCase("OWNER") || m.getPosition().equalsIgnoreCase("PREOWNER"));
    }
    public void addOneWeekToExpireTime(){
        expireTime += 604800000L;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public int getKills(){
        int kills = 0;
        for(Member m : getGuildMembers().values()){
            User u = UserManager.getUser(m.getUuid());
            kills += u.getKills();
        }
        return kills;
    }
    public int getDeaths(){
        int deaths = 0;
        for(Member m : getGuildMembers().values()){
            User u = UserManager.getUser(m.getUuid());
            deaths += u.getDeaths();
        }
        return deaths;
    }
    public int getPoints(){
        int points = 0;
        points = points + getKills();
        return points;
    }
    public double getKD(){
        if(getKills() == 0 && getDeaths() == 0){
            return 0.0D;
        }
        if(getKills() > 0 && getDeaths() == 0){
            return getKills();
        }
        if(getDeaths() > 0 && getKills() == 0){
            return -getDeaths();
        }
        return MathUtil.round(getKills()/getDeaths(), 2);
    }

    public OfflinePlayer getPreOwner(){
        OfflinePlayer player = null;
        for(Member m : getGuildMembers().values())
            if (m.getPosition().equalsIgnoreCase("PREOWNER")) {
                player = Bukkit.getOfflinePlayer(m.getUuid());
            }
        return player;
    }

    public boolean hasInvite(UUID u){
        return getInvites().getIfPresent(u) != null;
    }

    public void addInvite(UUID u){
        getInvites().put(u, System.currentTimeMillis());
    }

    public void removeInvite(UUID u){
        getInvites().invalidate(u);
    }

    public boolean setPreOwner(Player player){
        Member preowner = getPreOwnerMember();
        final Member m = getMember(player.getUniqueId());
        assert m != null;
        m.setPosition("PREOWNER");
        if(preowner != null){
            preowner.setPosition("NORMAL");
            return true;
        }
        return false;
    }

    public boolean setPreOwner(UUID player){
        Member preowner = getPreOwnerMember();
        final Member m = getMember(player);
        assert m != null;
        m.setPosition("PREOWNER");
        if(preowner != null){
            preowner.setPosition("NORMAL");
            return true;
        }
        return false;
    }

    public boolean isCanPlayerLimit(UUID uuid){
        Member m = getMember(uuid);
        assert m != null;
        return (m.getPosition().equalsIgnoreCase("OWNER") || m.getPosition().equalsIgnoreCase("PREOWNER") || m.getPosition().equalsIgnoreCase("MOD"));
    }
    public boolean isCanChangePvP(UUID uuid){
        Member m = getMember(uuid);
        assert m != null;
        return (m.getPosition().equalsIgnoreCase("OWNER") || m.getPosition().equalsIgnoreCase("PREOWNER") || m.getPosition().equalsIgnoreCase("MOD"));
    }
    public void addPlayersLimit(int limit){
        playersLimit += limit;
    }

    public void setPlayersLimit(int playersLimit) {
        this.playersLimit = playersLimit;
    }

    public boolean isMember(final UUID uuid){
        return this.guildMembers.get(uuid) != null;
    }

    public Member getMember(final UUID uuid){
        return this.guildMembers.get(uuid);
    }

    public void addMember(final Member member)
    {
        this.guildMembers.put(member.getUuid(), member);
    }

    public void addMember(final UUID uuid, final Member guildMember) {
        this.guildMembers.put(uuid, guildMember);
    }

    public void removeMember(final UUID uuid) {
        this.guildMembers.remove(uuid);
    }

    public void removeGuildSoul(int remove){
        guildSoul -= remove;
    }
    public void  addWinWars(int wins){
        winWars += wins;
        update(false);
    }
    public void  addLoseWars(int lose){
        loseWars += lose;
        update(false);
    }
    public void  addDrawWars(int draw){
        drawWars += draw;
        update(false);
    }
    public void  addLife(int life){
        this.life += life;
        update(false);
    }
    public void  removeLife(int life){
        this.life -= life;
        update(false);
    }
    public void addGuildSouls(int souls){
        this.guildSoul += souls;
    }
}
