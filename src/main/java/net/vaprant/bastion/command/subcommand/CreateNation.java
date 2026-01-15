package net.vaprant.bastion.command.subcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.vaprant.bastion.nation.permission.Authority;
import net.vaprant.bastion.nation.Nation;
import net.vaprant.bastion.player.BastionProfile;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateNation implements NationSubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {

        BastionProfile bastionPlayer = null;

        if (sender instanceof Player player) {

            bastionPlayer = BastionProfile.registry.getPlayer(player.getUniqueId());

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
