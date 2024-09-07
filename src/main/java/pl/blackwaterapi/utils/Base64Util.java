package pl.blackwaterapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64Util {

    public static String[] playerInventoryToBase64(PlayerInventory paramPlayerInventory)
    {
        String str1 = toBase64(paramPlayerInventory);
        String str2 = itemStackArrayToBase64(paramPlayerInventory.getArmorContents());

        return new String[] { str1, str2 };
    }

    public static String itemStackArrayToBase64(ItemStack[] paramArrayOfItemStack)
    {
        try
        {
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream localBukkitObjectOutputStream = new BukkitObjectOutputStream(localByteArrayOutputStream);

            localBukkitObjectOutputStream.writeInt(paramArrayOfItemStack.length);
            for (ItemStack localItemStack : paramArrayOfItemStack) {
                localBukkitObjectOutputStream.writeObject(localItemStack);
            }
            localBukkitObjectOutputStream.close();
            return Base64Coder.encodeLines(localByteArrayOutputStream.toByteArray());
        }
        catch (Exception localException)
        {
            throw new IllegalStateException("Unable to save item stacks.", localException);
        }
    }

    public static String toBase64(Inventory paramInventory)
    {
        try
        {
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream localBukkitObjectOutputStream = new BukkitObjectOutputStream(localByteArrayOutputStream);

            localBukkitObjectOutputStream.writeInt(paramInventory.getSize());
            for (int i = 0; i < paramInventory.getSize(); i++) {
                localBukkitObjectOutputStream.writeObject(paramInventory.getItem(i));
            }
            localBukkitObjectOutputStream.close();
            return Base64Coder.encodeLines(localByteArrayOutputStream.toByteArray());
        }
        catch (Exception localException)
        {
            throw new IllegalStateException("Unable to save item stacks.", localException);
        }
    }

    public static Inventory inventoryFromBase64(String paramString) throws IOException {
        try
        {
            ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(paramString));
            BukkitObjectInputStream localBukkitObjectInputStream = new BukkitObjectInputStream(localByteArrayInputStream);
            Inventory localInventory = Bukkit.getServer().createInventory(null, localBukkitObjectInputStream.readInt());
            for (int i = 0; i < localInventory.getSize(); i++) {
                localInventory.setItem(i, (ItemStack)localBukkitObjectInputStream.readObject());
            }
            localBukkitObjectInputStream.close();
            return localInventory;
        }
        catch (ClassNotFoundException localClassNotFoundException)
        {
            throw new IOException("Unable to decode class type.", localClassNotFoundException);
        }
    }

    public static ItemStack[] itemStackArrayFromBase64(String paramString) throws IOException {
        try
        {
            ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(paramString));
            BukkitObjectInputStream localBukkitObjectInputStream = new BukkitObjectInputStream(localByteArrayInputStream);
            ItemStack[] arrayOfItemStack = new ItemStack[localBukkitObjectInputStream.readInt()];
            for (int i = 0; i < arrayOfItemStack.length; i++) {
                arrayOfItemStack[i] = ((ItemStack)localBukkitObjectInputStream.readObject());
            }
            localBukkitObjectInputStream.close();
            return arrayOfItemStack;
        }
        catch (ClassNotFoundException | IOException localClassNotFoundException)
        {
            throw new IOException("Unable to decode class type.", localClassNotFoundException);
        }
    }
}
