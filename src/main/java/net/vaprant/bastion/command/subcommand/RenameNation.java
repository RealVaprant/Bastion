package net.vaprant.bastion.command.subcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.vaprant.bastion.nation.Authority;
import net.vaprant.bastion.nation.Nation;
import net.vaprant.bastion.player.BastionPlayer;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class RenameNation implements NationSubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            BastionNotification.error(sender, "You are not in a nation.");
            return;
        }

        BastionPlayer bastionPlayer = BastionPlayer.registry.getPlayer(player.getUniqueId());
        Nation nation = bastionPlayer.getNation();

        if (nation == null) {
            BastionNotification.error(player, "You are not in a nation.");
            return;
        }

        if (bastionPlayer.getNation().getAuthority(bastionPlayer) != Authority.OWNER) {
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
            BastionNotification.error(player, "Your nation already has this name.");
            return;
        }

        nation.name = args[1];


        BastionNotification.info(player, Component.empty().append(Component.text(
                "Your nation has been renamed to "
        ).append(
                Component.text(nation.name).color(NamedTextColor.YELLOW)
        )).append(Component.text(".")));

        BastionNotification.info(nation, Component.empty().append(Component.text(
                "Your nation has been renamed to "
        ).append(
                Component.text(nation.name).color(NamedTextColor.YELLOW)
        )).append(Component.text(".")));
    }
}
