package pl.justsectors.nbt.extenders;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import pl.blackwater.core.data.User;

public interface NBTExtender {

    default void readNBT(final User u, final NBTTagCompound nbtTagCompound){

    }

    void saveToNBT(final User u, final NBTTagCompound nbtTagCompound);

}

