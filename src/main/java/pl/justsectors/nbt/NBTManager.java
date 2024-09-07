package pl.justsectors.nbt;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MojangsonParseException;
import net.minecraft.server.v1_8_R3.MojangsonParser;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import pl.blackwater.core.data.User;
import pl.justsectors.nbt.extenders.NBTExtender;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NBTManager {

    private static final Set<NBTExtender> nbtExtenders = new HashSet<>(Arrays.asList());


    public static void applyToPlayer(User u, NBTTagCompound nbtTagCompound) {
        final EntityPlayer entityPlayer = u.asEntityPlayer();
        if(entityPlayer == null){
            return;
        }
        try {
            final NBTTagCompound applyCompund = MojangsonParser.parse(nbtTagCompound.toString());
            for (NBTExtender nbtExtender : nbtExtenders) {
                nbtExtender.readNBT(u, applyCompund);
            }
            entityPlayer.a(applyCompund);
        } catch (MojangsonParseException e) {
            e.printStackTrace();
        }
    }

    public static NBTTagCompound createNBTBasedOnUser(User u) {
        final EntityPlayer entityPlayer = u.asEntityPlayer();
        if(entityPlayer == null){
            return null;
        }
        final NBTTagCompound nbtTagCompound = new NBTTagCompound();
        entityPlayer.b(nbtTagCompound);
        for (NBTExtender nbtExtender : nbtExtenders) {
            nbtExtender.saveToNBT(u, nbtTagCompound);
        }
        return nbtTagCompound;
    }

    public static NBTTagCompound asNBT(String nbtString) {
        try {
            return MojangsonParser.parse(nbtString);
        } catch (MojangsonParseException e) {
            e.printStackTrace();
        }
        return null;
    }



}
