package pl.blackwater.core.utils;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.CustomEnchantManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.gui.actions.IAction;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.Util;

import java.util.Collections;

public class EnchantAction
  implements IAction
{
  private Enchantment enchant;
  private int levelEnchant;
  private int levelCost;
  private int needBooks;
  private int readyBooks;
  private int money;
  private int lapisCost;

  public EnchantAction(Enchantment e, int levelEnchant, int levelCost, int needBooks, int readyBooks)
  {
    this.enchant = e;
    this.levelEnchant = levelEnchant;
    this.levelCost = levelCost;
    this.needBooks = needBooks;
    this.readyBooks = readyBooks;
    this.money = levelCost * 50;
    this.lapisCost = levelCost;
  }

  public void execute(Player player, Inventory inventory, int slot, ItemStack itemStack)
  {
    ItemStack item = player.getItemInHand();
    if (item != null)
    {
      if (this.readyBooks >= this.needBooks)
      {
        final User user = UserManager.getUser(player);
        if ((player.getLevel() >= this.levelCost) && (user.getMoney() >= this.money)  && (ItemUtil.checkAndRemove(Collections.singletonList(new ItemStack(Material.INK_SACK, lapisCost, (short) 4)),player))|| (player.getGameMode() == GameMode.CREATIVE))
        {
          if (CustomEnchantManager.isAllowedEnchant(item, this.enchant))
          {
            if (item.containsEnchantment(this.enchant))
            {
              if (item.getEnchantmentLevel(this.enchant) >= this.levelEnchant)
              {
                player.sendMessage(Util.fixColor("&cTen przedmiot posiada juz to zaklecie na wyzszym badz rownym poziomie!"));
              }
              else
              {
                item.addUnsafeEnchantment(this.enchant, this.levelEnchant);
                player.sendMessage(Util.fixColor("&aGratulacje &7zaczarowales przedmiot !"));
                if (player.getGameMode() != GameMode.CREATIVE) {
                  player.setLevel(player.getLevel() - this.levelCost);
                  user.removeMoney(this.money);
                } else {
                  player.sendMessage(Util.fixColor("&3poziom EXP&7 zostal pobrany z trybu gry: &3KREATYWNY"));
                }
              }
            }
            else
            {
              item.addUnsafeEnchantment(this.enchant, this.levelEnchant);
              player.sendMessage(Util.fixColor("&aGratulacje &7zaczarowales przedmiot !"));
              if (player.getGameMode() != GameMode.CREATIVE) {
                player.setLevel(player.getLevel() - this.levelCost);
                user.removeMoney(this.money);
              } else {
                player.sendMessage(Util.fixColor("&3poziom EXP&7 zostal pobrany z trybu gry: &3KREATYWNY"));
              }
            }
          }
          else {
            player.sendMessage(Util.fixColor("&cNie mozesz juz dodac tego zaklecia !"));
          }
        }
        else {
          player.sendMessage(Util.fixColor("&7Musisz miec &3" + this.levelCost + " poziom EXP&7, " + lapisCost + " &3lapisu &7oraz &3" + money + " MONET &7aby zaklac ten przedmiot."));
        }
      }
      else {
        player.sendMessage(Util.fixColor("&cNie wystarczajaca ilosc biblioteczek otacza &5stol do zaklinania&7. &7Aby to zrobic potrzebujesz jeszcze: &3" + (this.needBooks - this.readyBooks) + " &7biblioteczek wokol stolu"));
      }
    }
    else {
      player.sendMessage(Util.fixColor("&cW rece musisz miec przedmiot, ktory chesz zaklac !"));
    }
    player.closeInventory();
  }
}