package net.vaprant.bastion.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BastionNotification {

    public static void error(Player player, String message) {
        player.sendActionBar(Component.text(message).color(NamedTextColor.RED));
    }

    public static void success(Player player, String message) {
        player.sendActionBar(Component.text(message).color(NamedTextColor.GREEN));
    }

    public static void error(CommandSender sender, String message) {
        sender.sendMessage(Component.text(message).color(NamedTextColor.RED));
    }

    public static void success(CommandSender sender, String message) {
        sender.sendMessage(Component.text(message).color(NamedTextColor.GREEN));
    }

    public static void warning(Player player, String message) {
        player.sendActionBar(Component.text(message).color(NamedTextColor.YELLOW));
    }

    public static void warning(CommandSender sender, String message) {
        sender.sendMessage(Component.text(message).color(NamedTextColor.YELLOW));
    }

}
