package pl.justsectors.packets.handler;

import com.sk89q.worldguard.LocalPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.blackwater.chestpvpdrop.settings.Config;
import pl.blackwater.core.Core;
import pl.blackwater.core.commands.AdminPanelCommand;
import pl.blackwater.core.commands.TellCommand;
import pl.blackwater.core.data.*;
import pl.blackwater.core.enums.BackupType;
import pl.blackwater.core.events.CustomEvent;
import pl.blackwater.core.events.CustomEventManager;
import pl.blackwater.core.halloween.HalloweenManager;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.inventories.AdminPanelInventory;
import pl.blackwater.core.managers.*;
import pl.blackwater.core.settings.*;
import pl.blackwater.core.utils.LocationUtil;
import pl.blackwater.core.utils.PlayerInventoryUtil;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.data.Member;
import pl.blackwater.guilds.data.War;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.managers.MemberManager;
import pl.blackwater.guilds.managers.WarManager;
import pl.blackwater.guilds.scoreboard.ScoreBoardNameTag;
import pl.blackwater.guilds.settings.GuildConfig;
import pl.blackwater.market.data.Auction;
import pl.blackwater.market.managers.AuctionManager;
import pl.blackwater.storage.data.StorageRent;
import pl.blackwater.storage.managers.StorageRentManager;
import pl.blackwater.storage.settings.StorageConfig;
import pl.blackwaterapi.configs.ConfigCreator;
import pl.blackwaterapi.configs.ConfigManager;
import pl.blackwaterapi.teleport.TeleportableImpl;
import pl.blackwaterapi.utils.*;
import pl.justsectors.packets.impl.auctions.AuctionRegisterPacket;
import pl.justsectors.packets.impl.auctions.AuctionSoldPacket;
import pl.justsectors.packets.impl.backups.BackupCreatePacket;
import pl.justsectors.packets.impl.chat.*;
import pl.justsectors.packets.impl.config.*;
import pl.justsectors.packets.impl.customevents.AddStatCustomEventPacket;
import pl.justsectors.packets.impl.customevents.EndCustomEventPacket;
import pl.justsectors.packets.impl.customevents.StartCustomEventPacket;
import pl.justsectors.packets.impl.global.GlobalEventPacket;
import pl.justsectors.packets.impl.global.GlobalGiftKeysPacket;
import pl.justsectors.packets.impl.guild.*;
import pl.justsectors.packets.impl.halloween.AddPumpkinPacket;
import pl.justsectors.packets.impl.mines.MineBuyPacket;
import pl.justsectors.packets.impl.mines.MineRegisterPacket;
import pl.justsectors.packets.impl.money.PayTransactionPacket;
import pl.justsectors.packets.impl.others.*;
import pl.justsectors.packets.impl.ranks.*;
import pl.justsectors.packets.impl.sectors.SectorStatusPacket;
import pl.justsectors.packets.impl.storage.*;
import pl.justsectors.packets.impl.teleport.PlayerSpawnTeleportPacket;
import pl.justsectors.packets.impl.teleport.PlayerTeleportPacket;
import pl.justsectors.packets.impl.teleport.PlayerToPlayerTeleportPacket;
import pl.justsectors.packets.impl.treasures.TreasureCreatePacket;
import pl.justsectors.packets.impl.treasures.TreasureDeletePacket;
import pl.justsectors.packets.impl.user.*;
import pl.justsectors.packets.impl.warps.WarpDeletePacket;
import pl.justsectors.packets.impl.warps.WarpRegisterPacket;
import pl.justsectors.redis.client.RedisClient;
import pl.justsectors.sectors.Sector;
import pl.justsectors.sectors.SectorManager;
import pl.justsectors.sectors.SectorType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class PacketHandlerImpl implements PacketHandler, Colors {

    @Override
    public void handle(ChatMessagePacket packet) {
        Bukkit.broadcastMessage(Util.fixColor(packet.getMessage()));
    }

    @Override
    public void handle(AdminChatPacket packet) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("core.adminchat.show")) {
                p.sendMessage(packet.getMessage());
            }
        }
    }

    @Override
    public void handle(TellMessagePacket tellMessagePacket) {
        TellCommand.getLastMsg().put(tellMessagePacket.getSenderUUID(), tellMessagePacket.getReceiverUUID());
        TellCommand.getLastMsg().put(tellMessagePacket.getReceiverUUID(), tellMessagePacket.getSenderUUID());
        final Player player = Bukkit.getPlayer(tellMessagePacket.getSenderUUID());
        if(player != null && player.isOnline()) {
            Util.sendMsg(player, ImportantColor + "Ja" + MainColor + " -> " + ImportantColor + tellMessagePacket.getReceiverName() + MainColor + ": " + ImportantColor + tellMessagePacket.getMessage());
        }
        for (final Player plr : Bukkit.getOnlinePlayers()){
            if (AdminPanelCommand.getSS_ADMINS().contains(plr)){
                plr.sendMessage(Util.fixColor(SpecialSigns + "[" + WarningColor + "Szpieg" + SpecialSigns + "]" + WarningColor_2 + tellMessagePacket.getSenderName() + " -> " + tellMessagePacket.getReceiverName() + ": " + tellMessagePacket.getMessage()));
            }
        }
        final Player o = Bukkit.getPlayer(tellMessagePacket.getReceiverUUID());
        if(o != null && o.isOnline()) {
            Util.sendMsg(o, ImportantColor + tellMessagePacket.getSenderName() + MainColor + " -> " + ImportantColor + "Ja" + MainColor + ": " + ImportantColor + tellMessagePacket.getMessage());
        }
    }

    @Override
    public void handle(GuildCreatePacket guildCreatePacket) {
        Guild g = GuildManager.createGuild(guildCreatePacket.getGuildTag(), guildCreatePacket.getGuildName(), guildCreatePacket.getOwnerUUID());
        final Member member = MemberManager.createMember(guildCreatePacket.getOwnerUUID(), g, "OWNER");
        g.addMember(guildCreatePacket.getOwnerUUID(),member);
        for(Player online : Bukkit.getOnlinePlayers()){
            online.playSound(online.getLocation(), Sound.ENDERDRAGON_DEATH, 2.0F,1.0F);
        }
        Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Gildia " + SpecialSigns + "[" + ImportantColor + g.getTag() + SpecialSigns + "]" + ImportantColor + g.getName() + MainColor + " zostala utworzona przez " + ImportantColor + guildCreatePacket.getOwnerName())));
        final Player player = Bukkit.getPlayer(guildCreatePacket.getOwnerName());
        if(player != null && player.isOnline())
        {
            TitleUtil.sendTitle(player,10,100,20, ImportantColor + "Gratulacje!", MainColor + "Utworzyles " + ImportantColor + "gildie");
        }
    }
    @Override
    public void handle(RemoveStatisticPacket packet) {
        final User u = UserManager.getUser(packet.getUserUUID());
        if(u == null) return;
        switch (packet.getStatistic()) {
            case "money":
                u.removeMoney((int) packet.getAmount());
                break;
            case "level":
                u.removeLevel((int) packet.getAmount());
                break;
            case "exp":
                u.removeExp(packet.getAmount());
                break;
            case "placechest":
                u.removePlaceChest((int) packet.getAmount());
                break;
            default:
                System.out.println("PacketHandlerIMPL -> RemoveStatisticPacket -> Doesnt found case to remove stat!");
                break;
        }
    }

    @Override
    public void handle(UserRegisterPacket userRegisterPacket) {
        if(UserManager.getUser(userRegisterPacket.getUuid()) == null) {
            final User u = UserManager.createUser(userRegisterPacket.getUserName(), userRegisterPacket.getUuid(),
                    userRegisterPacket.getAddress(), userRegisterPacket.getGameMode(), userRegisterPacket.isAllowFlight(),
                    null, null);
            Core.getRankManager().implementPermissions(u);
        }
    }

    @Override
    public void handle(AddStatisticPacket packet) {
        final User u = UserManager.getUser(packet.getUserUUID());
        if(u == null) return;
        switch (packet.getStat()) {
            case "money":
                u.addMoney((int) packet.getAmount());
                break;
            case "level":
                u.addLevel((int) packet.getAmount());
                break;
            case "exp":
                u.addExp(packet.getAmount());
                break;
            case "kills":
                u.addKills((int) packet.getAmount());
                break;
            case "throwpearl":
                u.addThrowPearl((int) packet.getAmount());
                break;
            case "eatref":
                u.addEatRef((int) packet.getAmount());
                break;
            case "eatkox":
                u.addEatKox((int) packet.getAmount());
                break;
            case "openpremiumchest":
                u.addOpenPremiumChest((int) packet.getAmount());
                break;
            case "killstreak":
                u.addKillStreak((int) packet.getAmount());
                break;
            case "boostlvl":
                u.addBoostLvL((int) packet.getAmount());
                break;
            case "boostmoney":
                u.addBoostMoney((int) packet.getAmount());
                break;
            case "boostkox":
                u.addBoostKox((int) packet.getAmount());
                break;
            case "boostref":
                u.addBoostRef((int) packet.getAmount());
                break;
            case "boostchest":
                u.addBoostChest((int) packet.getAmount());
                break;
            case "boostkill":
                u.addBoostKill((int) packet.getAmount());
                break;
            case "bonusdrop":
                u.addBounusDrop(packet.getAmount());
                break;
            case "logout":
                u.addLogouts((int) packet.getAmount());
                break;
            case "timeplay":
                u.addTimePlay((int) packet.getAmount());
                break;
            case "placechest":
                u.addPlaceChest((int) packet.getAmount());
                break;
            case "deaths":
                u.addDeaths((int)packet.getAmount());
                break;
            default:
                System.out.println("PacketHandlerIMPL -> AddStatisticPacket -> Doesnt found case to add stat!");
                break;
        }
    }
    @Override
    public void handle(GuildRemovePacket guildRemovePacket) {
        final War war = WarManager.getWar(guildRemovePacket.getGuildTag());
        final Guild g = GuildManager.getGuild(guildRemovePacket.getGuildTag());
        if(war != null) {
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + WarningColor + "WOJNA " + MainColor + "pomiedzy gildia " + ImportantColor + war.getGuild1().getTag() + MainColor + " a " + ImportantColor + war.getGuild2().getTag() + MainColor + " wlasnie sie " + ImportantColor + "zakonczyla" + MainColor + ", wynik wojny: " + ImportantColor + war.getGuild1().getTag() + SpecialSigns + " >> " + ImportantColor + war.getKills1() + MainColor + " %X% " + ImportantColor + war.getGuild2().getTag() + SpecialSigns + " >> " + ImportantColor + war.getKills2())));
            if (war.getGuild1().getTag().equalsIgnoreCase(guildRemovePacket.getGuildTag())) {
                war.getGuild2().addWinWars(1);
                war.getGuild1().addLoseWars(1);
                war.getGuild2().getOnlineMembers().forEach(m -> TitleUtil.sendTitle(m.getPlayer(), 20, 100, 30, Util.fixColor(Util.replaceString(ImportantColor + "GRATULACJE")), Util.fixColor(Util.replaceString(MainColor + "Twoja gildia " + ImportantColor + "wygrala " + MainColor + "wojne"))));
                war.getGuild1().getOnlineMembers().forEach(m -> TitleUtil.sendTitle(m.getPlayer(), 20, 100, 30, Util.fixColor(Util.replaceString(ImportantColor + "PRZYKRO NAM")), Util.fixColor(Util.replaceString(MainColor + "Twoja gildia " + ImportantColor + "przegrala " + MainColor + "wojne"))));
            } else {
                war.getGuild1().addWinWars(1);
                war.getGuild2().addLoseWars(1);
                war.getGuild1().getOnlineMembers().forEach(m -> TitleUtil.sendTitle(m.getPlayer(), 20, 100, 30, Util.fixColor(Util.replaceString(ImportantColor + "GRATULACJE")), Util.fixColor(Util.replaceString(MainColor + "Twoja gildia " + ImportantColor + "wygrala " + MainColor + "wojne"))));
                war.getGuild2().getOnlineMembers().forEach(m -> TitleUtil.sendTitle(m.getPlayer(), 20, 100, 30, Util.fixColor(Util.replaceString(ImportantColor + "PRZYKRO NAM")), Util.fixColor(Util.replaceString(MainColor + "Twoja gildia " + ImportantColor + "przegrala " + MainColor + "wojne"))));
            }
            WarManager.removeWar(war);
        }
        final User user = UserManager.getUser(guildRemovePacket.getExecutorName());
        ScoreBoardNameTag.updateGuildTag(user.getGuild());
        GuildManager.removeGuild(g);
        Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Gildia " + SpecialSigns + "[" + ImportantColor + g.getTag() + SpecialSigns + "]" + ImportantColor + g.getName() + MainColor + " zostala usunieta przez " + ImportantColor + guildRemovePacket.getExecutorName())));

        final Player player = Bukkit.getPlayer(guildRemovePacket.getExecutorName());
        if(player != null && player.isOnline())
        {
            TitleUtil.sendTitle(player,10,100,20, ImportantColor + "Gratulacje!", MainColor + "Rozwiazales " + ImportantColor + "gildie");
        }
    }

    @Override
    public void handle(GuildExpireExtendPacket expireExtendPacket) {
        final Guild g = GuildManager.getGuild(expireExtendPacket.getGuildTag());
        if(g != null)
        {
            g.setExpireTime(expireExtendPacket.getExpireValue());
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Gildia " + SpecialSigns + "[" + ImportantColor + g.getTag() + SpecialSigns + "]" + ImportantColor + g.getName() + MainColor + " zostala przedluzona do " + ImportantColor + Util.getDate(g.getExpireTime()) + MainColor + " przez " + ImportantColor + expireExtendPacket.getExecutorName())));
            final Player player = Bukkit.getPlayer(expireExtendPacket.getExecutorName());
            if(player != null && player.isOnline())
            {
                TitleUtil.sendTitle(player,10,100,20, ImportantColor + "Gratulacje!", MainColor + "Przedluzyles " + ImportantColor + "gildie o 7 dni");
            }
        }
    }

    @Override
    public void handle(GuildInvitePacket guildInvitePacket) {
        final Guild g = GuildManager.getGuild(guildInvitePacket.getGuildTag());
        if(g != null) {
            if(!g.hasInvite(guildInvitePacket.getInvitedUUID())) {
                g.addInvite(guildInvitePacket.getInvitedUUID());
                final Player player = Bukkit.getPlayer(guildInvitePacket.getExecutorName());
                if (player != null && player.isOnline()) {
                    TitleUtil.sendTitle(player, 10, 100, 20, ImportantColor + "Gratulacje!", MainColor + "Wyslano zaproszenie do gracza " + ImportantColor + guildInvitePacket.getInvited());
                    Util.sendMsg(player, ImportantColor + "Wyslano " + MainColor + "zaproszenie dla gracza " + ImportantColor + guildInvitePacket.getInvited());
                }
                final Player o = Bukkit.getPlayer(guildInvitePacket.getInvited());
                if (o != null && o.isOnline()) {
                    TitleUtil.sendTitle(o, 10, 100, 20, ImportantColor + "Uwaga!", MainColor + "Otrzymano zaproszenie do gildii");
                    Util.sendMsg(o, MainColor + "Otrzymales zaproszenie do gildii " + SpecialSigns + "[" + ImportantColor + g.getTag() + SpecialSigns + "]" + ImportantColor + g.getName());
                }
            }
            else
            {
                g.removeInvite(guildInvitePacket.getInvitedUUID());
                final Player player = Bukkit.getPlayer(guildInvitePacket.getExecutorName());
                if (player != null && player.isOnline()) {
                    Util.sendMsg(player, WarningColor + "Cofnieto " + MainColor + "zaproszenie dla gracza " + ImportantColor + guildInvitePacket.getInvited());
                }
                final Player o = Bukkit.getPlayer(guildInvitePacket.getInvited());
                if (o != null && o.isOnline()) {
                    Util.sendMsg(o, MainColor + "Twoje zaproszenie do gildii " + SpecialSigns + "[" + ImportantColor + g.getTag() + SpecialSigns + "]" + ImportantColor + g.getName() + MainColor + " zostalo " + WarningColor + "cofniete");
                }
            }
        }
    }

    @Override
    public void handle(GuildJoinPacket guildJoinPacket) {
        final Guild g = GuildManager.getGuild(guildJoinPacket.getGuildTag());
        if(g != null)
        {
            g.getInvites().invalidate(guildJoinPacket.getInvitedUUID());
            final Player player = Bukkit.getPlayer(guildJoinPacket.getInvitedName());
            final User user = UserManager.getUser(guildJoinPacket.getInvitedUUID());
            final Member member = MemberManager.createMember(user.getUuid(), g, "NORMAL");
            g.addMember(member);
            ScoreBoardNameTag.updateGuildTag(user.getGuild());
            if(player != null && player.isOnline()) {
                TitleUtil.sendTitle(player, 10, 100, 20, ImportantColor + "Gratulacje!", MainColor + "Dolaczono do " + ImportantColor + "gildii");
                Util.sendMsg(player, ImportantColor + "Dolaczyles " + MainColor + "do gildii " + ImportantColor + g.getTag());
                ScoreBoardNameTag.updateBoard(player);
            }
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Gracz " + ImportantColor + guildJoinPacket.getInvitedName() + MainColor + " dolaczyl do gildii " + SpecialSigns + "[" + ImportantColor + g.getTag() + SpecialSigns + "]" + ImportantColor + g.getName())));
        }
    }

    @Override
    public void handle(GuildKickPacket guildKickPacket) {
        final Guild g = GuildManager.getGuild(guildKickPacket.getGuildTag());
        if(g != null)
        {
            g.removeMember(guildKickPacket.getKickedUUID());
            ScoreBoardNameTag.updateGuildTag(g);
            final Player player = Bukkit.getPlayer(guildKickPacket.getExecutorName());
            if(player != null && player.isOnline())
            {
                ScoreBoardNameTag.updateBoard(player);
                TitleUtil.sendTitle(player,10,100,20,ImportantColor + "Gratulacje!", MainColor + "Wyrzuciles gracza " + ImportantColor + guildKickPacket.getKickedName() + MainColor + " z gildii");
            }
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Gracz " + ChatColor.DARK_GREEN + guildKickPacket.getKickedName() + MainColor + " zostal wyrzucony z gildii " + SpecialSigns + "[" + ImportantColor + g.getTag() + SpecialSigns + "]" + ImportantColor + g.getName() + MainColor + " przez " + ImportantColor + guildKickPacket.getExecutorName())));
            final Player kicked = Bukkit.getPlayer(guildKickPacket.getKickedUUID());
            if(kicked != null && kicked.isOnline())
            {
                TitleUtil.sendTitle(kicked,10,100,20,ImportantColor + "Uwaga!", MainColor + "Zostales wyrzucony przez gracza " + ImportantColor + guildKickPacket.getExecutorName() + MainColor + " z gildii");
            }
        }


    }

    @Override
    public void handle(GuildLeavePacket guildLeavePacket) {
        final Guild guild = GuildManager.getGuild(guildLeavePacket.getGuildTag());
        if(guild != null)
        {
            guild.removeMember(guildLeavePacket.getLeaveUUID());
            final Player player = Bukkit.getPlayer(guildLeavePacket.getLeaveUUID());
            if(player != null && player.isOnline()) {
                TitleUtil.sendTitle(player, 10, 100, 20, ImportantColor + "Gratulacje!", MainColor + "Opusciles " + ImportantColor + "gildie");
            }
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Gracz " + ImportantColor + guildLeavePacket.getLeaveName() + MainColor + " opuscil gildie " + SpecialSigns + "[" + ImportantColor + guild.getTag() + SpecialSigns + "]" + ImportantColor + guild.getName())));
        }
    }

    @Override
    public void handle(GuildSetOwnerPacket guildSetOwnerPacket) {
        final Guild g = GuildManager.getGuild(guildSetOwnerPacket.getGuildTag());
        if(g != null)
        {
            g.setOwner(guildSetOwnerPacket.getOwnerUUID());
            final Player player = Bukkit.getPlayer(guildSetOwnerPacket.getExecutorName());
            if(player != null && player.isOnline()) {
                TitleUtil.sendTitle(player, 10, 100, 20, ImportantColor + "Gratulacje!", MainColor + "Zmieniono zalozyciela gildii");
            }
            final Player o = Bukkit.getPlayer(guildSetOwnerPacket.getOwnerUUID());
            if(o != null && o.isOnline()) {
                TitleUtil.sendTitle(o, 10, 100, 20, ImportantColor + "Gratulacje!", MainColor + "Awansowales na zalozyciela gildii");
            }
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Gracz " + ImportantColor + guildSetOwnerPacket.getOwnerName() + MainColor + " zostal nowym zalozycielem gildii " + SpecialSigns + "[" + ImportantColor + g.getTag() + SpecialSigns + "]" + ImportantColor + g.getName())));
        }
    }

    @Override
    public void handle(GuildPlayerLimitPacket playerLimitPacket) {
        final Guild g = GuildManager.getGuild(playerLimitPacket.getGuildTag());
        if(g != null)
        {
            g.setPlayersLimit(playerLimitPacket.getLimit());
            final Player player = Bukkit.getPlayer(playerLimitPacket.getExecutorName());
            if(player != null && player.isOnline()) {
                TitleUtil.sendTitle(player, 10, 100, 20, ImportantColor + "Gratulacje!", MainColor + "Powiekszono limit czlonkow do " + ImportantColor + g.getPlayersLimit());
            }
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Gildia " + SpecialSigns + "[" + ImportantColor + g.getTag() + SpecialSigns + "]" + ImportantColor + g.getName() + MainColor + " powiekszyla swoj limit czlonkow do " + ImportantColor + g.getPlayersLimit() + MainColor + " przez " + ImportantColor + playerLimitPacket.getExecutorName())));
        }
    }

    @Override
    public void handle(GuildSetPreOwnerPacket guildSetPreOwnerPacket) {
        final Guild g = GuildManager.getGuild(guildSetPreOwnerPacket.getGuildTag());
        if(g != null)
        {
            g.setPreOwner(guildSetPreOwnerPacket.getNewOwnerUUID());
            final Player o = Bukkit.getPlayer(guildSetPreOwnerPacket.getNewOwnerUUID());
            if(o != null && o.isOnline())
            {
                TitleUtil.sendTitle(o,10,100,20,ImportantColor + "Gratulacje!", MainColor + "Awansowales na zastepce gildii");
            }
            final Player player = Bukkit.getPlayer(guildSetPreOwnerPacket.getExecutorName());
            if(player != null && player.isOnline()) {
                TitleUtil.sendTitle(player, 10, 100, 20, ImportantColor + "Gratulacje!", MainColor + "Nadano zastepce gildii dla gracza " + ImportantColor + guildSetPreOwnerPacket.getNewOwnerName());
            }
        }
    }

    @Override
    public void handle(GuildSetPvPPacket guildSetPvPPacket) {
        final Guild g = GuildManager.getGuild(guildSetPvPPacket.getGuildTag());
        if(g != null)
        {
            g.setPvp(!g.isPvp());
            for (Member m : g.getOnlineMembers()) {
                Util.sendMsg(m.getPlayer(), Util.replaceString(SpecialSigns + "->> " + MainColor + "PvP w gildi zostalo " + (g.isPvp() ? ChatColor.GREEN + "wlaczone" : ChatColor.DARK_RED + "wylaczone") + MainColor + " przez " + ImportantColor + guildSetPvPPacket.getExecutorName()));
            }
        }
    }

    @Override
    public void handle(GuildWarStartPacket guildWarPacket) {
        final Guild g = GuildManager.getGuild(guildWarPacket.getGuildTag());

        if(g != null)
        {
            g.getOnlineMembers().forEach(m -> Util.sendMsg(m.getPlayer(), Util.replaceString(SpecialSigns + ">> " + WarningColor + "WOJNA " + MainColor + "Twoja gildia wypowiedziała wojne gildii " + ImportantColor + guildWarPacket.getSecondGuildTag() + MainColor + ", wojna zacznie się za " + ImportantColor + "10 minut" + MainColor + ", po tym jak sie zacznie bedzie ona trwała przez " + ImportantColor + "1 godzine" + MainColor + ", po godzinie wojne wygra gildia ktora " + ImportantColor + "zabila wiecej czlonkow wrogiej gildii " + MainColor + "!")));
            ScoreBoardNameTag.updateBoard(g);
        }
        final Guild o = GuildManager.getGuild(guildWarPacket.getSecondGuildTag());
        if(o != null)
        {
            o.getOnlineMembers().forEach(m -> Util.sendMsg(m.getPlayer(), Util.replaceString(SpecialSigns + ">> " + WarningColor + "WOJNA " + MainColor + "Twoja gildia została zaatakowana przez gildie " + ImportantColor + guildWarPacket.getGuildTag() + MainColor + ", wojna zacznie się za " + ImportantColor + "10 minut" + MainColor + ", po tym jak sie zacznie bedzie ona trwała przez " + ImportantColor + "1 godzine" + MainColor + ", po godzinie wojne wygra gildia ktora " + ImportantColor + "zabila wiecej czlonkow wrogiej gildii " + MainColor + "!")));
            ScoreBoardNameTag.updateBoard(o);
        }
        Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Gildia "  + SpecialSigns + "[" + ImportantColor + guildWarPacket.getGuildTag() + SpecialSigns + "]" + ImportantColor + guildWarPacket.getGuildName() + MainColor + " wypowiedziala wojne gildii " + SpecialSigns + "[" + ImportantColor + guildWarPacket.getSecondGuildTag() + SpecialSigns + "]" + ImportantColor + guildWarPacket.getSecondGuildName() + MainColor + ", wojna zacznie sie za " + ImportantColor + "10 minut")));
        WarManager.createWar(g,o,Util.parseDateDiff(GuildConfig.WARMANAGER_WARTIME,true));
    }

    @Override
    public void handle(GuildRemoveSoulsPacket guildRemoveSoulsPacket) {
        final Guild g = GuildManager.getGuild(guildRemoveSoulsPacket.getGuildTag());
        if(g != null)
        {
            g.removeGuildSoul(guildRemoveSoulsPacket.getSoulsToRemove());
        }
    }

    @Override
    public void handle(SendTitlePacket packet) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            TitleUtil.sendFullTitle(p, 20, 100, 30, packet.getTitle(), packet.getSubtitle());
        }
    }
    @Override
    public void handle(ClearInventoryPacket packet){
        final Player player = Bukkit.getPlayer(packet.getPlayerName());
        if(player != null && player.isOnline()){
            PlayerInventoryUtil.ClearPlayerInventory(player);
            Util.sendMsg(player, MainColor + "Twoj ekwipunek zostal wyczyszczony przez " + ImportantColor + packet.getExecutorName() + MainColor + "!");
        }
    }

    @Override
    public void handle(UserGiveEventPacket packet) {
        final User u = UserManager.getUser(packet.getUserName());
        if(u != null) {
            switch (packet.getEventName()) {
                case "turbomoney":
                    u.setEventTurboMoney(2);
                    u.setEventTurboMoneyTime(packet.getTime());
                    break;
                case "turbodrop":
                    u.setTurboDrop(true);
                    u.setTurboDropTime(packet.getTime());
                    break;
                case "turboexp":
                    u.setTurboExp(true);
                    u.setTurboExpTime(packet.getTime());
                    break;
            }
        }else{
            Logger.warning("User doesnt exist !! UserGiveEventPacket! (" + packet.getUserName() + ")");
        }
    }

    @Override
    public void handle(UserRemoveEventPacket packet) {
        final User u = UserManager.getUser(packet.getUserName());
        if(u != null) {
            switch (packet.getEventName()) {
                case "turbomoney":
                    u.setEventTurboMoney(1);
                    break;
                case "turbodrop":
                    u.setTurboDrop(false);
                    break;
                case "turboexp":
                    u.setTurboExp(false);
                    break;
            }
        }else{
            Logger.warning("User doesnt exist !! UserRemoveEventPacket! (" + packet.getUserName() + ")");
        }
    }

    @Override
    public void handle(GlobalEventPacket packet) {
        switch (packet.getEventName()){
            case "turbomoney":
                EventManager.setTurbomoney_admin(packet.getAdmin());
                EventManager.setTurbomoney_time(packet.getTime());
                EventManager.getTurbomoney().put(1,"");
                final Date d = new Date(packet.getTime());
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String timeShow = sdf.format(d);
                Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Administrator " + ImportantColor + packet.getAdmin() + MainColor + " aktywowal event TurboMoney do " + SpecialSigns + "(" + ImportantColor + timeShow  + SpecialSigns + ")")));
                break;
            case "turbodrop":
                EventManager.setTurbodrop_admin(packet.getAdmin());
                EventManager.setTurbodrop_time(packet.getTime());
                EventManager.getTurbodrop().put(1,"");
                final Date d1 = new Date(packet.getTime());
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String timeShow1 = sdf1.format(d1);
                Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Administrator " + ImportantColor + packet.getAdmin() + MainColor + " aktywowal event TurboDrop do " + SpecialSigns + "(" + ImportantColor + timeShow1  + SpecialSigns + ")")));
                break;
            case "turboexp":
                EventManager.setTurboexp_admin(packet.getAdmin());
                EventManager.setTurboexp_time(packet.getTime());
                EventManager.getTurboexp().put(1,"");
                final Date d2 = new Date(packet.getTime());
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String timeShow2 = sdf2.format(d2);
                Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Administrator " + ImportantColor + packet.getAdmin() + MainColor + " aktywowal event TurboEXP do " + SpecialSigns + "(" + ImportantColor + timeShow2  + SpecialSigns + ")")));
                break;
            case "drop":
                final int amount = Config.EVENT_DROP = Integer.parseInt(packet.getMultipler());
                Config.saveConfig();
                EventManager.setDrop_admin(packet.getAdmin());
                EventManager.setDrop_time(packet.getTime());
                EventManager.getDrop().put(1,"");
                final Date d3 = new Date(packet.getTime());
                SimpleDateFormat sdf3 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String timeShow3 = sdf3.format(d3);
                Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Administrator " + ImportantColor + packet.getAdmin() + MainColor + " aktywowal event Drop x" + ImportantColor + amount + MainColor + " do " + SpecialSigns + "(" + ImportantColor + timeShow3  + SpecialSigns + ")")));
                break;
            case "exp":
                final int amount1 = Config.EVENT_EXP = Integer.parseInt(packet.getMultipler());
                Config.saveConfig();
                EventManager.setExp_admin(packet.getAdmin());
                EventManager.setExp_time(packet.getTime());
                EventManager.getExp().put(1,"");
                final Date d4 = new Date(packet.getTime());
                SimpleDateFormat sdf4 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String timeShow4 = sdf4.format(d4);
                Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Administrator " + ImportantColor + packet.getAdmin() + MainColor + " aktywowal event EXP x" + ImportantColor + amount1 + MainColor + " do " + SpecialSigns + "(" + ImportantColor + timeShow4  + SpecialSigns + ")")));
                break;
            case "infinitystone":
                EventManager.setInfinitystone_admin(packet.getAdmin());
                EventManager.setInfinitystone_time(packet.getTime());
                EventManager.getInfinitystone().put(1,"");
                final Date d5 = new Date(packet.getTime());
                SimpleDateFormat sdf5 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String timeShow5 = sdf5.format(d5);
                Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Administrator " + ImportantColor + packet.getAdmin() + MainColor + " aktywowal event InfinityStone do " + SpecialSigns + "(" + ImportantColor + timeShow5  + SpecialSigns + ")")));
                break;
        }
    }

    @Override
    public void handle(FlyTogglePacket packet) {
        final Player o = Bukkit.getPlayer(packet.getUserName());
        final User user = UserManager.getUser(packet.getUserName());
        user.setFly(!user.isFly());
        if(o  != null && o.isOnline()){
            MinecraftServer.getServer().postToMainThread(() -> {
                o.setAllowFlight(!o.getAllowFlight());
                Util.sendMsg(o, ImportantColor + "" + (user.isFly() ? "Wlaczono" : "Wylaczono") + MainColor + " tryb latania przez " + ImportantColor + packet.getExecutor()  + MainColor + "!");
            });
        }

    }

    @Override
    public void handle(GlobalGiftKeysPacket packet) {
        Bukkit.broadcastMessage(Util.fixColor("          "+ MessageConfig.SERVERNAME_TAG + "          "));
        Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Wszyscy Gracze Dostali Klucze Do Skrzynii")));
        Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Nazwa Skrzyni: " + ImportantColor + packet.getChestName() + " " + SpecialSigns + "(" + ImportantColor + packet.getAmount() + " " + MainColor + "szt. " + SpecialSigns + ")")));
        Bukkit.broadcastMessage(Util.fixColor("          "+ MessageConfig.SERVERNAME_TAG + "          "));
        for (Player giftp : Bukkit.getOnlinePlayers()){
            MinecraftServer.getServer().postToMainThread(() -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "case givekey " + giftp.getDisplayName() + " " + packet.getChestName() + " " + packet.getAmount());
            });
        }
    }

    @Override
    public void handle(PlayerTeleportPacket playerTeleportPacket) {
        TeleportManager.registerRequest(playerTeleportPacket.getTeleportPlayer(), new TeleportableImpl("playerLocation",
                new Location(Bukkit.getWorld("world"), playerTeleportPacket.getLocX(), playerTeleportPacket.getLocY(), playerTeleportPacket.getLocZ())
        ));
    }

    @Override
    public void handle(PlayerSpawnTeleportPacket playerSpawnTeleportPacket) {
        TeleportManager.registerRequest(playerSpawnTeleportPacket.getUuid(), new TeleportableImpl("spawnLocation",
                Bukkit.getWorld("world").getSpawnLocation()
        ));
    }

    @Override
    public void handle(PlayerToPlayerTeleportPacket playerToPlayerTeleportPacket) {
        final User targetUser = UserManager.getUser(playerToPlayerTeleportPacket.getTargetPlayer());
        final Player p = targetUser.asPlayer();
        if (p == null || !p.isOnline()) {
            return;
        }
        TeleportManager.registerRequest(playerToPlayerTeleportPacket.getPlayerTeleport(), new TeleportableImpl("playerLocation", p.getLocation()));
    }

    @Override
    public void handle(BackupCreatePacket backupCreatePacket) {
        backupCreatePacket.getBackup().setup();
        Core.getBackupManager().registerBackup(backupCreatePacket.getBackup());
    }

    @Override
    public void handle(AuctionRegisterPacket auctionRegisterPacket) {
        final User user = UserManager.getUser(auctionRegisterPacket.getAuction().getOwner());
        if(user != null)
        {
            user.removeMoney(350);
        }
        auctionRegisterPacket.getAuction().setup();
        AuctionManager.registerAuction(auctionRegisterPacket.getAuction());
        final Player p = Bukkit.getPlayer(auctionRegisterPacket.getOwnerName());
        if(p != null && p.isOnline())
        {
            TitleUtil.sendFullTitle(p, 10, 100, 5, Util.fixColor(Util.replaceString("&4&l%X% &6&lMarket &4&l%X%")), Util.fixColor(Util.replaceString("&6Wystawiono &7przedmiot &6na &7market &6!")));
        }
        final Auction a = auctionRegisterPacket.getAuction();
        final String itemString = ItemManager.get(a.getItem().getType());
        for(Player online : Bukkit.getOnlinePlayers()){
            ChatControlUser cc = ChatControlUserManager.getUser(online);
            ChatControlUserManager.sendMsg(online,
                    Util.fixColor(Util.replaceString("&8&l* &a&lM&2&larket &8&l>> &6Gracz &7" + auctionRegisterPacket.getOwnerName() + " &6wystawil przedmiot &8(&c" + itemString + "&8) &6za &7" + a.getCost() + "&6$"))
                    , cc.isMarketGlobalMessage());
        }
    }

    @Override
    public void handle(AuctionSoldPacket auctionSoldPacket) {
        final Auction a = AuctionManager.getAuction(auctionSoldPacket.getIndex());
        if(a == null) throw new RuntimeException("Sprzedaz aukcji nie powiodla sie ID: " + auctionSoldPacket.getIndex());
        if(a.isSold()){
            return;
        }
        a.setSold(true);
        final Player p = Bukkit.getPlayer(auctionSoldPacket.getBuyerName());
        final User user = UserManager.getUser(auctionSoldPacket.getBuyerName());
        if(user != null)
        {
            user.removeMoney(a.getCost());
        }
        if(p != null && p.isOnline()) {
            TitleUtil.sendFullTitle(p, 10, 100, 5, Util.fixColor(Util.replaceString("&4&l%X% &6&lMarket &4&l%X%")), Util.fixColor(Util.replaceString("&2Kupno przedmiotu przebieglo &a&lpomyslnie")));
        }
        final String itemToString = ItemManager.get(a.getItem().getType());
        for(Player online : Bukkit.getOnlinePlayers()){
            ChatControlUser cc = ChatControlUserManager.getUser(online);
            ChatControlUserManager.sendMsg(online,
                    Util.fixColor(Util.replaceString("&8&l* &a&lM&2&larket &8&l>> &6Gracz &7" + auctionSoldPacket.getBuyerName() + " &6zakupil przedmiot &8(&c" + itemToString + "&8) &6za &7" + a.getCost() + "&6$"))
                    , cc.isMarketGlobalMessage());
        }
        final User ownerUser = UserManager.getUser(auctionSoldPacket.getAuctionOwner());
        if(ownerUser != null){
            ownerUser.addMoney(a.getCost() + 350);
        }
        final Player o = Bukkit.getPlayer(auctionSoldPacket.getAuctionOwner());
        if(o != null && o.isOnline()){
            Util.sendMsg(o, Util.fixColor(Util.replaceString("&8&l* &a&lM&2&larket &8&l>> &6Gracz &7" + auctionSoldPacket.getBuyerName() + " &6zakupil przedmiot &8(&c" + itemToString + "&8) &6za &7" + a.getCost() + "&6$ z twojej &7aukcjii")));
        }
    }

    @Override
    public void handle(WarpRegisterPacket warpRegisterPacket) {
        final Warp warp = warpRegisterPacket.getWarp();
        WarpManager.registerWarp(warp);
        final Player p = Bukkit.getPlayer(warpRegisterPacket.getExecutorUUID());
        if(p != null && p.isOnline())
        {
            Util.sendMsg(p, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Stworzyles nowy warp o nazwie " + ImportantColor + warp.getName() + MainColor + " z uprawieniem " + ImportantColor + warp.getPex().toUpperCase())));
        }
    }

    @Override
    public void handle(WarpDeletePacket warpDeletePacket) {
        final Warp warp = WarpManager.getWarp(warpDeletePacket.getWarpName());
        if(warp != null)
        {
            WarpManager.deleteWarp(warp.getName());
            final Player p = Bukkit.getPlayer(warpDeletePacket.getExecutorUUID());
            if(p != null && p.isOnline()) {
                Util.sendMsg(p, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Usunoles warp " + ImportantColor + warp.getName())));
            }
        }
    }

    @Override
    public void handle(TreasureCreatePacket treasureCreatePacket) {
        Core.getTreasureChestManager().registerTreasure(treasureCreatePacket.getTreasureChest());

        final Player player = Bukkit.getPlayer(treasureCreatePacket.getCreatorUUID());
        if (player != null && player.isOnline()) {
            Util.sendMsg(player, Util.fixColor(Util.replaceString(WarningColor + "ChestManager " + SpecialSigns + "->> " + MainColor + "Stworzono nowa skrzynke o typie " + ImportantColor + treasureCreatePacket.getTreasureChest().getType())));
        }
    }

    @Override
    public void handle(TreasureDeletePacket treasureDeletePacket) {
        final TreasureChest treasureChest = Core.getTreasureChestManager().getChest(treasureDeletePacket.getTreasureUUID());
        Core.getTreasureChestManager().deleteChest(treasureChest);
        final Player player = Bukkit.getPlayer(treasureDeletePacket.getTreasureUUID());
        if (player != null && player.isOnline()) {
            Util.sendMsg(player, Util.fixColor(Util.replaceString(WarningColor + "ChestManager " + SpecialSigns + "->> " + MainColor + "Usunieto skrzynke o typie " + ImportantColor + treasureDeletePacket.getTreasureType())));
        }
    }

    @Override
    public void handle(MineBuyPacket mineBuyPacket) {
        final Mine m = MineManager.getMine(mineBuyPacket.getMineID());
        if(m != null)
        {
            final User user = UserManager.getUser(mineBuyPacket.getBuyerUUID());
            if(user == null) return;
            user.removeMoney(m.getCost());
            user.removeLevel(m.getLevel());
            m.addPlayer(user);
            SectorManager.getCurrentSector().ifPresent(sector -> {
                if(sector.getSectorType() == SectorType.MINE)
                {
                    m.addPlayerToRegion(user);
                }
            });
            user.setStoneLimit(m.getMineStoneLimit());
            final Player p = user.asPlayer();
            if(p != null && p.isOnline())
            {
                p.closeInventory();
                TitleUtil.sendFullTitle(p, 20, 100, 30, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "Mine-Manager " + SpecialSigns + "<<")), Util.fixColor(MainColor + "Zakupiono kopalnie: " + ImportantColor + UnderLined + m.getName()));
                MineManager.OpenMineMenu(p);
            }
        }
    }

    @Override
    public void handle(MineRegisterPacket mineRegisterPacket) {
        MineManager.registerMine(mineRegisterPacket.getMine());
        for(Player p : Bukkit.getOnlinePlayers())
        {
            if(p.isOp())
            {
                Util.sendMsg(p, Util.replaceString(SpecialSigns + ">> " + MainColor + " Utworzono kopalnie " + ImportantColor + UnderLined + mineRegisterPacket.getMine().getName()));
            }
        }
    }

    @Override
    public void handle(SectorStatusPacket sectorStatusPacket) {
        final Sector sector = SectorManager.getSector(sectorStatusPacket.getSectorName());
        if(sector != null)
        {
            sector.setLastUpdate(System.currentTimeMillis());
            sector.setTps(sector.getTps());
        }
    }

    @Override
    public void handle(PayTransactionPacket packet) {
        final User receiver = UserManager.getUser(packet.getReceiverName());
        if(receiver != null)
        {
            receiver.addMoney(packet.getMoneyValue());
            final Player p = Bukkit.getPlayer(receiver.getUuid());
            if(p != null && p.isOnline())
            {
                Util.sendMsg(p, Util.replaceString(SpecialSigns + ">> " + MainColor + " Otrzymano od gracza " + ImportantColor + UnderLined + packet.getSenderName() + MainColor + " monety w ilosci " + ImportantColor + packet.getMoneyValue()));
            }
        }
        final User sender = UserManager.getUser(packet.getSenderName());
        if(sender != null)
        {
            sender.removeMoney(packet.getMoneyValue());
            final Player p = Bukkit.getPlayer(sender.getUuid());
            if(p != null && p.isOnline())
            {
                Util.sendMsg(p, Util.replaceString(SpecialSigns + ">> " + MainColor + " Przelano na konto gracza " + ImportantColor + UnderLined + packet.getReceiverName() + MainColor + " monety w ilosci " + ImportantColor + packet.getMoneyValue()));
            }
        }
    }

    @Override
    public void handle(RankingResetPacket packet) {
        final User u = UserManager.getUser(packet.getReceiverName());
        if(u != null)
        {
            u.setKills(0);
            u.setDeaths(0);
            u.setLogouts(0);
            u.setKillStreak(0);
            u.setLevel(0);
            u.setExp(0);
            u.setTimePlay(0);
            u.setEatKox(0);
            u.setEatRef(0);
            u.setThrowPearl(0);
            u.setOpenPremiumChest(0);
            u.setMoney(0);
            u.setSafeKox(0);
            u.setSafeRef(0);
            u.setSafePearl(0);
            final Player p = Bukkit.getPlayer(packet.getExecutorName());
            if(p != null && p.isOnline())
            {
                Util.sendMsg(p, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Zresetowano ranking gracza " + ImportantColor + u.getLastName() + MainColor + "!")));
            }
        }
    }

    @Override
    public void handle(HelpOpMessagePacket packet) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if ((p.hasPermission("aw") || p.isOp()) && !p.getDisplayName().equalsIgnoreCase(packet.getExecutor())) {
                Util.sendMsg(p, SpecialSigns + "[" + ImportantColor + BOLD + UnderLined + "HelpOP" + SpecialSigns + "]" + ImportantColor + packet.getExecutor() + MainColor + " -> " + ImportantColor + packet.getMessage());
            }
        }
    }

    @Override
    public void handle(JoinSectorPacket packet) {
        final User user = UserManager.getUser(packet.getUserName());
        if(user == null)
        {
            return;
        }
        final Sector sector = SectorManager.getSector(packet.getSectorName());
        if(sector == null)
        {
            throw new RuntimeException("Sektor spalil sie podczas najazdu 3 rzeszy");
        }
        user.setSectorName(packet.getSectorName());
        //user.getSector().getOnlinePlayers().add(user.getLastName());
        //user.getSector().getRedisOnlinePlayers().addAsync(user.getLastName());
    }

    @Override
    public void handle(LeftSectorPacket leftSectorPacket) {
        final Sector sector = SectorManager.getSector(leftSectorPacket.getSectorName());
        if(sector == null)
        {
            throw new RuntimeException("Wykryto urwane smiglo na sektorze " + leftSectorPacket.getSectorName());
        }
        //sector.getOnlinePlayers().remove(leftSectorPacket.getUserName());
        //sector.getRedisOnlinePlayers().removeAsync(leftSectorPacket.getUserName());
    }

    @Override
    public void handle(UserChangeSectorPacket userChangeSectorPacket) {

    }

    @Override
    public void handle(UserEatKoxPacket userEatKoxPacket) {
        final User user = UserManager.getUser(userEatKoxPacket.getUserUUID());
        if(user != null)
        {
            user.addEatKox(1);
        }
    }

    @Override
    public void handle(UserEatRefPacket userEatRefPacket) {
        final User user = UserManager.getUser(userEatRefPacket.getUserUUID());
        if(user != null)
        {
            user.addEatRef(1);
        }
    }

    @Override
    public void handle(UserThrowPearl userThrowPearl) {
        final User u = UserManager.getUser(userThrowPearl.getUserUUID());
        if(u != null)
        {
            u.addThrowPearl(1);
        }
    }

    @Override
    public void handle(StopServerPacket packet) {
        Bukkit.savePlayers();
        RedisClient.sendSectorsPacket(new SectorDisablePacket(CoreConfig.CURRENT_SECTOR_NAME));
        SectorManager.getCurrentSector().ifPresent(sector -> sector.getRedisOnlinePlayers().clear());
        for (Player online : Bukkit.getOnlinePlayers()) {
            final Backup backup = Core.getBackupManager().createBackup(online, BackupType.CLOSESERVER, "CONSOLE");
            backup.insert();
            RedisClient.sendSectorsPacket(new BackupCreatePacket(backup));
            User onlineUser = UserManager.getUser(online);
            onlineUser.update(true);
            online.saveData();
            CombatManager.removeCombat(online);
            UserManager.leaveFromGame(online, false);
            online.kickPlayer(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Serwer jest w trakcie " + WarningColor + BOLD + "wylaczania\n" + SpecialSigns + ">> " + MainColor + "Wejdz ponownie za chwile!")));
        }
        for (World w : Bukkit.getWorlds())
            w.save();
        Bukkit.shutdown();
    }

    @Override
    public void handle(SectorEnablePacket sectorEnablePacket) {
        final Sector sector = SectorManager.getSector(sectorEnablePacket.getSectorName());
        if(sector != null)
        {
            sector.getOnlinePlayers().clear();
        }
    }

    @Override
    public void handle(SectorDisablePacket sectorDisablePacket) {
        final Sector sector = SectorManager.getSector(sectorDisablePacket.getSectorName());
        if(sector != null)
        {
            sector.getOnlinePlayers().clear();
        }
    }

    @Override
    public void handle(SetStatisticPacket setStatisticPacket) {
        final User user = UserManager.getUser(setStatisticPacket.getUserUUID());
        if(user == null) return;
        switch (setStatisticPacket.getStatisticType()){
            case "kills":
                user.setKills((int) setStatisticPacket.getAmount());
                break;
            case "deaths":
                user.setDeaths((int) setStatisticPacket.getAmount());
                break;
            case "logouts":
                user.setLogouts((int) setStatisticPacket.getAmount());
                break;
            case "killstreak":
                user.setKillStreak((int) setStatisticPacket.getAmount());
                break;
            case "level":
                user.setLevel((int) setStatisticPacket.getAmount());
                break;
            case "exp":
                user.setExp((int) setStatisticPacket.getAmount());
                break;
            case "timeplay":
                user.setTimePlay((int) setStatisticPacket.getAmount());
                break;
            case "money":
                user.setMoney((int) setStatisticPacket.getAmount());
                break;
            case "minestonelimit":
                user.setStoneLimit((int) setStatisticPacket.getAmount());
                break;
            case "breakstone":
                user.setDropStones((int) setStatisticPacket.getAmount());
                break;
            case "bonusdrop":
                user.setBonusDrop(setStatisticPacket.getAmount());
                break;
        }
    }

    @Override
    public void handle(ChatControlMessagePacket packet) {
        final ChatControlUser cc = ChatControlUserManager.getUser(packet.getUserUUID());
        boolean b = ChatControlUserManager.getStatus(cc,packet.getChatControlType());
        final Player p = Bukkit.getPlayer(packet.getUserUUID());
        if(p != null && p.isOnline()) {
            ChatControlUserManager.sendMsg(p,Util.replaceString(packet.getMessage()),b);
        }

    }

    @Override
    public void handle(ChatControlGlobalMessagePacket packet) {
        for(Player p : Bukkit.getOnlinePlayers()){
            final ChatControlUser cc = ChatControlUserManager.getUser(p.getUniqueId());
            final boolean b = ChatControlUserManager.getStatus(cc,packet.getType());

            ChatControlUserManager.sendMsg(p,packet.getMessage(),b);
        }
    }

    @Override
    public void handle(UserKickPacket userKickPacket) {
        final Player player = Bukkit.getPlayer(userKickPacket.getTargetUUID());
        if(player != null && player.isOnline()){
            MinecraftServer.getServer().postToMainThread(() -> player.kickPlayer(Util.fixColor(Util.replaceString(userKickPacket.getReason()))));
        }
    }

    @Override
    public void handle(AddPermissionPacket addPermissionPacket) {
        final Rank r = Core.getRankManager().getRank(addPermissionPacket.getRankName());

        r.addPermission(addPermissionPacket.getPermission());
        r.update(true);
    }

    @Override
    public void handle(RemovePermissionPacket removePermissionPacket) {
        final Rank r = Core.getRankManager().getRank(removePermissionPacket.getRankName());

        r.removePermission(removePermissionPacket.getPermission());
        r.update(true);
    }

    @Override
    public void handle(SetSuffixPacket setSuffixPacket) {
        final Rank r = Core.getRankManager().getRank(setSuffixPacket.getRankName());

        r.setSuffix(setSuffixPacket.getSuffix());
        r.update(true);
    }

    @Override
    public void handle(SetPrefixPacket setPrefixPacket) {
        final Rank r = Core.getRankManager().getRank(setPrefixPacket.getRankName());

        r.setPrefix(setPrefixPacket.getPrefix());
        r.update(true);

    }

    @Override
    public void handle(CreateRankPacket createRankPacket) {
        Core.getRankManager().createRank(createRankPacket.getRankName());
    }

    @Override
    public void handle(SetConfigStringPacket packet) {
        final ConfigCreator configCreator = ConfigManager.getConfig(packet.getConfigName());

        final FileConfiguration config = configCreator.getConfig();

        configCreator.setField(packet.getPath(),packet.getVariable());
        configCreator.reloadConfig();

        if(packet.isOpenAdminPanelInventory()){
            final Player player = Bukkit.getPlayer(packet.getPacketExecutor());
            if(player != null && player.isOnline()){
                if(player.getOpenInventory() != null){
                    player.closeInventory();
                }
                AdminPanelInventory.openGlobal(player);
            }
        }
    }

    @Override
    public void handle(SetConfigIntPacket packet) {
        final ConfigCreator configCreator = ConfigManager.getConfig(packet.getConfigName());

        final FileConfiguration config = configCreator.getConfig();

        configCreator.setField(packet.getPath(),packet.getVariable());
        configCreator.saveConfig();

        if(packet.isOpenAdminPanelInventory()){
            final Player player = Bukkit.getPlayer(packet.getPacketExecutor());
            if(player != null && player.isOnline()){
                if(player.getOpenInventory() != null){
                    player.closeInventory();
                }
                AdminPanelInventory.openGlobal(player);
            }
        }
    }

    @Override
    public void handle(SetConfigBooleanPacket packet) {
        final ConfigCreator configCreator = ConfigManager.getConfig(packet.getConfigName());

        final FileConfiguration config = configCreator.getConfig();

        configCreator.setField(packet.getPath(),packet.isVariable());

        System.out.println("PAKIET " + (packet.isVariable() ? 1 : 0)  + " dla " + packet.getPath() + " , " + configCreator.getConfigName());

        configCreator.saveConfig();

        if(packet.isOpenAdminPanelInventory()){
            final Player player = Bukkit.getPlayer(packet.getPacketExecutor());
            if(player != null && player.isOnline()){
                if(player.getOpenInventory() != null){
                    player.closeInventory();
                }
                AdminPanelInventory.openGlobal(player);
            }
        }
    }

    @Override
    public void handle(SetConfigDoublePacket packet) {
        final ConfigCreator configCreator = ConfigManager.getConfig(packet.getConfigName());

        final FileConfiguration config = configCreator.getConfig();

        configCreator.setField(packet.getPath(),packet.getVariable());


        configCreator.saveConfig();

        if(packet.isOpenAdminPanelInventory()){
            final Player player = Bukkit.getPlayer(packet.getPacketExecutor());
            if(player != null && player.isOnline()){
                if(player.getOpenInventory() != null){
                    player.closeInventory();
                }
                AdminPanelInventory.openGlobal(player);
            }
        }
    }

    @Override
    public void handle(SetConfigLongPacket packet) {
        final ConfigCreator configCreator = ConfigManager.getConfig(packet.getConfigName());

        final FileConfiguration config = configCreator.getConfig();

        configCreator.setField(packet.getPath(),packet.getVariable());
        configCreator.saveConfig();

        if(packet.isOpenAdminPanelInventory()){
            final Player player = Bukkit.getPlayer(packet.getPacketExecutor());
            if(player != null && player.isOnline()){
                if(player.getOpenInventory() != null){
                    player.closeInventory();
                }
                AdminPanelInventory.openGlobal(player);
            }
        }

    }

    @Override
    public void handle(ItemShopPacket packet) {
        final User u = UserManager.getUser(packet.getPlayerName());
        switch (packet.getName()) {
            case "vip":
                final Player player = Bukkit.getPlayer(packet.getPlayerName());
                if(player != null && player.isOnline()) {
                    RankSetManager.setRank(u, "ITEMSHOP", Core.getRankManager().getRank("vip"), 0L);
                }else{
                    if(u.getSector().getSectorName().equalsIgnoreCase(SectorManager.getCurrentSector().get().getSectorName())){
                        RankSetManager.setRank(u, "ITEMSHOP", Core.getRankManager().getRank("vip"), 0L);
                    }
                }
                break;
            case "svip":
                final Player player1 = Bukkit.getPlayer(packet.getPlayerName());
                if(player1 != null && player1.isOnline()) {
                    RankSetManager.setRank(u, "ITEMSHOP", Core.getRankManager().getRank("svip"), 0L);
                }else{
                    if(u.getSector().getSectorName().equalsIgnoreCase(SectorManager.getCurrentSector().get().getSectorName())){
                        RankSetManager.setRank(u, "ITEMSHOP", Core.getRankManager().getRank("svip"), 0L);
                    }
                }
                break;
            case "chestpvp10":
                final Player player2 = Bukkit.getPlayer(packet.getPlayerName());
                if(player2 != null && player2.isOnline()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "case givekey " + u.getLastName() + " chestpvp 10");
                }
                break;
            case "startpack5":
                final Player player3 = Bukkit.getPlayer(packet.getPlayerName());
                if(player3 != null && player3.isOnline()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "case givekey " + u.getLastName() + " startpack 5");
                }
                break;
            case "minechest10":
                final Player player4 = Bukkit.getPlayer(packet.getPlayerName());
                if(player4 != null && player4.isOnline()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "case givekey " + u.getLastName() + " minechest 10");
                }
                break;
            case "money10":
                u.addMoney(10000);
                break;
            case "money25":
                u.addMoney(25000);
                break;
            case "money50":
                u.addMoney(50000);
                break;
            case "turbomoney2h":
                u.setEventTurboMoney(2);
                final long time = Util.parseDateDiff("2h", true);
                u.setEventTurboMoneyTime(time);
                break;
            case "turbodropexp1h":
                u.setTurboDrop(true);
                u.setTurboDropTime(Util.parseDateDiff("1h", true));
                u.setTurboExp(true);
                u.setTurboExpTime(Util.parseDateDiff("1h", true));
                break;
            case "dropx21h":
                Config.EVENT_DROP = 2;
                Config.saveConfig();
                EventManager.setDrop_admin(u.getLastName());
                final long time3 = Util.parseDateDiff("1h", true);
                EventManager.setDrop_time(time3);
                EventManager.getDrop().put(1, "");
                break;
            case "expx21h":
                Config.EVENT_EXP = 2;
                Config.saveConfig();
                EventManager.setExp_admin(u.getLastName());
                final long time4 = Util.parseDateDiff("1h", true);
                EventManager.setExp_time(time4);
                EventManager.getExp().put(1, "");
                break;
            case "chestpvp10startpack10":
                final Player player5 = Bukkit.getPlayer(packet.getPlayerName());
                if(player5 != null && player5.isOnline()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "case givekey " + u.getLastName() + " chestpvp 10");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "case givekey " + u.getLastName() + " startpack 10");
                }
                break;
            case "minechest20startpack5":
                final Player player6 = Bukkit.getPlayer(packet.getPlayerName());
                if(player6 != null && player6.isOnline()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "case givekey " + u.getLastName() + " minechest 20");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "case givekey " + u.getLastName() + " startpack 5");
                }
                break;
            case "startpack15":
                final Player player7 = Bukkit.getPlayer(packet.getPlayerName());
                if(player7 != null && player7.isOnline()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "case givekey " + u.getLastName() + " startpack 15");
                }
                break;
        }
    }

    @Override
    public void handle(LoadConfigPacket packet) {
        switch (packet.getConfig()){
            case "coreconfig.yml":
                new CoreConfig();
                break;
            case "guildconfig.yml":
                new GuildConfig();
                break;
            case "kits.yml":
                new KitsConfig();
                break;
            case "tablist.yml":
                new TabListConfig();
                break;
            case "effect.yml":
                new EffectConfig();
                break;
            case "drops.yml":
                Config.reloadConfig();
                new Config();
                break;
        }
    }

    @Override
    public void handle(ChatTogglePacket packet) {
        final ChatManager manager = Core.getChatManager();
        manager.toggleChat(packet.getSener(),packet.isStatus());
    }

    @Override
    public void handle(ChatVipTogglePacket packet) {
        final ChatManager manager = Core.getChatManager();
        manager.toggleVipChat(packet.getSender(),packet.isStatus());
    }

    @Override
    public void handle(ProtectionDisablePacket packet) {
        final User user = UserManager.getUser(packet.getUuid());
        user.setProtection(0L);
    }

    @Override
    public void handle(StorageBuyPacket packet) {
        final StorageRent storage = StorageRentManager.getStorageRent(packet.getStorageID());
        final User u = UserManager.getUser(packet.getUuid());

        u.removeMoney(storage.getCost());

        storage.setOwner(u.getUuid());
        storage.setExpireTime(System.currentTimeMillis() + storage.getRentTime());
        storage.setRent(true);

        if(SectorManager.getCurrentSector().get().getSectorType().equals(SectorType.STORAGE)) {
            storage.getStorageRegion().getMembers().clear();
            storage.getStorageRegion().getMembers().addPlayer(u.getUuid());
            final Sign sign = storage.getSign();
            sign.setLine(0, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Wlasciciel ")));
            sign.setLine(1, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + ImportantColor + u.getLastName())));
            sign.setLine(2, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Wygasa za")));
            sign.setLine(3, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + Util.secondsToString((int) ((int)((int)storage.getExpireTime() - System.currentTimeMillis()) / 1000L)))));
            sign.update();

            storage.update(true);
        }
    }

    @Override
    public void handle(StorageAddExpireTimePacket packet) {
        final StorageRent storage = StorageRentManager.getStorageRent(packet.getStorageID());
        final User u = UserManager.getUser(packet.getUuid());

        u.removeMoney(StorageConfig.STORAGE_DAYCOST);
        storage.addExpireTime(TimeUtil.DAY.getTime(1));
        if(SectorManager.getCurrentSector().get().getSectorType().equals(SectorType.STORAGE)) {
            storage.update(true);
        }
    }

    @Override
    public void handle(StorageRemoveMemberPacket packet) {
        final StorageRent storage = StorageRentManager.getStorageRent(packet.getStorageID());
        final User u = UserManager.getUser(packet.getUuid());


        storage.removeMember(packet.getUuid().toString());
        if(SectorManager.getCurrentSector().get().getSectorType().equals(SectorType.STORAGE)) {
            storage.getStorageRegion().getMembers().removePlayer(u.getUuid());
            storage.update(true);
        }
    }

    @Override

    public void handle(StorageAddMemberPacket packet) {
        final StorageRent storage = StorageRentManager.getStorageRent(packet.getStorageID());
        final User u = UserManager.getUser(packet.getUuid());
        storage.addMember(packet.getUuid().toString());
        if(SectorManager.getCurrentSector().get().getSectorType().equals(SectorType.STORAGE)) {
            storage.getStorageRegion().getMembers().addPlayer(u.getUuid());
            storage.update(true);
        }

    }

    @Override
    public void handle(StorageLeftPacket packet) {
        final StorageRent storage = StorageRentManager.getStorageRent(packet.getStorageID());

        storage.setOwner(null);
        storage.setExpireTime(0L);
        storage.setRent(false);
        storage.setMembers("");
        if(SectorManager.getCurrentSector().get().getSectorType().equals(SectorType.STORAGE)) {
            storage.getStorageRegion().getMembers().clear();
            storage.getStorageRegion().getOwners().clear();
            Sign sign = storage.getReSign();
            sign.setLine(0, Util.fixColor(Util.replaceString("->> " + MainColor + "DO KUPNA")));
            sign.setLine(1, Util.fixColor(Util.replaceString("->> " + MainColor + "ID: " + SpecialSigns + storage.getId())));
            sign.setLine(2, Util.fixColor(Util.replaceString("->> " + MainColor + "Koszt: " + SpecialSigns + storage.getCost() + "$")));
            sign.setLine(3, Util.fixColor(Util.replaceString("->> " + MainColor + "Na czas: " + SpecialSigns + Util.secondsToString((int)(storage.getRentTime() / 1000)))));
            sign.update();

        }

        }

    @Override
    public void handle(StorageCreatePacket packet) {
        if(!SectorManager.getCurrentSector().get().getSectorType().equals(SectorType.STORAGE)) {
            StorageRent storage = StorageRentManager.addStorageRent(LocationUtil.convertStringToBlockLocation(packet.getSignLocation()), packet.getRentTime(), packet.getCost(), packet.getStorageRegion(), LocationUtil.convertStringToBlockLocation(packet.getInteractBlockLocation()));
        }
    }

    @Override
    public void handle(GuildEffectPacket packet) {
        final PotionEffect potionEffect = new PotionEffect(PotionEffectType.getByName(packet.getType()),packet.getDuration(),packet.getAmplifier(),true);

        final Guild guild = GuildManager.getGuild(packet.getGuildTag());

        if(guild != null) {
            for (Member member : guild.getGuildMembers().values()) {
                if (member != null && member.getPlayer() != null && member.getPlayer().isOnline()) {
                    final Player player = member.getPlayer();
                    player.addPotionEffect(potionEffect, true);
                    Util.sendMsg(player, "&2&lGILDIE &8->> &7Orzymales efekt zakupiony dla &acalej gildii&7!");
                }
            }
        }
    }

    @Override
    public void handle(StartCustomEventPacket packet) {
        if(CustomEventManager.getActiveEvent() == null) {
            CustomEventManager.startEvent(packet.getEventType(), packet.getEventTime());
        }
    }

    @Override
    public void handle(AddStatCustomEventPacket packet) {
        if(CustomEventManager.getActiveEvent() != null) {
            CustomEventManager.getActiveEvent().addStatForEvent(packet.getUuid(), packet.getAmount());
        }
    }

    @Override
    public void handle(GuildAddSoulsPacket packet) {
        final Guild guild = GuildManager.getGuild(packet.getGuildTag());
        if(guild != null){
            guild.addGuildSouls(packet.getAmount());
        }
    }

    @Override
    public void handle(EndCustomEventPacket packet) {
        CustomEventManager.endEvent();
    }

    @Override
    public void handle(GuildSetSoulsPacket packet) {
        final Guild guild = GuildManager.getGuild(packet.getGuildTag());
        if(guild != null){
            guild.setGuildSoul(packet.getAmonut());
        }
    }

    @Override
    public void handle(AddPumpkinPacket addPumpkinPacket) {
        HalloweenManager.registerLocationToResp(addPumpkinPacket.getLocation());
    }
}