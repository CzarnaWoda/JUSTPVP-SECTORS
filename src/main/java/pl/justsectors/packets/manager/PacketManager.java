package pl.justsectors.packets.manager;

import pl.blackwaterapi.teleport.PlayerTeleport;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.auctions.AuctionRegisterPacket;
import pl.justsectors.packets.impl.auctions.AuctionSoldPacket;
import pl.justsectors.packets.impl.backups.BackupCreatePacket;
import pl.justsectors.packets.impl.bans.BanAddPacket;
import pl.justsectors.packets.impl.chat.*;
import pl.justsectors.packets.impl.config.*;
import pl.justsectors.packets.impl.customevents.AddStatCustomEventPacket;
import pl.justsectors.packets.impl.customevents.EndCustomEventPacket;
import pl.justsectors.packets.impl.customevents.StartCustomEventPacket;
import pl.justsectors.packets.impl.global.GlobalEventPacket;
import pl.justsectors.packets.impl.global.GlobalGiftKeysPacket;
import pl.justsectors.packets.impl.guild.*;
import pl.justsectors.packets.impl.halloween.AddPumpkinPacket;
import pl.justsectors.packets.impl.itemshop.AddSlotReservationPacket;
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

import java.util.HashMap;
import java.util.Map;

public class PacketManager {

    private static final Map<Integer, Class<? extends RedisPacket>> packetsByID = new HashMap<>();
    private static final Map<Class<? extends RedisPacket>, Integer> packetsByClass = new HashMap<>();


    //user 1+
    static {
        registerPacket(1, ChatMessagePacket.class);
        registerPacket(2, AdminChatPacket.class);
        registerPacket(3, AddStatisticPacket.class);
        registerPacket(4, ClearInventoryPacket.class);
        registerPacket(5, UserGiveEventPacket.class);
        registerPacket(6, UserRemoveEventPacket.class);
        registerPacket(7, FlyTogglePacket.class);
        registerPacket(8, JoinSectorPacket.class);
        registerPacket(9, LeftSectorPacket.class);
        registerPacket(10, PlayerSpawnTeleportPacket.class);
        registerPacket(11, BackupCreatePacket.class);
        registerPacket(12, AuctionRegisterPacket.class);
        registerPacket(13, AuctionSoldPacket.class);
        registerPacket(14, WarpRegisterPacket.class);
        registerPacket(15, WarpDeletePacket.class);
        registerPacket(16, UserEatKoxPacket.class);
        registerPacket(17, UserEatRefPacket.class);
        registerPacket(18, UserThrowPearl.class);
        registerPacket(19, TellMessagePacket.class);
        registerPacket(20, RemoveStatisticPacket.class);
        registerPacket(21, SetStatisticPacket.class);
        registerPacket(22, ChatControlMessagePacket.class);
        registerPacket(23, PlayerToPlayerTeleportPacket.class);
        registerPacket(24, PlayerTeleportPacket.class);
        registerPacket(25, UserRegisterPacket.class);
        registerPacket(26, TreasureCreatePacket.class);
        registerPacket(27, TreasureDeletePacket.class);
        registerPacket(28, UserKickPacket.class);
        registerPacket(29, PayTransactionPacket.class);
        registerPacket(30, RankingResetPacket.class);
        registerPacket(31, PayTransactionPacket.class);
        registerPacket(32, ProtectionDisablePacket.class);
        registerPacket(33, ItemShopPacket.class);

        registerPacket(34, StorageAddMemberPacket.class);
        registerPacket(35, StorageAddMemberPacket.class);
        registerPacket(36, StorageBuyPacket.class);
        registerPacket(37, StorageLeftPacket.class);
        registerPacket(38, StorageRemoveMemberPacket.class);
        registerPacket(39, StorageCreatePacket.class);
        registerPacket(40, StorageAddExpireTimePacket.class);

        //guild 100+
        registerPacket(100, GuildCreatePacket.class);
        registerPacket(101, GuildExpireExtendPacket.class);
        registerPacket(102, GuildExpireExtendPacket.class);
        registerPacket(103, GuildInvitePacket.class);
        registerPacket(104, GuildJoinPacket.class);
        registerPacket(105, GuildKickPacket.class);
        registerPacket(106, GuildLeavePacket.class);
        registerPacket(107, GuildPlayerLimitPacket.class);
        registerPacket(108, GuildRemovePacket.class);
        registerPacket(109, GuildSetOwnerPacket.class);
        registerPacket(110, GuildSetPreOwnerPacket.class);
        registerPacket(111, GuildSetPvPPacket.class);
        registerPacket(112, GuildWarStartPacket.class);
        registerPacket(113, GuildWarRemovePacket.class);
        registerPacket(114, GuildRemoveSoulsPacket.class);
        registerPacket(115,GuildEffectPacket.class);
        registerPacket(116, GuildAddSoulsPacket.class);
        registerPacket(117, GuildSetSoulsPacket.class);


        //globals 200+
        registerPacket(201, GlobalEventPacket.class);
        registerPacket(202, GlobalGiftKeysPacket.class);
        registerPacket(203, ChatControlGlobalMessagePacket.class);

        //customevents 300+

        registerPacket(301, AddStatCustomEventPacket.class);
        registerPacket(302, StartCustomEventPacket.class);
        registerPacket(303, EndCustomEventPacket.class);
        registerPacket(304, AddPumpkinPacket.class);
        //others 500+
        registerPacket(501, SendTitlePacket.class);
        registerPacket(502, HelpOpMessagePacket.class);
        registerPacket(503, StopServerPacket.class);
        registerPacket(504, SectorEnablePacket.class);
        registerPacket(505, SectorDisablePacket.class);
        registerPacket(506, AddPermissionPacket.class);
        registerPacket(507, RemovePermissionPacket.class);
        registerPacket(508, SetPrefixPacket.class);
        registerPacket(509, SetSuffixPacket.class);
        registerPacket(510, CreateRankPacket.class);
        registerPacket(511, SectorStatusPacket.class);
        registerPacket(512, ChatTogglePacket.class);
        registerPacket(513, ChatVipTogglePacket.class);



        //mine 600+
        registerPacket(600, MineRegisterPacket.class);
        registerPacket(601, MineBuyPacket.class);

        //configs 900+
        registerPacket(901, SetConfigBooleanPacket.class);
        registerPacket(902, SetConfigIntPacket.class);
        registerPacket(903, SetConfigStringPacket.class);
        registerPacket(904, SetConfigDoublePacket.class);
        registerPacket(905, SetConfigLongPacket.class);
        registerPacket(906, LoadConfigPacket.class);


        //1500+ from spigot to bungee
        registerPacket(1500, AddSlotReservationPacket.class);
        registerPacket(1501, BanAddPacket.class);

    }

    public static void registerPacket(int id, Class<? extends RedisPacket> packetClass) {
        packetsByID.putIfAbsent(id, packetClass);
        packetsByClass.putIfAbsent(packetClass, id);
    }

    public static Class<? extends RedisPacket> getPacketClass(final int id){
        return packetsByID.get(id);
    }

    public static RedisPacket getPacket(int id) {
        Class<? extends RedisPacket> packet = packetsByID.get(id);
        if(packet == null){
            return null;
        }
        try {
            return packetsByID.get(id).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getPacketID(Class<? extends RedisPacket> clz) {
        return packetsByClass.get(clz);
    }


}
