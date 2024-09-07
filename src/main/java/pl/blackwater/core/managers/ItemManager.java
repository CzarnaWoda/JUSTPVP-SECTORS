package pl.blackwater.core.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pl.blackwater.core.utils.StringUtil;

@SuppressWarnings("deprecation")
public class ItemManager
{
    public static Map<Material, String> LANG;
    private static final Material[] repairable = {
                Material.DIAMOND_PICKAXE, Material.DIAMOND_SWORD, Material.DIAMOND_SPADE, Material.DIAMOND_AXE, Material.DIAMOND_HOE,
                Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.IRON_PICKAXE,
                Material.IRON_SWORD, Material.IRON_SPADE, Material.IRON_AXE, Material.IRON_HOE, Material.IRON_HELMET, Material.IRON_CHESTPLATE,
                Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.GOLD_PICKAXE, Material.GOLD_SWORD, Material.GOLD_SPADE, Material.GOLD_AXE,
                Material.GOLD_HOE, Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS, Material.STONE_PICKAXE,
                Material.STONE_SWORD, Material.STONE_SPADE, Material.STONE_AXE, Material.STONE_HOE, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE,
                Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS, Material.WOOD_PICKAXE, Material.WOOD_SWORD, Material.WOOD_SPADE,
                Material.WOOD_AXE, Material.WOOD_HOE, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS,
                Material.LEATHER_BOOTS, Material.BOW, Material.FISHING_ROD};
    static {
        (ItemManager.LANG = new HashMap<>()).put(Material.AIR, "powietrze");
        ItemManager.LANG.put(Material.STONE, "kamien");
        ItemManager.LANG.put(Material.GRASS, "trawa");
        ItemManager.LANG.put(Material.DIRT, "ziemia");
        ItemManager.LANG.put(Material.COBBLESTONE, "bruk");
        ItemManager.LANG.put(Material.WOOD, "deski");
        ItemManager.LANG.put(Material.SAPLING, "sadzonka");
        ItemManager.LANG.put(Material.BEDROCK, "bedrock");
        ItemManager.LANG.put(Material.WATER, "woda");
        ItemManager.LANG.put(Material.STATIONARY_WATER, "woda");
        ItemManager.LANG.put(Material.LAVA, "lawa");
        ItemManager.LANG.put(Material.STATIONARY_LAVA, "lawa");
        ItemManager.LANG.put(Material.SAND, "piasek");
        ItemManager.LANG.put(Material.GRAVEL, "zwir");
        ItemManager.LANG.put(Material.GOLD_ORE, "ruda zlota");
        ItemManager.LANG.put(Material.IRON_ORE, "ruda zelaza");
        ItemManager.LANG.put(Material.COAL_ORE, "ruda wegla");
        ItemManager.LANG.put(Material.LOG, "drewno");
        ItemManager.LANG.put(Material.LEAVES, "liscie");
        ItemManager.LANG.put(Material.SPONGE, "gabka");
        ItemManager.LANG.put(Material.GLASS, "szklo");
        ItemManager.LANG.put(Material.LAPIS_ORE, "ruda lipisu");
        ItemManager.LANG.put(Material.LAPIS_BLOCK, "blok lapisu");
        ItemManager.LANG.put(Material.DISPENSER, "dozownik");
        ItemManager.LANG.put(Material.SANDSTONE, "pisakowiec");
        ItemManager.LANG.put(Material.NOTE_BLOCK, "note block");
        ItemManager.LANG.put(Material.BED_BLOCK, "lozko");
        ItemManager.LANG.put(Material.POWERED_RAIL, "zasilane tory");
        ItemManager.LANG.put(Material.DETECTOR_RAIL, "tory z czujnikiem");
        ItemManager.LANG.put(Material.PISTON_STICKY_BASE, "tlok");
        ItemManager.LANG.put(Material.WEB, "nic");
        ItemManager.LANG.put(Material.LONG_GRASS, "trawa");
        ItemManager.LANG.put(Material.DEAD_BUSH, "uschneity krzak");
        ItemManager.LANG.put(Material.PISTON_BASE, "tlok");
        ItemManager.LANG.put(Material.PISTON_EXTENSION, "tlok");
        ItemManager.LANG.put(Material.WOOL, "welna");
        ItemManager.LANG.put(Material.PISTON_MOVING_PIECE, "tlok");
        ItemManager.LANG.put(Material.YELLOW_FLOWER, "tulipan");
        ItemManager.LANG.put(Material.RED_ROSE, "roza");
        ItemManager.LANG.put(Material.BROWN_MUSHROOM, "brazowy grzyb");
        ItemManager.LANG.put(Material.RED_MUSHROOM, "muchomor");
        ItemManager.LANG.put(Material.GOLD_BLOCK, "blok zlota");
        ItemManager.LANG.put(Material.IRON_BLOCK, "blok zelaza");
        ItemManager.LANG.put(Material.DOUBLE_STEP, "podwojna polplytka");
        ItemManager.LANG.put(Material.STEP, "polplytka");
        ItemManager.LANG.put(Material.BRICK, "cegly");
        ItemManager.LANG.put(Material.TNT, "tnt");
        ItemManager.LANG.put(Material.BOOKSHELF, "biblioteczka");
        ItemManager.LANG.put(Material.MOSSY_COBBLESTONE, "zamszony bruk");
        ItemManager.LANG.put(Material.OBSIDIAN, "obsydian");
        ItemManager.LANG.put(Material.TORCH, "pochodnia");
        ItemManager.LANG.put(Material.FIRE, "ogien");
        ItemManager.LANG.put(Material.MOB_SPAWNER, "mob spawner");
        ItemManager.LANG.put(Material.WOOD_STAIRS, "drewniane schodki");
        ItemManager.LANG.put(Material.CHEST, "skrzynia");
        ItemManager.LANG.put(Material.REDSTONE_WIRE, "redstone");
        ItemManager.LANG.put(Material.DIAMOND_ORE, "ruda diamentu");
        ItemManager.LANG.put(Material.DIAMOND_BLOCK, "blok diamentu");
        ItemManager.LANG.put(Material.WORKBENCH, "stol rzemieslniczy");
        ItemManager.LANG.put(Material.CROPS, "nasionka");
        ItemManager.LANG.put(Material.SOIL, "nasionka");
        ItemManager.LANG.put(Material.FURNACE, "piecyk");
        ItemManager.LANG.put(Material.BURNING_FURNACE, "piecyk");
        ItemManager.LANG.put(Material.SIGN_POST, "tabliczka");
        ItemManager.LANG.put(Material.WOODEN_DOOR, "drewniane drzwi");
        ItemManager.LANG.put(Material.LADDER, "drabinka");
        ItemManager.LANG.put(Material.RAILS, "tory");
        ItemManager.LANG.put(Material.COBBLESTONE_STAIRS, "brukowe schody");
        ItemManager.LANG.put(Material.WALL_SIGN, "tabliczka");
        ItemManager.LANG.put(Material.LEVER, "dzwignia");
        ItemManager.LANG.put(Material.STONE_PLATE, "plytka naciskowa");
        ItemManager.LANG.put(Material.IRON_DOOR_BLOCK, "zelazne drzwi");
        ItemManager.LANG.put(Material.WOOD_PLATE, "plytka nasickowa");
        ItemManager.LANG.put(Material.REDSTONE_ORE, "ruda redstone");
        ItemManager.LANG.put(Material.GLOWING_REDSTONE_ORE, "ruda redstone");
        ItemManager.LANG.put(Material.REDSTONE_TORCH_OFF, "czerwona pochodnia");
        ItemManager.LANG.put(Material.REDSTONE_TORCH_ON, "czerwona pochodnia");
        ItemManager.LANG.put(Material.STONE_BUTTON, "kamienny przycisk");
        ItemManager.LANG.put(Material.SNOW, "snieg");
        ItemManager.LANG.put(Material.ICE, "lod");
        ItemManager.LANG.put(Material.SNOW_BLOCK, "snieg");
        ItemManager.LANG.put(Material.CACTUS, "kaktus");
        ItemManager.LANG.put(Material.CLAY, "glina");
        ItemManager.LANG.put(Material.SUGAR_CANE_BLOCK, "trzcina");
        ItemManager.LANG.put(Material.JUKEBOX, "szafa grajaca");
        ItemManager.LANG.put(Material.FENCE, "plotek");
        ItemManager.LANG.put(Material.PUMPKIN, "dynia");
        ItemManager.LANG.put(Material.NETHERRACK, "netherrack");
        ItemManager.LANG.put(Material.SOUL_SAND, "pisaek dusz");
        ItemManager.LANG.put(Material.GLOWSTONE, "jasnoglaz");
        ItemManager.LANG.put(Material.PORTAL, "portal");
        ItemManager.LANG.put(Material.JACK_O_LANTERN, "jack'o'latern");
        ItemManager.LANG.put(Material.CAKE_BLOCK, "ciasto");
        ItemManager.LANG.put(Material.DIODE_BLOCK_OFF, "przekaznik");
        ItemManager.LANG.put(Material.DIODE_BLOCK_ON, "przekaznik");
        ItemManager.LANG.put(Material.getMaterial(146), "zamknieta skrzynia");
        ItemManager.LANG.put(Material.STAINED_GLASS, "utwardzone szklo");
        ItemManager.LANG.put(Material.TRAP_DOOR, "wlaz");
        ItemManager.LANG.put(Material.MONSTER_EGGS, "jajko potwora");
        ItemManager.LANG.put(Material.SMOOTH_BRICK, "cegly");
        ItemManager.LANG.put(Material.HUGE_MUSHROOM_1, "duzy grzyb");
        ItemManager.LANG.put(Material.HUGE_MUSHROOM_2, "duzy grzyb");
        ItemManager.LANG.put(Material.IRON_FENCE, "kraty");
        ItemManager.LANG.put(Material.THIN_GLASS, "szyba");
        ItemManager.LANG.put(Material.MELON_BLOCK, "arbuz");
        ItemManager.LANG.put(Material.PUMPKIN_STEM, "dynia");
        ItemManager.LANG.put(Material.MELON_STEM, "arbuz");
        ItemManager.LANG.put(Material.VINE, "pnacze");
        ItemManager.LANG.put(Material.FENCE_GATE, "furtka");
        ItemManager.LANG.put(Material.BRICK_STAIRS, "ceglane schodki");
        ItemManager.LANG.put(Material.SMOOTH_STAIRS, "kamienne schodki");
        ItemManager.LANG.put(Material.MYCEL, "gryzbnia");
        ItemManager.LANG.put(Material.WATER_LILY, "lilia wodna");
        ItemManager.LANG.put(Material.NETHER_BRICK, "cegly netherowe");
        ItemManager.LANG.put(Material.NETHER_FENCE, "netherowy plotek");
        ItemManager.LANG.put(Material.NETHER_BRICK_STAIRS, "netherowe schodki");
        ItemManager.LANG.put(Material.NETHER_WARTS, "brodawki");
        ItemManager.LANG.put(Material.ENCHANTMENT_TABLE, "stol do enchantu");
        ItemManager.LANG.put(Material.BREWING_STAND, "stol alchemiczny");
        ItemManager.LANG.put(Material.CAULDRON, "kociol");
        ItemManager.LANG.put(Material.ENDER_PORTAL, "ender portal");
        ItemManager.LANG.put(Material.ENDER_PORTAL_FRAME, "ender portal");
        ItemManager.LANG.put(Material.ENDER_STONE, "kamien kresu");
        ItemManager.LANG.put(Material.DRAGON_EGG, "jajko smoka");
        ItemManager.LANG.put(Material.REDSTONE_LAMP_OFF, "lampa");
        ItemManager.LANG.put(Material.REDSTONE_LAMP_ON, "lampa");
        ItemManager.LANG.put(Material.WOOD_DOUBLE_STEP, "podwojna drewniana polplytka");
        ItemManager.LANG.put(Material.WOOD_STEP, "drewnania polplytka");
        ItemManager.LANG.put(Material.COCOA, "kakao");
        ItemManager.LANG.put(Material.SANDSTONE_STAIRS, "piaskowe schodki");
        ItemManager.LANG.put(Material.EMERALD_ORE, "ruda szmaragdu");
        ItemManager.LANG.put(Material.ENDER_CHEST, "skrzynia kresu");
        ItemManager.LANG.put(Material.TRIPWIRE_HOOK, "potykacz");
        ItemManager.LANG.put(Material.TRIPWIRE, "potykacz");
        ItemManager.LANG.put(Material.EMERALD_BLOCK, "blok szmaragdu");
        ItemManager.LANG.put(Material.SPRUCE_WOOD_STAIRS, "drewniane schodki");
        ItemManager.LANG.put(Material.BIRCH_WOOD_STAIRS, "drewniane schodki");
        ItemManager.LANG.put(Material.JUNGLE_WOOD_STAIRS, "drewniane schodki");
        ItemManager.LANG.put(Material.COMMAND, "blok polecen");
        ItemManager.LANG.put(Material.BEACON, "magiczna latarnia");
        ItemManager.LANG.put(Material.COBBLE_WALL, "brukowy plotek");
        ItemManager.LANG.put(Material.FLOWER_POT, "doniczka");
        ItemManager.LANG.put(Material.CARROT, "marchewka");
        ItemManager.LANG.put(Material.POTATO, "ziemniak");
        ItemManager.LANG.put(Material.WOOD_BUTTON, "drewniany przycisk");
        ItemManager.LANG.put(Material.SKULL, "glowa");
        ItemManager.LANG.put(Material.ANVIL, "kowadlo");
        ItemManager.LANG.put(Material.TRAPPED_CHEST, "skrzynka z pulapka");
        ItemManager.LANG.put(Material.GOLD_PLATE, "zlota polplytka");
        ItemManager.LANG.put(Material.IRON_PLATE, "zelaza polplytka");
        ItemManager.LANG.put(Material.REDSTONE_COMPARATOR_OFF, "komparator");
        ItemManager.LANG.put(Material.REDSTONE_COMPARATOR_ON, "komparator");
        ItemManager.LANG.put(Material.DAYLIGHT_DETECTOR, "detektor swiatla dziennego");
        ItemManager.LANG.put(Material.REDSTONE_BLOCK, "blok redstone");
        ItemManager.LANG.put(Material.QUARTZ_ORE, "ruda kwarcu");
        ItemManager.LANG.put(Material.HOPPER, "lej");
        ItemManager.LANG.put(Material.QUARTZ_BLOCK, "blok kwarcu");
        ItemManager.LANG.put(Material.QUARTZ_STAIRS, "kwarcowe schodki");
        ItemManager.LANG.put(Material.ACTIVATOR_RAIL, "tory aktywacyjne");
        ItemManager.LANG.put(Material.DROPPER, "podajnik");
        ItemManager.LANG.put(Material.STAINED_CLAY, "utwardzona glina");
        ItemManager.LANG.put(Material.STAINED_GLASS_PANE, "utwardzona szyba");
        ItemManager.LANG.put(Material.LEAVES_2, "liscie");
        ItemManager.LANG.put(Material.LOG_2, "drewno");
        ItemManager.LANG.put(Material.ACACIA_STAIRS, "drewniane schodki");
        ItemManager.LANG.put(Material.DARK_OAK_STAIRS, "drewniane schodki");
        ItemManager.LANG.put(Material.HAY_BLOCK, "sloma");
        ItemManager.LANG.put(Material.CARPET, "dywan");
        ItemManager.LANG.put(Material.HARD_CLAY, "glina");
        ItemManager.LANG.put(Material.COAL_BLOCK, "blok wegla");
        ItemManager.LANG.put(Material.PACKED_ICE, "utwardzony lod");
        ItemManager.LANG.put(Material.DOUBLE_PLANT, "sadzonka");
        ItemManager.LANG.put(Material.IRON_SPADE, "zelazna lopata");
        ItemManager.LANG.put(Material.IRON_PICKAXE, "zelazny kilof");
        ItemManager.LANG.put(Material.IRON_AXE, "zelazna siekiera");
        ItemManager.LANG.put(Material.FLINT_AND_STEEL, "zapalniczka");
        ItemManager.LANG.put(Material.APPLE, "jablko");
        ItemManager.LANG.put(Material.BOW, "luk");
        ItemManager.LANG.put(Material.ARROW, "strzala");
        ItemManager.LANG.put(Material.COAL, "wegiel");
        ItemManager.LANG.put(Material.DIAMOND, "diament");
        ItemManager.LANG.put(Material.IRON_INGOT, "sztabka zelaza");
        ItemManager.LANG.put(Material.GOLD_INGOT, "sztabka zlota");
        ItemManager.LANG.put(Material.IRON_SWORD, "zelazny miecz");
        ItemManager.LANG.put(Material.WOOD_SWORD, "drewniany miecz");
        ItemManager.LANG.put(Material.WOOD_SPADE, "drewniana lopata");
        ItemManager.LANG.put(Material.WOOD_PICKAXE, "drewniany kilof");
        ItemManager.LANG.put(Material.WOOD_AXE, "drewnania siekiera");
        ItemManager.LANG.put(Material.STONE_SWORD, "kamienny miecz");
        ItemManager.LANG.put(Material.STONE_SPADE, "kamienna lopata");
        ItemManager.LANG.put(Material.STONE_PICKAXE, "kamienny kilof");
        ItemManager.LANG.put(Material.STONE_AXE, "kamienna siekiera");
        ItemManager.LANG.put(Material.DIAMOND_SWORD, "diamentowy miecz");
        ItemManager.LANG.put(Material.DIAMOND_SPADE, "diamentowa lopata");
        ItemManager.LANG.put(Material.DIAMOND_PICKAXE, "diamentowy kilof");
        ItemManager.LANG.put(Material.DIAMOND_AXE, "diamentowa siekiera");
        ItemManager.LANG.put(Material.STICK, "patyk");
        ItemManager.LANG.put(Material.BOWL, "miseczka");
        ItemManager.LANG.put(Material.MUSHROOM_SOUP, "zupa grzybowa");
        ItemManager.LANG.put(Material.GOLD_SWORD, "zloty miecz");
        ItemManager.LANG.put(Material.GOLD_SPADE, "zlota lopata");
        ItemManager.LANG.put(Material.GOLD_PICKAXE, "zloty kilof");
        ItemManager.LANG.put(Material.GOLD_AXE, "zlota siekiera");
        ItemManager.LANG.put(Material.STRING, "nitka");
        ItemManager.LANG.put(Material.FEATHER, "pioro");
        ItemManager.LANG.put(Material.SULPHUR, "proch strzelniczy");
        ItemManager.LANG.put(Material.WOOD_HOE, "drewniana motyka");
        ItemManager.LANG.put(Material.STONE_HOE, "kamienna motyka");
        ItemManager.LANG.put(Material.IRON_HOE, "zelazna motyka");
        ItemManager.LANG.put(Material.DIAMOND_HOE, "diemtnowa motyka");
        ItemManager.LANG.put(Material.GOLD_HOE, "zlota motyka");
        ItemManager.LANG.put(Material.SEEDS, "nasionka");
        ItemManager.LANG.put(Material.WHEAT, "pszenica");
        ItemManager.LANG.put(Material.BREAD, "chleb");
        ItemManager.LANG.put(Material.LEATHER_HELMET, "skorzany helm");
        ItemManager.LANG.put(Material.LEATHER_CHESTPLATE, "skorzana klata");
        ItemManager.LANG.put(Material.LEATHER_LEGGINGS, "skorzane spodnie");
        ItemManager.LANG.put(Material.LEATHER_BOOTS, "skorzane buty");
        ItemManager.LANG.put(Material.CHAINMAIL_HELMET, "helm z kolcza");
        ItemManager.LANG.put(Material.CHAINMAIL_CHESTPLATE, "klata z kolcza");
        ItemManager.LANG.put(Material.CHAINMAIL_LEGGINGS, "spodnie z kolcza");
        ItemManager.LANG.put(Material.CHAINMAIL_BOOTS, "buty z kolcza");
        ItemManager.LANG.put(Material.IRON_HELMET, "zelazny helm");
        ItemManager.LANG.put(Material.IRON_CHESTPLATE, "zelazna klata");
        ItemManager.LANG.put(Material.IRON_LEGGINGS, "zelazne spodnie");
        ItemManager.LANG.put(Material.IRON_BOOTS, "zelazne buty");
        ItemManager.LANG.put(Material.DIAMOND_HELMET, "diamentowy helm");
        ItemManager.LANG.put(Material.DIAMOND_CHESTPLATE, "diamentowa klata");
        ItemManager.LANG.put(Material.DIAMOND_LEGGINGS, "diamentowe spodnie");
        ItemManager.LANG.put(Material.DIAMOND_BOOTS, "diamentowe buty");
        ItemManager.LANG.put(Material.GOLD_HELMET, "zloty helm");
        ItemManager.LANG.put(Material.GOLD_CHESTPLATE, "zlota klata");
        ItemManager.LANG.put(Material.GOLD_LEGGINGS, "zlote spodnie");
        ItemManager.LANG.put(Material.GOLD_BOOTS, "zlote buty");
        ItemManager.LANG.put(Material.FLINT, "krzemien");
        ItemManager.LANG.put(Material.PORK, "schab");
        ItemManager.LANG.put(Material.GRILLED_PORK, "pieczony schab");
        ItemManager.LANG.put(Material.PAINTING, "obraz");
        ItemManager.LANG.put(Material.GOLDEN_APPLE,"zlote jablko");
        ItemManager.LANG.put(Material.SIGN, "znak");
        ItemManager.LANG.put(Material.WOOD_DOOR, "drewniane drzwi");
        ItemManager.LANG.put(Material.BUCKET, "wiaderko");
        ItemManager.LANG.put(Material.WATER_BUCKET, "wiaderko wody");
        ItemManager.LANG.put(Material.LAVA_BUCKET, "wiaderko lawy");
        ItemManager.LANG.put(Material.MINECART, "wagonik");
        ItemManager.LANG.put(Material.SADDLE, "siodlo");
        ItemManager.LANG.put(Material.IRON_DOOR, "zelazne drzwi");
        ItemManager.LANG.put(Material.REDSTONE, "czerwony proszek");
        ItemManager.LANG.put(Material.SNOW_BALL, "sniezka");
        ItemManager.LANG.put(Material.BOAT, "lodka");
        ItemManager.LANG.put(Material.LEATHER, "skora");
        ItemManager.LANG.put(Material.MILK_BUCKET, "wiaderko mleka");
        ItemManager.LANG.put(Material.CLAY_BRICK, "cegly");
        ItemManager.LANG.put(Material.CLAY_BALL, "kulka gliny");
        ItemManager.LANG.put(Material.SUGAR_CANE, "trzcina cukrowa");
        ItemManager.LANG.put(Material.PAPER, "papier");
        ItemManager.LANG.put(Material.BOOK, "ksiazka");
        ItemManager.LANG.put(Material.SLIME_BALL, "kolka szlamu");
        ItemManager.LANG.put(Material.STORAGE_MINECART, "wagonik");
        ItemManager.LANG.put(Material.POWERED_MINECART, "wagonik");
        ItemManager.LANG.put(Material.EGG, "jajko");
        ItemManager.LANG.put(Material.COMPASS, "kompas");
        ItemManager.LANG.put(Material.FISHING_ROD, "wedka");
        ItemManager.LANG.put(Material.WATCH, "zegar");
        ItemManager.LANG.put(Material.GLOWSTONE_DUST, "jasnopyl");
        ItemManager.LANG.put(Material.RAW_FISH, "ryba");
        ItemManager.LANG.put(Material.COOKED_FISH, "pieczona ryba");
        ItemManager.LANG.put(Material.INK_SACK, "czarny barwnik");
        ItemManager.LANG.put(Material.BONE, "kosc");
        ItemManager.LANG.put(Material.SUGAR, "cukier");
        ItemManager.LANG.put(Material.CAKE, "ciasto");
        ItemManager.LANG.put(Material.BED, "lozko");
        ItemManager.LANG.put(Material.DIODE, "przekaznik");
        ItemManager.LANG.put(Material.COOKIE, "ciastko");
        ItemManager.LANG.put(Material.MAP, "mapa");
        ItemManager.LANG.put(Material.SHEARS, "nozyce");
        ItemManager.LANG.put(Material.MELON, "arbuz");
        ItemManager.LANG.put(Material.PUMPKIN_SEEDS, "nasiono dyni");
        ItemManager.LANG.put(Material.MELON_SEEDS, "nasiono melona");
        ItemManager.LANG.put(Material.RAW_BEEF, "stek");
        ItemManager.LANG.put(Material.COOKED_BEEF, "pieczony stek");
        ItemManager.LANG.put(Material.RAW_CHICKEN, "kurczak");
        ItemManager.LANG.put(Material.COOKED_CHICKEN, "upieczony kurczak");
        ItemManager.LANG.put(Material.ROTTEN_FLESH, "zgnile mieso");
        ItemManager.LANG.put(Material.ENDER_PEARL, "perla endermana");
        ItemManager.LANG.put(Material.BLAZE_ROD, "palka blaza");
        ItemManager.LANG.put(Material.GHAST_TEAR, "lza gasta");
        ItemManager.LANG.put(Material.GOLD_NUGGET, "zloty samorodek");
        ItemManager.LANG.put(Material.NETHER_STALK, "brodawka netherowa");
        ItemManager.LANG.put(Material.POTION, "mikstura");
        ItemManager.LANG.put(Material.GLASS_BOTTLE, "szklana butelka");
        ItemManager.LANG.put(Material.SPIDER_EYE, "oko pajaka");
        ItemManager.LANG.put(Material.FERMENTED_SPIDER_EYE, "zfermentowane oko pajaka");
        ItemManager.LANG.put(Material.BLAZE_POWDER, "blaze powder");
        ItemManager.LANG.put(Material.MAGMA_CREAM, "magmowy krem");
        ItemManager.LANG.put(Material.BREWING_STAND_ITEM, "stol alchemiczny");
        ItemManager.LANG.put(Material.CAULDRON_ITEM, "kociol");
        ItemManager.LANG.put(Material.EYE_OF_ENDER, "oko kresu");
        ItemManager.LANG.put(Material.SPECKLED_MELON, "arbuz");
        ItemManager.LANG.put(Material.MONSTER_EGG, "jajko spawnujace");
        ItemManager.LANG.put(Material.EXP_BOTTLE, "butelka z expem");
        ItemManager.LANG.put(Material.FIREBALL, "kula ognia");
        ItemManager.LANG.put(Material.BOOK_AND_QUILL, "ksiazka z piorem");
        ItemManager.LANG.put(Material.WRITTEN_BOOK, "zapisana ksiazka");
        ItemManager.LANG.put(Material.EMERALD, "emerald");
        ItemManager.LANG.put(Material.ITEM_FRAME, "ramka na obraz");
        ItemManager.LANG.put(Material.FLOWER_POT_ITEM, "doniczka");
        ItemManager.LANG.put(Material.CARROT_ITEM, "marchewka");
        ItemManager.LANG.put(Material.POTATO_ITEM, "ziemniak");
        ItemManager.LANG.put(Material.BAKED_POTATO, "upieczony ziemniak");
        ItemManager.LANG.put(Material.POISONOUS_POTATO, "trujacy ziemniak");
        ItemManager.LANG.put(Material.EMPTY_MAP, "pusta mapa");
        ItemManager.LANG.put(Material.GOLDEN_CARROT, "zlota marchewka");
        ItemManager.LANG.put(Material.SKULL_ITEM, "glowa");
        ItemManager.LANG.put(Material.CARROT_STICK, "marchewka na patyku");
        ItemManager.LANG.put(Material.NETHER_STAR, "gwiazda netherowa");
        ItemManager.LANG.put(Material.PUMPKIN_PIE, "placek dyniowy");
        ItemManager.LANG.put(Material.FIREWORK, "fajerwerka");
        ItemManager.LANG.put(Material.FIREWORK_CHARGE, "fajerwerka");
        ItemManager.LANG.put(Material.ENCHANTED_BOOK, "enchantowana ksiazka");
        ItemManager.LANG.put(Material.REDSTONE_COMPARATOR, "komperator");
        ItemManager.LANG.put(Material.NETHER_BRICK_ITEM, "cegla netherowa");
        ItemManager.LANG.put(Material.QUARTZ, "kwarc");
        ItemManager.LANG.put(Material.EXPLOSIVE_MINECART, "wagonik z tnt");
        ItemManager.LANG.put(Material.HOPPER_MINECART, "wagonik z lejem");
        ItemManager.LANG.put(Material.IRON_BARDING, "zelazna motyka");
        ItemManager.LANG.put(Material.GOLD_BARDING, "zlota motyka");
        ItemManager.LANG.put(Material.DIAMOND_BARDING, "diamentowa motyka");
        ItemManager.LANG.put(Material.LEASH, "lasso");
        ItemManager.LANG.put(Material.NAME_TAG, "name tag");
        ItemManager.LANG.put(Material.COMMAND_MINECART, "wagonik z blokiem polecen");
        ItemManager.LANG.put(Material.GOLD_RECORD, "plyta muzyczna");
        ItemManager.LANG.put(Material.GREEN_RECORD, "plyta muzyczna");
        ItemManager.LANG.put(Material.RECORD_3, "plyta muzyczna");
        ItemManager.LANG.put(Material.RECORD_4, "plyta muzyczna");
        ItemManager.LANG.put(Material.RECORD_5, "plyta muzyczna");
        ItemManager.LANG.put(Material.RECORD_6, "plyta muzyczna");
        ItemManager.LANG.put(Material.RECORD_7, "plyta muzyczna");
        ItemManager.LANG.put(Material.RECORD_8, "plyta muzyczna");
        ItemManager.LANG.put(Material.RECORD_9, "plyta muzyczna");
        ItemManager.LANG.put(Material.RECORD_10, "plyta muzyczna");
        ItemManager.LANG.put(Material.RECORD_11, "plyta muzyczna");
        ItemManager.LANG.put(Material.RECORD_12, "plyta muzyczna");
    }

    public static List<String> parseColor(List<String> colour) {
    	final List<String> l = new ArrayList<>();
        for (final String message : colour) {
            l.add(ChatColor.translateAlternateColorCodes('&', message));
        }
        return l;
    }
    
    public static List<String> replaceInList(List<String> list, String from, String to) {
    	final List<String> l = new ArrayList<>();
        for (final String s : list) {
            l.add(StringUtil.replaceText(s, from, to));
        }
        return l;
    }
    
    public static void setAndGet(Inventory inv, ItemStack toRemove, int slot) {
    	final ItemStack is = inv.getItem(slot);
        int amount = is.getAmount();
        amount -= toRemove.getAmount();
        if (amount < 0) {
            amount = 0;
        }
        if (amount == 0) {
            inv.setItem(slot, new ItemStack(Material.AIR));
        }
        else {
            is.setAmount(amount);
            inv.setItem(slot, is);
        }
    }
    
    public static String get(Material mat) {
    	final String s = ItemManager.LANG.get(mat);
        if (s == null) {
            return mat.name().toLowerCase().replace('_', ' ');
        }
        return s;
    }
    public static boolean isRepairable(ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR)) return false;
        for (Material mat : repairable) {
            if (item.getType().equals(mat)) {
                return true;
            }
        }
        return false;
    }
}
