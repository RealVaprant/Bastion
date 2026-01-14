package net.vaprant.bastion.command.subcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.ARGBLike;
import net.vaprant.bastion.nation.Authority;
import net.vaprant.bastion.nation.Nation;
import net.vaprant.bastion.player.BastionPlayer;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Named;
import java.util.HashMap;
import java.util.UUID;

public class CreateNation implements NationSubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {

        BastionPlayer bastionPlayer = null;

        if (sender instanceof Player player) {

            bastionPlayer = BastionPlayer.registry.getPlayer(player.getUniqueId());

            if (bastionPlayer.getNation() != null) {
                BastionNotification.error(sender, "You're already in a nation.");
                return;
            }

        }



        //TODO: Add name constraints
        if (args.length == 1) {
            BastionNotification.error(sender, "bro, set a name please.");
            return;
        }

        if (args.length > 2) {
            BastionNotification.error(sender, "Spaces?? come on..");
            return;
        }

        String nationName = args[1];


        //TODO: Serialize Nation, and save it to disk
        Nation nation = new Nation(nationName, null);
        Nation.registry.addNation(nation);

        Bukkit.broadcast(
                Component.empty().color(TextColor.color(0xA5FF))
                        .append(Component.text(nationName).color(NamedTextColor.YELLOW))
                        .append(Component.text(" has been declared a nation!"))
        );


        if (sender instanceof Player player) {

            nation.addMember(bastionPlayer, Authority.OWNER);
            nation.setOwner(bastionPlayer);
            BastionNotification.success(player, "Your nation has started!");

        }
        else {
            //TODO: update the message to provide instructions on how to add members to nations from console.
            BastionNotification.warning(sender, "An empty nation has been created.");
        }



    }
}
