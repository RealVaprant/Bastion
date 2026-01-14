package net.vaprant.bastion.command.subcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.vaprant.bastion.nation.Authority;
import net.vaprant.bastion.nation.Nation;
import net.vaprant.bastion.player.BastionProfile;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickMember implements NationSubCommand{

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            BastionNotification.error(sender, "This command is exclusive to players.");
            return;
        }

        BastionProfile bastionPlayer = BastionProfile.registry.getPlayer(player.getUniqueId());

        Nation nation = bastionPlayer.getNation();

        if (nation == null) {
            BastionNotification.error(player, "You aren't in a nation.");
            return;
        }

        if (nation.getAuthority(bastionPlayer) != Authority.OWNER) {
            BastionNotification.error(player, "Your authority is insufficient.");
        }

        OfflinePlayer subject = Bukkit.getOfflinePlayer(args[1]);

        if (!subject.hasPlayedBefore()){
            BastionNotification.error(player, "That player does not exist.");
            return;
        }

        if (!nation.isMember(subject.getUniqueId())){
            BastionNotification.error(player, "That player is not in your nation.");
            return;
        }

        BastionProfile bastionProfile = BastionProfile.registry.getPlayer(subject.getUniqueId());
        nation.removeMember(bastionProfile);

        BastionNotification.infoMessage(nation,
                Component.text(player.getName() + " kicked " + subject.getName() + " from your nation.")
        );
        BastionNotification.success(player, subject.getName() + " was kicked from your nation.");


        if (subject.isOnline()){
            BastionNotification.infoMessage((Player) subject, Component.empty().append(
                    Component.text("You were kicked from ").append(
                            Component.text(nation.name).color(NamedTextColor.YELLOW)).append(
                            Component.text(".")
                    )));
        }
    }
}
