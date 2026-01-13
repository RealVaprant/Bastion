package net.vaprant.bastion.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BastionNotification {

    public static void error(CommandSender sender, String message) {
        directMessage(sender, Component.text(message).color(NamedTextColor.RED));
    }

    public static void success(CommandSender sender, String message) {
        directMessage(sender, Component.text(message).color(NamedTextColor.GREEN));
    }

    public static void warning(CommandSender sender, String message) {
        directMessage(sender, Component.text(message).color(NamedTextColor.YELLOW));
    }

    public static void info(CommandSender sender, String message) {
        directMessage(sender, Component.text(message).color(TextColor.color(0xA5FF)));
    }
    public static void info(CommandSender sender, Component message) {
        directMessage(sender, Component.empty().color(TextColor.color(0xA5FF)).append(message));
    }

    public static void broadcast(String message) {
        Bukkit.broadcast(Component.text(message).color(TextColor.color(0xA5FF)));
    }
    public static void broadcast(Component message) {
        Bukkit.broadcast(Component.empty().color(TextColor.color(0xA5FF)).append(message));
    }

    private static void directMessage(CommandSender sender, Component message) {
        if (sender instanceof Player player) {
            player.sendActionBar(message);
        }
        else {
            sender.sendMessage(message);
        }
    }

}
