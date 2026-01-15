package net.vaprant.bastion.command.subcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.vaprant.bastion.Bastion;
import net.vaprant.bastion.command.NationTabCompleter;
import net.vaprant.bastion.nation.Authority;
import net.vaprant.bastion.nation.Nation;
import net.vaprant.bastion.player.BastionProfile;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class JoinNation implements NationSubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player player)) {
            BastionNotification.error(sender, "This command is exclusive to players.");
            return;
        }

        BastionProfile bastionPlayer = BastionProfile.registry.getPlayer(player.getUniqueId());

        if (bastionPlayer.getNation() != null) {
            BastionNotification.error(player, "You belong to another nation right now.");
            return;
        }

        Nation nation = Nation.registry.getNation(args[1]);

        if (nation == null){
            BastionNotification.error(player, "That nation does not exist.");
            return;
        }

        if (!nation.isInvited(bastionPlayer.uuid)) {
            if (nation.getOwner().isOnline()) {

                if (nation.isRequestingJoin(bastionPlayer.uuid)){
                    BastionNotification.error(player, "You already have an ongoing join request.");
                    return;
                }

                int joinRequestTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(
                        Bastion.getInstance(),
                        () -> {
                            if (nation.isRequestingJoin(bastionPlayer.uuid)){
                                nation.removeJoinRequest(bastionPlayer.uuid);

                                BastionNotification.info(player, Component.empty().append(
                                        Component.text("Your join request to ").append(
                                                Component.text(nation.name).color(NamedTextColor.YELLOW)
                                        )
                                ).append(Component.text(" has expired.")));
                            }
                        }, 1200L // 1-minute delay.


                );
                nation.addJoinRequest(bastionPlayer.uuid, joinRequestTaskId);

                // Nation owner's notification of the join request.
                BastionNotification.infoMessage((Player) nation.getOwner().getPlayer(), Component.text(
                                player.getName() + " requested to join "

                        ).append(
                                Component.text(nation.name).color(NamedTextColor.YELLOW)
                        ).decorate(TextDecoration.UNDERLINED)
                        .hoverEvent(
                                Component.text("Click to invite!").color(NamedTextColor.GREEN)
                        )
                        .clickEvent(
                                ClickEvent.runCommand("nation invite " + player.getName())
                        ));

                // Command executor's notification that the request was successfully sent.
                BastionNotification.infoMessage(player, Component.text(
                        "You sent a join request to ").append(
                                Component.text(nation.name).color(NamedTextColor.YELLOW)).append(
                                        Component.text(".")
                        )
                );
            }
            else {
                BastionNotification.error(player, "No authorities were available to receive your request.");
            }
        }
        else {



            Integer joinRequestTask = nation.getJoinRequsetTask(bastionPlayer.uuid);
            Integer inviteTask = nation.getInviteTask(bastionPlayer.uuid);

            if (joinRequestTask != null) {
                Bukkit.getScheduler().cancelTask(joinRequestTask);
                nation.removeJoinRequest(bastionPlayer.uuid);
            }

            if (inviteTask != null) {
                Bukkit.getScheduler().cancelTask(inviteTask);
                nation.removeInvitation(bastionPlayer.uuid);

            }

            BastionNotification.infoMessage(nation, Component.text(
                    player.getName() + " joined your nation."
            ));

            nation.addMember(bastionPlayer, Authority.MEMBER);

            BastionNotification.infoMessage(player, Component.text("You joined the nation."
            ));
        }




    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return NationTabCompleter.filterCompletions(Nation.registry.getAllNationNames(), args[1]);
        }
        return List.of();
    }
}
