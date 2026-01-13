package net.vaprant.bastion.command.subcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.vaprant.bastion.nation.Authority;
import net.vaprant.bastion.nation.Nation;
import net.vaprant.bastion.player.BastionPlayer;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisbandNation implements NationSubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            BastionNotification.error(sender, "This command is for players. Please use /nation admin disband.");
            return;
        }

        BastionPlayer bastionPlayer = BastionPlayer.registry.getPlayer(player.getUniqueId());

        Nation nation = bastionPlayer.getNation();

        if (nation == null) {
            BastionNotification.error(player, "You aren't in a nation.");
            return;
        }

        Authority authority = nation.getAuthority(bastionPlayer);

        if (authority == Authority.OWNER) {
            nation.disband();
            BastionNotification.broadcast(Component.empty().append(
                    Component.text(nation.name).color(NamedTextColor.YELLOW)).append(
                    Component.text(" has disbanded.")
                    ));
        }
        else {
            BastionNotification.error(player, "Your authority is insufficient.");
        }
    }
}
