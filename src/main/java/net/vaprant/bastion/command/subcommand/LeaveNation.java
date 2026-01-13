package net.vaprant.bastion.command.subcommand;

import net.vaprant.bastion.player.BastionPlayer;
import net.vaprant.bastion.util.BastionNotification;
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

        if (bastionPlayer.getNation() == null) {
            BastionNotification.error(player, "You are not in a nation.");
        }
        else {
            BastionNotification.info(player, "You are no longer in " + bastionPlayer.getNation().name + ".");
            bastionPlayer.getNation().removeMember(bastionPlayer);
        }

    }
}