package net.vaprant.bastion.command.subcommand;

import net.vaprant.bastion.nation.Authority;
import net.vaprant.bastion.nation.Nation;
import net.vaprant.bastion.player.BastionPlayer;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CreateNation implements NationSubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof Player player) {
            BastionPlayer.registry.getPlayer(player.getUniqueId());
        }


        //TODO: Add name constraints
        if (args.length == 2) {
            BastionNotification.error(sender, "bro, set a name please.");
            return;
        }

        if (args.length > 3) {
            BastionNotification.error(sender, "Spaces?? come on..");
            return;
        }

        UUID uuid = UUID.randomUUID();
        String name = args[2];
        HashMap<UUID, Authority> members = new HashMap<>();

        if (sender instanceof Player player) {

            members.put(player.getUniqueId(), Authority.OWNER);

            BastionNotification.success(player, "Your nation has started!");
        }
        else {
            BastionNotification.warning(sender, "An empty nation has been created. Please add people.");
        }

        Nation nation = new Nation(uuid, name, members);
        Nation.registry.addNation(nation);


        //TODO: Serialize Nation, and save it to disk
    }
}
