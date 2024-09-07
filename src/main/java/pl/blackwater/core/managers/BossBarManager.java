package pl.blackwater.core.managers;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import org.bukkit.ChatColor;
import pl.supereasy.bpaddons.bossbar.BarColor;
import pl.supereasy.bpaddons.bossbar.BarStyle;
import pl.supereasy.bpaddons.bossbar.BossBarBuilder;

import java.util.UUID;

public class BossBarManager {

    public final String TAG = "BP|UpdateBossInfo";

    private final UUID joinBar = UUID.randomUUID();

    public PacketDataSerializer joinBarPacket;

    public BossBarManager(){
        joinBarPacket = buildJoinBarPacket();
    }

    public PacketDataSerializer buildJoinBarPacket(){
        return joinBarPacket = BossBarBuilder
                .add(joinBar)
                .style(BarStyle.SEGMENTED_6)
                .progress(1)
                .color(BarColor.PURPLE)
                .title(TextComponent.fromLegacyText(ChatColor.LIGHT_PURPLE + ""  + ChatColor.BLUE + "8 MAJ NOWA EDYCJA 16:00"))
                .buildPacket().serialize();
    }
}
