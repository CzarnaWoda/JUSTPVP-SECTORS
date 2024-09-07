package pl.blackwaterapi.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemUtil
{
    private static HashMap<Material, String> polishNames;
    public static List<ItemStack> getItems(String string, int modifier)
    {
        //id:data:amount;id:data:amount;
      ArrayList<ItemStack> items = new ArrayList<>();
      String[] array = string.split(";");
      for(String s : array){
          String[] array2 = s.split("-");
          for (int i = 0; i < array2.length; i ++) {
              array2[i] = array2[i];
          }
          final int id = Integer.parseInt(array2[0]);
          final short data = Short.parseShort(array2[1]);
          final int amount = Integer.parseInt(array2[2]);
          final ItemStack itemStack = new ItemStack(Material.getMaterial(id),amount*modifier,data);
          items.add(itemStack);
      }
      return items;
    }
    
    public static boolean checkItems(List<ItemStack> items, Player p)
    {
      for (ItemStack item : items) {
        if (!p.getInventory().containsAtLeast(item, item.getAmount())) {
          return false;
        }
      }
      return true;
    }
    
    
    public static String getItems(List<ItemStack> items) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < items.size(); i ++){
            ItemStack item = items.get(i);
            if(i+1 == items.size()){
                sb.append(getPolishMaterial(item.getType())).append(" §8(§c").append(item.getAmount()).append(" ").append("szt§8)");
            }else
                sb.append(getPolishMaterial(item.getType())).append(" §8(§c").append(item.getAmount()).append(" ").append("szt§8)").append("§7,§4 ");
        }
        return sb.toString();
    }
    
    public static void removeItems(List<ItemStack> items, Player player) {
        Inventory inv = player.getInventory();
        List<ItemStack> removes = new ArrayList<>();
        for (ItemStack item : items) {
            if (inv.containsAtLeast(item, item.getAmount())) {
                removes.add(item);
            }
        }
        if (removes.size() == items.size()) {
            for (ItemStack item : items) {
                for (ItemStack remove : removes) {
                    if (item.getType().equals(remove.getType()) && item.getData().equals(remove.getData())) {
                        inv.removeItem(item);
                    }
                }
            }
        }
        removes.clear();
    }
    
    public static boolean checkAndRemove(List<ItemStack> items, Player player)
    {
      boolean has = checkItems(items, player);
      if (has) {
        removeItems(items, player);
      }
      return has;
    }
    
    public static String getPolishMaterial(Material material) {
        String name = ItemUtil.polishNames.get(material);
        if (name == null || name.equals("")) {
            name = material.name().toLowerCase().replace("_", " ");
        }
        return name;
    }
    
    public static ItemStack getPlayerHead(String name) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta meta = (SkullMeta)itemStack.getItemMeta();
        meta.setOwner(name);
        meta.setDisplayName(Util.fixColor("&6" + name));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
    public static void giveItems(List<ItemStack> items,Player p){
    	ItemStack[] itemss = items.toArray(new ItemStack[0]);
    	HashMap<Integer, ItemStack> notStored = p.getInventory().addItem(itemss);
    	for(ItemStack i : notStored.values()) {
    		p.getWorld().dropItemNaturally(p.getLocation(), i);
    	}
    }
    
    static {
        (polishNames = new HashMap()).put(Material.AIR, " powietrze");
        polishNames.put(Material.STONE, " kamien");
        polishNames.put(Material.GRASS, " trawa");
        polishNames.put(Material.DIRT, " ziemia");
        polishNames.put(Material.COBBLESTONE, " bruk");
        polishNames.put(Material.WOOD, " deski");
        polishNames.put(Material.SAPLING, " sadzonka");
        polishNames.put(Material.BEDROCK, " bedrock");
        polishNames.put(Material.WATER, " woda");
        polishNames.put(Material.STATIONARY_WATER, " woda");
        polishNames.put(Material.LAVA, " lawa");
        polishNames.put(Material.STATIONARY_LAVA, " lawa");
        polishNames.put(Material.SAND, " piasek");
        polishNames.put(Material.GRAVEL, " zwir");
        polishNames.put(Material.GOLD_ORE, " ruda zlota");
        polishNames.put(Material.IRON_ORE, " ruda zelaza");
        polishNames.put(Material.COAL_ORE, " ruda wegla");
        polishNames.put(Material.LOG, " drewno");
        polishNames.put(Material.LEAVES, " liscie");
        polishNames.put(Material.SPONGE, " gabka");
        polishNames.put(Material.GLASS, " szklo");
        polishNames.put(Material.LAPIS_ORE, " ruda lipisu");
        polishNames.put(Material.LAPIS_BLOCK, " blok lapisu");
        polishNames.put(Material.DISPENSER, " dozownik");
        polishNames.put(Material.SANDSTONE, " pisakowiec");
        polishNames.put(Material.NOTE_BLOCK, " note block");
        polishNames.put(Material.BED_BLOCK, " lozko");
        polishNames.put(Material.POWERED_RAIL, " zasilane tory");
        polishNames.put(Material.DETECTOR_RAIL, " tory z czujnikiem");
        polishNames.put(Material.PISTON_STICKY_BASE, " tlok");
        polishNames.put(Material.WEB, " nic");
        polishNames.put(Material.LONG_GRASS, " trawa");
        polishNames.put(Material.DEAD_BUSH, " uschneity krzak");
        polishNames.put(Material.PISTON_BASE, " tlok");
        polishNames.put(Material.PISTON_EXTENSION, " tlok");
        polishNames.put(Material.WOOL, " welna");
        polishNames.put(Material.PISTON_MOVING_PIECE, " tlok");
        polishNames.put(Material.YELLOW_FLOWER, " tulipan");
        polishNames.put(Material.RED_ROSE, " roza");
        polishNames.put(Material.BROWN_MUSHROOM, " brazowy grzyb");
        polishNames.put(Material.RED_MUSHROOM, " muchomor");
        polishNames.put(Material.GOLD_BLOCK, " blok zlota");
        polishNames.put(Material.IRON_BLOCK, " blok zelaza");
        polishNames.put(Material.DOUBLE_STEP, " podwojna polplytka");
        polishNames.put(Material.STEP, " polplytka");
        polishNames.put(Material.BRICK, " cegly");
        polishNames.put(Material.TNT, " tnt");
        polishNames.put(Material.BOOKSHELF, " biblioteczka");
        polishNames.put(Material.MOSSY_COBBLESTONE, " zamszony bruk");
        polishNames.put(Material.OBSIDIAN, " obsydian");
        polishNames.put(Material.TORCH, " pochodnia");
        polishNames.put(Material.FIRE, " ogien");
        polishNames.put(Material.MOB_SPAWNER, " mob spawner");
        polishNames.put(Material.WOOD_STAIRS, " drewniane schodki");
        polishNames.put(Material.CHEST, " skrzynia");
        polishNames.put(Material.REDSTONE_WIRE, " redstone");
        polishNames.put(Material.DIAMOND_ORE, " ruda diamentu");
        polishNames.put(Material.DIAMOND_BLOCK, " blok diamentu");
        polishNames.put(Material.WORKBENCH, " stol rzemieslniczy");
        polishNames.put(Material.CROPS, " nasionka");
        polishNames.put(Material.SOIL, " nasionka");
        polishNames.put(Material.FURNACE, " piecyk");
        polishNames.put(Material.BURNING_FURNACE, " piecyk");
        polishNames.put(Material.SIGN_POST, " tabliczka");
        polishNames.put(Material.WOODEN_DOOR, " drewniane drzwi");
        polishNames.put(Material.LADDER, " drabinka");
        polishNames.put(Material.RAILS, " tory");
        polishNames.put(Material.COBBLESTONE_STAIRS, " brukowe schody");
        polishNames.put(Material.WALL_SIGN, " tabliczka");
        polishNames.put(Material.LEVER, " dzwignia");
        polishNames.put(Material.STONE_PLATE, " plytka naciskowa");
        polishNames.put(Material.IRON_DOOR_BLOCK, " zelazne drzwi");
        polishNames.put(Material.WOOD_PLATE, " plytka nasickowa");
        polishNames.put(Material.REDSTONE_ORE, " ruda redstone");
        polishNames.put(Material.GLOWING_REDSTONE_ORE, " ruda redstone");
        polishNames.put(Material.REDSTONE_TORCH_OFF, " czerwona pochodnia");
        polishNames.put(Material.REDSTONE_TORCH_ON, " czerwona pochodnia");
        polishNames.put(Material.STONE_BUTTON, " kamienny przycisk");
        polishNames.put(Material.SNOW, " snieg");
        polishNames.put(Material.ICE, " lod");
        polishNames.put(Material.SNOW_BLOCK, " snieg");
        polishNames.put(Material.CACTUS, " kaktus");
        polishNames.put(Material.CLAY, " glina");
        polishNames.put(Material.SUGAR_CANE_BLOCK, " trzcina");
        polishNames.put(Material.JUKEBOX, " szafa grajaca");
        polishNames.put(Material.FENCE, " plotek");
        polishNames.put(Material.PUMPKIN, " dynia");
        polishNames.put(Material.NETHERRACK, " netherrack");
        polishNames.put(Material.SOUL_SAND, " pisaek dusz");
        polishNames.put(Material.GLOWSTONE, " jasnoglaz");
        polishNames.put(Material.PORTAL, " portal");
        polishNames.put(Material.JACK_O_LANTERN, " jack'o'latern");
        polishNames.put(Material.CAKE_BLOCK, " ciasto");
        polishNames.put(Material.DIODE_BLOCK_OFF, " przekaznik");
        polishNames.put(Material.DIODE_BLOCK_ON, " przekaznik");
        polishNames.put(Material.getMaterial(146), " zamknieta skrzynia");
        polishNames.put(Material.STAINED_GLASS, " utwardzone szklo");
        polishNames.put(Material.TRAP_DOOR, " wlaz");
        polishNames.put(Material.MONSTER_EGGS, " jajko potwora");
        polishNames.put(Material.SMOOTH_BRICK, " cegly");
        polishNames.put(Material.HUGE_MUSHROOM_1, " duzy grzyb");
        polishNames.put(Material.HUGE_MUSHROOM_2, " duzy grzyb");
        polishNames.put(Material.IRON_FENCE, " kraty");
        polishNames.put(Material.THIN_GLASS, " szyba");
        polishNames.put(Material.MELON_BLOCK, " arbuz");
        polishNames.put(Material.PUMPKIN_STEM, " dynia");
        polishNames.put(Material.MELON_STEM, " arbuz");
        polishNames.put(Material.VINE, " pnacze");
        polishNames.put(Material.FENCE_GATE, " furtka");
        polishNames.put(Material.BRICK_STAIRS, " ceglane schodki");
        polishNames.put(Material.SMOOTH_STAIRS, " kamienne schodki");
        polishNames.put(Material.MYCEL, " grzybnia");
        polishNames.put(Material.WATER_LILY, " lilia wodna");
        polishNames.put(Material.NETHER_BRICK, " cegly netherowe");
        polishNames.put(Material.NETHER_FENCE, " netherowy plotek");
        polishNames.put(Material.NETHER_BRICK_STAIRS, " netherowe schodki");
        polishNames.put(Material.NETHER_WARTS, " brodawki");
        polishNames.put(Material.ENCHANTMENT_TABLE, " stol do enchantu");
        polishNames.put(Material.BREWING_STAND, " stol alchemiczny");
        polishNames.put(Material.CAULDRON, " kociol");
        polishNames.put(Material.ENDER_PORTAL, " ender portal");
        polishNames.put(Material.ENDER_PORTAL_FRAME, " ender portal");
        polishNames.put(Material.ENDER_STONE, " kamien kresu");
        polishNames.put(Material.DRAGON_EGG, " jajko smoka");
        polishNames.put(Material.REDSTONE_LAMP_OFF, " lampa");
        polishNames.put(Material.REDSTONE_LAMP_ON, " lampa");
        polishNames.put(Material.WOOD_DOUBLE_STEP, " podwojna drewniana polplytka");
        polishNames.put(Material.WOOD_STEP, " drewnania polplytka");
        polishNames.put(Material.COCOA, " kakao");
        polishNames.put(Material.SANDSTONE_STAIRS, " piaskowe schodki");
        polishNames.put(Material.EMERALD_ORE, " ruda szmaragdu");
        polishNames.put(Material.ENDER_CHEST, " skrzynia kresu");
        polishNames.put(Material.TRIPWIRE_HOOK, " potykacz");
        polishNames.put(Material.TRIPWIRE, " potykacz");
        polishNames.put(Material.EMERALD_BLOCK, " blok szmaragdu");
        polishNames.put(Material.SPRUCE_WOOD_STAIRS, " drewniane schodki");
        polishNames.put(Material.BIRCH_WOOD_STAIRS, " drewniane schodki");
        polishNames.put(Material.JUNGLE_WOOD_STAIRS, " drewniane schodki");
        polishNames.put(Material.COMMAND, " blok polecen");
        polishNames.put(Material.BEACON, " magiczna latarnia");
        polishNames.put(Material.COBBLE_WALL, " brukowy plotek");
        polishNames.put(Material.FLOWER_POT, " doniczka");
        polishNames.put(Material.CARROT, " marchewka");
        polishNames.put(Material.POTATO, " ziemniak");
        polishNames.put(Material.WOOD_BUTTON, " drewniany przycisk");
        polishNames.put(Material.SKULL, " glowa");
        polishNames.put(Material.ANVIL, " kowadlo");
        polishNames.put(Material.TRAPPED_CHEST, " skrzynka z pulapka");
        polishNames.put(Material.GOLD_PLATE, " zlota polplytka");
        polishNames.put(Material.IRON_PLATE, " zelaza polplytka");
        polishNames.put(Material.REDSTONE_COMPARATOR_OFF, " komparator");
        polishNames.put(Material.REDSTONE_COMPARATOR_ON, " komparator");
        polishNames.put(Material.DAYLIGHT_DETECTOR, " detektor swiatla dziennego");
        polishNames.put(Material.REDSTONE_BLOCK, " blok redstone");
        polishNames.put(Material.QUARTZ_ORE, " ruda kwarcu");
        polishNames.put(Material.HOPPER, " lej");
        polishNames.put(Material.QUARTZ_BLOCK, " blok kwarcu");
        polishNames.put(Material.QUARTZ_STAIRS, " kwarcowe schodki");
        polishNames.put(Material.ACTIVATOR_RAIL, " tory aktywacyjne");
        polishNames.put(Material.DROPPER, " podajnik");
        polishNames.put(Material.STAINED_CLAY, " utwardzona glina");
        polishNames.put(Material.STAINED_GLASS_PANE, " utwardzona szyba");
        polishNames.put(Material.LEAVES_2, " liscie");
        polishNames.put(Material.LOG_2, " drewno");
        polishNames.put(Material.ACACIA_STAIRS, " drewniane schodki");
        polishNames.put(Material.DARK_OAK_STAIRS, " drewniane schodki");
        polishNames.put(Material.HAY_BLOCK, " sloma");
        polishNames.put(Material.CARPET, " dywan");
        polishNames.put(Material.HARD_CLAY, " glina");
        polishNames.put(Material.COAL_BLOCK, " blok wegla");
        polishNames.put(Material.PACKED_ICE, " utwardzony lod");
        polishNames.put(Material.DOUBLE_PLANT, " sadzonka");
        polishNames.put(Material.IRON_SPADE, " zelazna lopata");
        polishNames.put(Material.IRON_PICKAXE, " zelazny kilof");
        polishNames.put(Material.IRON_AXE, " zelazna siekiera");
        polishNames.put(Material.FLINT_AND_STEEL, " zapalniczka");
        polishNames.put(Material.APPLE, " jablko");
        polishNames.put(Material.BOW, " luk");
        polishNames.put(Material.ARROW, " strzala");
        polishNames.put(Material.COAL, " wegiel");
        polishNames.put(Material.DIAMOND, " diament");
        polishNames.put(Material.IRON_INGOT, " sztabka zelaza");
        polishNames.put(Material.GOLD_INGOT, " sztabka zlota");
        polishNames.put(Material.IRON_SWORD, " zelazny miecz");
        polishNames.put(Material.WOOD_SWORD, " drewniany miecz");
        polishNames.put(Material.WOOD_SPADE, " drewniana lopata");
        polishNames.put(Material.WOOD_PICKAXE, " drewniany kilof");
        polishNames.put(Material.WOOD_AXE, " drewnania siekiera");
        polishNames.put(Material.STONE_SWORD, " kamienny miecz");
        polishNames.put(Material.STONE_SPADE, " kamienna lopata");
        polishNames.put(Material.STONE_PICKAXE, " kamienny kilof");
        polishNames.put(Material.STONE_AXE, " kamienna siekiera");
        polishNames.put(Material.DIAMOND_SWORD, " diamentowy miecz");
        polishNames.put(Material.DIAMOND_SPADE, " diamentowa lopata");
        polishNames.put(Material.DIAMOND_PICKAXE, " diamentowy kilof");
        polishNames.put(Material.DIAMOND_AXE, " diamentowa siekiera");
        polishNames.put(Material.STICK, " patyk");
        polishNames.put(Material.BOWL, " miseczka");
        polishNames.put(Material.MUSHROOM_SOUP, " zupa grzybowa");
        polishNames.put(Material.GOLD_SWORD, " zloty miecz");
        polishNames.put(Material.GOLD_SPADE, " zlota lopata");
        polishNames.put(Material.GOLD_PICKAXE, " zloty kilof");
        polishNames.put(Material.GOLD_AXE, " zlota siekiera");
        polishNames.put(Material.STRING, " nitka");
        polishNames.put(Material.FEATHER, " pioro");
        polishNames.put(Material.SULPHUR, " proch strzelniczy");
        polishNames.put(Material.WOOD_HOE, " drewniana motyka");
        polishNames.put(Material.STONE_HOE, " kamienna motyka");
        polishNames.put(Material.IRON_HOE, " zelazna motyka");
        polishNames.put(Material.DIAMOND_HOE, " diemtnowa motyka");
        polishNames.put(Material.GOLD_HOE, " zlota motyka");
        polishNames.put(Material.SEEDS, " nasionka");
        polishNames.put(Material.WHEAT, " pszenica");
        polishNames.put(Material.BREAD, " chleb");
        polishNames.put(Material.LEATHER_HELMET, " skorzany helm");
        polishNames.put(Material.LEATHER_CHESTPLATE, " skorzana klata");
        polishNames.put(Material.LEATHER_LEGGINGS, " skorzane spodnie");
        polishNames.put(Material.LEATHER_BOOTS, " skorzane buty");
        polishNames.put(Material.CHAINMAIL_HELMET, " helm z kolcza");
        polishNames.put(Material.CHAINMAIL_CHESTPLATE, " klata z kolcza");
        polishNames.put(Material.CHAINMAIL_LEGGINGS, " spodnie z kolcza");
        polishNames.put(Material.CHAINMAIL_BOOTS, " buty z kolcza");
        polishNames.put(Material.IRON_HELMET, " zelazny helm");
        polishNames.put(Material.IRON_CHESTPLATE, " zelazna klata");
        polishNames.put(Material.IRON_LEGGINGS, " zelazne spodnie");
        polishNames.put(Material.IRON_BOOTS, " zelazne buty");
        polishNames.put(Material.DIAMOND_HELMET, " diamentowy helm");
        polishNames.put(Material.DIAMOND_CHESTPLATE, " diamentowa klata");
        polishNames.put(Material.DIAMOND_LEGGINGS, " diamentowe spodnie");
        polishNames.put(Material.DIAMOND_BOOTS, " diamentowe buty");
        polishNames.put(Material.GOLD_HELMET, " zloty helm");
        polishNames.put(Material.GOLD_CHESTPLATE, " zlota klata");
        polishNames.put(Material.GOLD_LEGGINGS, " zlote spodnie");
        polishNames.put(Material.GOLD_BOOTS, " zlote buty");
        polishNames.put(Material.FLINT, " krzemien");
        polishNames.put(Material.PORK, " schab");
        polishNames.put(Material.GRILLED_PORK, " pieczony schab");
        polishNames.put(Material.PAINTING, " obraz");
        polishNames.put(Material.GOLDEN_APPLE, " zlote jablko");
        polishNames.put(Material.SIGN, " znak");
        polishNames.put(Material.WOOD_DOOR, " drewniane drzwi");
        polishNames.put(Material.BUCKET, " wiaderko");
        polishNames.put(Material.WATER_BUCKET, " wiaderko wody");
        polishNames.put(Material.LAVA_BUCKET, " wiaderko lawy");
        polishNames.put(Material.MINECART, " wagonik");
        polishNames.put(Material.SADDLE, " siodlo");
        polishNames.put(Material.IRON_DOOR, " zelazne drzwi");
        polishNames.put(Material.REDSTONE, " czerwony proszek");
        polishNames.put(Material.SNOW_BALL, " sniezka");
        polishNames.put(Material.BOAT, " lodka");
        polishNames.put(Material.LEATHER, " skora");
        polishNames.put(Material.MILK_BUCKET, " wiaderko mleka");
        polishNames.put(Material.CLAY_BRICK, " cegly");
        polishNames.put(Material.CLAY_BALL, " kulka gliny");
        polishNames.put(Material.SUGAR_CANE, " trzcina cukrowa");
        polishNames.put(Material.PAPER, " papier");
        polishNames.put(Material.BOOK, " ksiazka");
        polishNames.put(Material.SLIME_BALL, " kulka szlamu");
        polishNames.put(Material.STORAGE_MINECART, " wagonik");
        polishNames.put(Material.POWERED_MINECART, " wagonik");
        polishNames.put(Material.EGG, " jajko");
        polishNames.put(Material.COMPASS, " kompas");
        polishNames.put(Material.FISHING_ROD, " wedka");
        polishNames.put(Material.WATCH, " zegar");
        polishNames.put(Material.GLOWSTONE_DUST, " jasnopyl");
        polishNames.put(Material.RAW_FISH, " ryba");
        polishNames.put(Material.COOKED_FISH, " pieczona ryba");
        polishNames.put(Material.INK_SACK, " czarny barwnik");
        polishNames.put(Material.BONE, " kosc");
        polishNames.put(Material.SUGAR, " cukier");
        polishNames.put(Material.CAKE, " ciasto");
        polishNames.put(Material.BED, " lozko");
        polishNames.put(Material.DIODE, " przekaznik");
        polishNames.put(Material.COOKIE, " ciastko");
        polishNames.put(Material.MAP, " mapa");
        polishNames.put(Material.SHEARS, " nozyce");
        polishNames.put(Material.MELON, " arbuz");
        polishNames.put(Material.PUMPKIN_SEEDS, " nasiono dyni");
        polishNames.put(Material.MELON_SEEDS, " nasiono melona");
        polishNames.put(Material.RAW_BEEF, " stek");
        polishNames.put(Material.COOKED_BEEF, " pieczony stek");
        polishNames.put(Material.RAW_CHICKEN, " kurczak");
        polishNames.put(Material.COOKED_CHICKEN, " upieczony kurczak");
        polishNames.put(Material.ROTTEN_FLESH, " zgnile mieso");
        polishNames.put(Material.ENDER_PEARL, " perla endermana");
        polishNames.put(Material.BLAZE_ROD, " palka blaza");
        polishNames.put(Material.GHAST_TEAR, " lza gasta");
        polishNames.put(Material.GOLD_NUGGET, " zloty samorodek");
        polishNames.put(Material.NETHER_STALK, " brodawka netherowa");
        polishNames.put(Material.POTION, " mikstura");
        polishNames.put(Material.GLASS_BOTTLE, " szklana butelka");
        polishNames.put(Material.SPIDER_EYE, " oko pajaka");
        polishNames.put(Material.FERMENTED_SPIDER_EYE, " zfermentowane oko pajaka");
        polishNames.put(Material.BLAZE_POWDER, " blaze powder");
        polishNames.put(Material.MAGMA_CREAM, " magmowy krem");
        polishNames.put(Material.BREWING_STAND_ITEM, " stol alchemiczny");
        polishNames.put(Material.CAULDRON_ITEM, " kociol");
        polishNames.put(Material.EYE_OF_ENDER, " oko kresu");
        polishNames.put(Material.SPECKLED_MELON, " arbuz");
        polishNames.put(Material.MONSTER_EGG, " jajko spawnujace");
        polishNames.put(Material.EXP_BOTTLE, " butelka z expem");
        polishNames.put(Material.FIREBALL, " kula ognia");
        polishNames.put(Material.BOOK_AND_QUILL, " ksiazka z piorem");
        polishNames.put(Material.WRITTEN_BOOK, " zapisana ksiazka");
        polishNames.put(Material.EMERALD, " emerald");
        polishNames.put(Material.ITEM_FRAME, " ramka na obraz");
        polishNames.put(Material.FLOWER_POT_ITEM, " doniczka");
        polishNames.put(Material.CARROT_ITEM, " marchewka");
        polishNames.put(Material.POTATO_ITEM, " ziemniak");
        polishNames.put(Material.BAKED_POTATO, " upieczony ziemniak");
        polishNames.put(Material.POISONOUS_POTATO, " trujacy ziemniak");
        polishNames.put(Material.EMPTY_MAP, " pusta mapa");
        polishNames.put(Material.GOLDEN_CARROT, " zlota marchewka");
        polishNames.put(Material.SKULL_ITEM, " glowa");
        polishNames.put(Material.CARROT_STICK, " marchewka na patyku");
        polishNames.put(Material.NETHER_STAR, " gwiazda netherowa");
        polishNames.put(Material.PUMPKIN_PIE, " placek dyniowy");
        polishNames.put(Material.FIREWORK, " fajerwerka");
        polishNames.put(Material.FIREWORK_CHARGE, " fajerwerka");
        polishNames.put(Material.ENCHANTED_BOOK, " enchantowana ksiazka");
        polishNames.put(Material.REDSTONE_COMPARATOR, " komperator");
        polishNames.put(Material.NETHER_BRICK_ITEM, " cegla netherowa");
        polishNames.put(Material.QUARTZ, " kwarc");
        polishNames.put(Material.EXPLOSIVE_MINECART, " wagonik z tnt");
        polishNames.put(Material.HOPPER_MINECART, " wagonik z lejem");
        polishNames.put(Material.IRON_BARDING, " zelazna motyka");
        polishNames.put(Material.GOLD_BARDING, " zlota motyka");
        polishNames.put(Material.DIAMOND_BARDING, " diamentowa motyka");
        polishNames.put(Material.LEASH, " lasso");
        polishNames.put(Material.NAME_TAG, " name tag");
        polishNames.put(Material.COMMAND_MINECART, " wagonik z blokiem polecen");
        polishNames.put(Material.GOLD_RECORD, " plyta muzyczna");
        polishNames.put(Material.GREEN_RECORD, " plyta muzyczna");
        polishNames.put(Material.RECORD_3, " plyta muzyczna");
        polishNames.put(Material.RECORD_4, " plyta muzyczna");
        polishNames.put(Material.RECORD_5, " plyta muzyczna");
        polishNames.put(Material.RECORD_6, " plyta muzyczna");
        polishNames.put(Material.RECORD_7, " plyta muzyczna");
        polishNames.put(Material.RECORD_8, " plyta muzyczna");
        polishNames.put(Material.RECORD_9, " plyta muzyczna");
        polishNames.put(Material.RECORD_10, " plyta muzyczna");
        polishNames.put(Material.RECORD_11, " plyta muzyczna");
        polishNames.put(Material.RECORD_12, " plyta muzyczna");
    }
}
