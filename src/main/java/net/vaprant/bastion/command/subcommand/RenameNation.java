package net.vaprant.bastion.command.subcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.vaprant.bastion.nation.permission.Authority;
import net.vaprant.bastion.nation.Nation;
import net.vaprant.bastion.nation.permission.PermissionNode;
import net.vaprant.bastion.player.BastionProfile;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class RenameNation implements NationSubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            BastionNotification.error(sender, "This command is exclusive to players.");
            return;
        }

        BastionProfile bastionPlayer = BastionProfile.registry.getPlayer(player.getUniqueId());
        Nation nation = bastionPlayer.getNation();

        if (nation == null) {
            BastionNotification.error(player, "You are not in a nation.");
            return;
        }

        if (nation.hasPermission(bastionPlayer.uuid, PermissionNode.RENAME)) {
            BastionNotification.error(player, "Your authority is insufficient.");
            return;
        }

        //TODO: naming constraints.
        if (args.length == 1) {
            BastionNotification.error(player, "bro, set a name please.");
            return;
        }

        if (args.length > 2) {
            BastionNotification.error(player, "Spaces?? come on..");
            return;
        }

        if (Objects.equals(args[1], nation.name)) {
            BastionNotification.error(player, "Your nation already has that name.");
            return;
        }

        if (Nation.registry.isNation(args[1])) {
            BastionNotification.error(player, "Another nation already has that name.");
            return;
        }

        nation.rename(args[1]);

        BastionNotification.infoMessage(nation, Component.empty().append(Component.text(
                player.getName() + " renamed your nation to "
        ).append(
                Component.text(nation.name).color(NamedTextColor.YELLOW)
        )).append(Component.text(".")));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            return List.of();
        }

        Nation nation = BastionProfile.registry.getPlayer(player.getUniqueId()).getNation();;
        if (nation != null) {
            return List.of(nation.name);
        }
        return List.of();
    }
}
