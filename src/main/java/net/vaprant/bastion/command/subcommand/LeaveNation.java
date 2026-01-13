package net.vaprant.bastion.command.subcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.vaprant.bastion.nation.Nation;
import net.vaprant.bastion.player.BastionPlayer;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveNation implements NationSubCommand{
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            BastionNotification.error(sender, "The console cannot join nor leave nations.");
            return;
        }

        BastionPlayer bastionPlayer = BastionPlayer.registry.getPlayer(player.getUniqueId());
        Nation nation = bastionPlayer.getNation();

        if (nation == null) {
            BastionNotification.error(player, "You are not in a nation.");
        }
        else {

            BastionNotification.info(player, Component.text(
                            "You are no longer in "
                    ).append(Component.text(
                                    bastionPlayer.getNation().name
                            ).color(NamedTextColor.YELLOW)
                    ).append(Component.text("."))
            );


            nation.removeMember(bastionPlayer);
            if (!nation.isDisbaned) {
                nation.broadcast(Component.text(
                        player.getName() + " left the nation."
                ).color(TextColor.color(0xA5FF)));
            }
        }

    }
}