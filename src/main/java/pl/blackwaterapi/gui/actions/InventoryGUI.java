package pl.blackwaterapi.gui.actions;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import pl.blackwaterapi.gui.listeners.InventoryListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

@Data
public class InventoryGUI
  implements Listener
{
  private Inventory inventory;
  private HashMap<Integer, IAction> actions;
  private HashMap<UUID, Long> times = new HashMap<>();
  private Plugin plugin;
  
public InventoryGUI(Plugin plugin, String title, int rows)
  {
    this.inventory = Bukkit.createInventory(null, rows * 9, ChatColor.translateAlternateColorCodes('&', title));
    this.actions = new HashMap<>();
    this.plugin = plugin;
    InventoryListener.getInventories().put(this.inventory, this);
  }
  public InventoryGUI clone(){
    InventoryGUI inventoryGUI = new InventoryGUI(plugin,inventory.getTitle(),inventory.getSize()/9);
    int i = 0;
    for(ItemStack content : inventory.getContents()){
      inventoryGUI.setItem(i,content,actions.get(i));
      i++;
    }
    return inventoryGUI;
  }

  public void addItem(ItemStack is, IAction clickAction) {
    for (int i = 0; i < this.inventory.getSize(); i++) {
      if (this.inventory.getItem(i) == null) {
        this.inventory.setItem(i, is);
        this.actions.put(i, clickAction);
        return;
      }
    }
  }


  public void setItem(int slot, ItemStack itemStack, IAction clickAction)
  {
    slot = slot > this.inventory.getSize() ? slot % this.inventory.getSize() : slot;
    this.inventory.setItem(slot, itemStack);
    this.actions.put(slot, clickAction);
  }
  
  public InventoryGUI setAllItems(ItemStack[] items)
  {
    for (int i = 0; i < this.inventory.getSize(); i++) {
      this.inventory.setItem(i, items[i]);
    }
    return this;
  }
  
  public InventoryGUI setAction(int slot, IAction action)
  {
    if (action != null) {
      IAction put = this.actions.put(slot, action);
    } else {
      this.actions.remove(slot);
    }
    return this;
  }
  
  public InventoryGUI setOpenAction(IAction action)
  {
    if (action != null) {
      this.actions.put(-1, action);
    } else {
      this.actions.remove(-1);
    }
    return this;
  }
  
  public ItemStack getItem(int i)
  {
    return this.inventory.getItem(i);
  }
  
  public InventoryGUI setCloseAction(IAction action)
  {
    if (action != null) {
      IAction put = this.actions.put(-2, action);
    } else {
      this.actions.remove(-2);
    }
    return this;
  }
  
  public void openInventory(Player player)
  {
    player.openInventory(this.inventory);
  }
  
  public InventoryGUI openInventory(Player[] players)
  {
    for (Player player : players) {
      openInventory(player);
    }
    return this;
  }
  
  public InventoryGUI openInventory(Collection<? extends Player> players)
  {
    for (Player player : players) {
      openInventory(player);
    }
    return this;
  }
  
  public Inventory get()
  {
    return this.inventory;
  }
  /*
  @EventHandler
  public void onInventoryClick(InventoryClickEvent e)
  {
    if (e.getInventory().equals(this.inventory))
    {
      e.setCancelled(true);
      IAction action = this.actions.get(e.getRawSlot());
      if (action != null) {
        final Player player = (Player) e.getWhoClicked();
        if(!player.hasPermission("core.admin") && times.get(player.getUniqueId()) != null && times.get(player.getUniqueId()) > System.currentTimeMillis()){
          Util.sendMsg(player, Util.fixColor(Util.replaceString("&4Blad: &cNie mozesz tak czesto klikac !")));
        }else {
          times.put(player.getUniqueId(),System.currentTimeMillis() + 500L);
          action.execute(player, e.getInventory(), e.getRawSlot(), e.getInventory().getItem(e.getRawSlot()));
        }
      } else {
        e.getWhoClicked().openInventory(e.getInventory());
        //
      }
    }
  }
  */
 /* @EventHandler
  public void onInventoryMoveItemEvent(InventoryMoveItemEvent e)
  {
    if (e.getDestination().equals(this.inventory)) {
      e.setCancelled(true);
    }
    if (e.getInitiator().equals(this.inventory)) {
      e.setCancelled(true);
    }
    if (this.inventory.equals(e.getSource())) {
      e.setCancelled(true);
    }
  }

  */
  
  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent e)
  {
    if (e.getInventory().equals(this.inventory))
    {
      IAction action = this.actions.get(-1);
      if (action != null) {
        action.execute((Player)e.getPlayer(), e.getInventory(), -1, null);
      }
    }
  }
  
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent e)
  {
    if (e.getInventory().equals(this.inventory))
    {
      IAction action = this.actions.get(-2);
      if (action != null) {
        action.execute((Player)e.getPlayer(), e.getInventory(), -1, null);
      }
    }
  }
  
  @EventHandler
  public void onInventoryInteractEvent(InventoryInteractEvent e)
  {
    if (this.inventory.equals(e.getInventory())) {
      e.setCancelled(true);
    }
  }
  
  @EventHandler
  public void onInventoryDragEvent(InventoryDragEvent e)
    {
      if (e.getInventory().equals(this.inventory)) {
        e.setCancelled(true);
      }
  }
}