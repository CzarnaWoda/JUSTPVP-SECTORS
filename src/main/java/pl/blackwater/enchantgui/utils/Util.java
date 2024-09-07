package pl.blackwater.enchantgui.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Util {

    private static final Random random = new Random();

    public static String fixColors(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String[] fixColors(String... texts) {
        String[] strings = new String[texts.length];
        for (int i = 0; i < strings.length; i++)
            strings[i] = fixColors(texts[i]);
        return strings;
    }

    public static List<String> fixColors(List<String> texts) {
        return texts.stream().map(Util::fixColors).collect(Collectors.toList());
    }

    public static void sendMessage(Player player, String text) {
        player.sendMessage(text);
    }

    public static void sendMessage(CommandSender sender, String text) {
        sender.sendMessage(text);
    }

    public static void sendMessageFixed(Player player, String text) {
        player.sendMessage(fixColors(text));
    }

    public static void sendMessageFixed(CommandSender sender, String text) {
        sender.sendMessage(fixColors(text));
    }

    public static boolean getChance(double chance) {
        return (Math.random() < chance);
    }

    public static int getRandomInteger(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public static double getRandomDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
