package pl.justsectors.packets.handler;

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

public interface PacketHandler {

    //CHATS
    void handle(final ChatMessagePacket packet);

    void handle(final AdminChatPacket packet);

    void handle(final TellMessagePacket tellMessagePacket);

    void handle(final ChatControlMessagePacket chatControlChatMessagePacket);

    void handle(final ChatControlGlobalMessagePacket chatControlGlobalMessagePacket);

    void handle(final ChatTogglePacket packet);

    void handle(final ChatVipTogglePacket packet);


   	//GUILDS
    void handle(final GuildCreatePacket guildCreatePacket);

    void handle(final GuildRemovePacket guildRemovePacket);

    void handle(final GuildExpireExtendPacket expireExtendPacket);

    void handle(final GuildInvitePacket guildInvitePacket);

    void handle(final GuildJoinPacket guildJoinPacket);

    void handle(final GuildKickPacket guildKickPacket);

    void handle(final GuildLeavePacket guildLeavePacket);

    void handle(final GuildSetOwnerPacket guildSetOwnerPacket);

    void handle(final GuildPlayerLimitPacket playerLimitPacket);

    void handle(final GuildSetPreOwnerPacket guildSetPreOwnerPacket);

    void handle(final GuildSetPvPPacket guildSetPvPPacket);

    void handle(final GuildWarStartPacket guildWarPacket);

    void handle(final GuildRemoveSoulsPacket guildRemoveSoulsPacket);

    void handle(final GuildEffectPacket packet);

    //USER
    void handle(final AddStatisticPacket packet);
    void handle(final ClearInventoryPacket packet);
    void handle(final UserGiveEventPacket packet);
    void handle(final UserRemoveEventPacket packet);
    void handle(final FlyTogglePacket packet);
    void handle(final JoinSectorPacket packet);
    void handle(final LeftSectorPacket leftSectorPacket);
    void handle(final UserChangeSectorPacket userChangeSectorPacket);
    void handle(final UserEatKoxPacket userEatKoxPacket);
    void handle(final UserEatRefPacket userEatRefPacket);
    void handle(final UserThrowPearl userThrowPearl);
    void handle(final SetStatisticPacket setStatisticPacket);
    void handle(final RemoveStatisticPacket removeStatisticPacket);
    void handle(final UserKickPacket userKickPacket);
    void handle(final UserRegisterPacket userRegisterPacket);
    void handle(final ProtectionDisablePacket packet);

    //OTHERS
    void handle(final SendTitlePacket packet);
    void handle(final HelpOpMessagePacket packet);
    void handle(final StopServerPacket packet);
    void handle(final SectorEnablePacket sectorEnablePacket);
    void handle(final SectorDisablePacket sectorDisablePacket);

    //GLOBAL

    void handle(final GlobalEventPacket packet);
    void handle(final GlobalGiftKeysPacket packet);


    //TELEPORT
    void handle(final PlayerTeleportPacket playerTeleportPacket);

    void handle(final PlayerSpawnTeleportPacket playerSpawnTeleportPacket);

    void handle(final PlayerToPlayerTeleportPacket playerToPlayerTeleportPacket);

    //BACKUSP
    void handle(final BackupCreatePacket backupCreatePacket);

    //AUCTIONS
    void handle(final AuctionRegisterPacket auctionRegisterPacket);
    void handle(final AuctionSoldPacket auctionSoldPacket);

    //warps
    void handle(final WarpRegisterPacket warpRegisterPacket);
    void handle(final WarpDeletePacket warpDeletePacket);

    //ranks
    void handle(final AddPermissionPacket addPermissionPacket);
    void handle(final RemovePermissionPacket removePermissionPacket);
    void handle(final SetSuffixPacket setSuffixPacket);
    void handle(final SetPrefixPacket setPrefixPacket);
    void handle(final CreateRankPacket createRankPacket);
    //treasureseqweqw
    void handle(final TreasureCreatePacket treasureCreatePacket);
    void handle(final TreasureDeletePacket treasureDeletePacket);

    //mines
    void handle(final MineBuyPacket mineBuyPacket);
    void handle(final MineRegisterPacket mineRegisterPacket);

    //sectors
    void handle(final SectorStatusPacket sectorStatusPacket);

    //money
    void handle(final PayTransactionPacket packet);

    //other
    void handle(final RankingResetPacket packet);

    //configs
    void handle(SetConfigStringPacket packet);
    void handle(SetConfigIntPacket packet);
    void handle(SetConfigBooleanPacket packet);
    void handle(SetConfigDoublePacket packet);
    void handle(LoadConfigPacket packet);
    void handle(SetConfigLongPacket packet);
    //itemshop

    void handle(ItemShopPacket packet);

    //storages

    void handle(StorageBuyPacket packet);
    void handle(StorageAddExpireTimePacket packet);
    void handle(StorageRemoveMemberPacket packet);
    void handle(StorageAddMemberPacket packet);
    void handle(StorageLeftPacket packet);
    void handle(StorageCreatePacket packet);

    //customevents

    void handle(StartCustomEventPacket packet);

    void handle(AddStatCustomEventPacket packet);

    void handle(EndCustomEventPacket packet);

    void handle(GuildAddSoulsPacket packet);

    void handle(GuildSetSoulsPacket packet);

    void handle(final AddPumpkinPacket addPumpkinPacket);

}
