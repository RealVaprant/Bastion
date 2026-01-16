package net.vaprant.bastion.command.subcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.vaprant.bastion.command.NationTabCompleter;
import net.vaprant.bastion.nation.permission.Authority;
import net.vaprant.bastion.nation.Nation;
import net.vaprant.bastion.nation.permission.PermissionNode;
import net.vaprant.bastion.player.BastionProfile;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

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

        if (!bastionPlayer.hasPermission(PermissionNode.KICK)) {
            BastionNotification.error(player, "Your authority is insufficient.");
            return;
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

        if (subject.getUniqueId() == player.getUniqueId()){
            BastionNotification.error(player, "You cannot kick yourself.");
            return;
        }

        if (!bastionPlayer.getAuthority().isHigherThan(nation.getAuthority(subject.getUniqueId()))) {
            BastionNotification.error(player, "Your authority is insufficient.");
        }


        BastionProfile bastionProfile = BastionProfile.registry.getPlayer(subject.getUniqueId());
        nation.removeMember(bastionProfile);

        BastionNotification.infoMessage(nation,
                Component.text(player.getName() + " kicked " + subject.getName() + " from the nation.")
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

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            return List.of();
        }
        if (args.length == 2){
            BastionProfile profile = BastionProfile.registry.getPlayer(player.getUniqueId());
            Nation nation = profile.getNation();

            if (nation == null){
                return List.of();
            }

            return NationTabCompleter.filterCompletions(nation.getMembers().stream()
                    .map(OfflinePlayer::getName)
                    .filter(Objects::nonNull)
                    .toList(), args[1]);
        }


        return List.of();
    }

    @Override
    public boolean isAccessible(CommandSender sender) {
        if (!(sender instanceof Player player)){
            return false;
        }

        BastionProfile bastionProfile = BastionProfile.registry.getPlayer(player.getUniqueId());

        return (bastionProfile.getNation() != null && bastionProfile.hasPermission(PermissionNode.KICK));
    }
}
