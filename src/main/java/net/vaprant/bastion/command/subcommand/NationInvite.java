package net.vaprant.bastion.command.subcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.vaprant.bastion.Bastion;
import net.vaprant.bastion.nation.Authority;
import net.vaprant.bastion.nation.Nation;
import net.vaprant.bastion.player.BastionProfile;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class NationInvite implements NationSubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            BastionNotification.error(sender, "You are not in a nation.");
            return;
        }

        BastionProfile bastionPlayer = BastionProfile.registry.getPlayer(player.getUniqueId());
        Nation nation = bastionPlayer.getNation();

        if (nation == null){
            BastionNotification.error(player, "You are not in a nation.");
            return;
        }

        //TODO: Create a permission system.
        if (nation.getAuthority(bastionPlayer) != Authority.OWNER) {
            BastionNotification.error(player, "Your authority is insufficient.");
        }


        OfflinePlayer recipient = Bukkit.getOfflinePlayer(args[1]);

        if (!recipient.hasPlayedBefore()){
            BastionNotification.error(player, "That player does not exist.");
            return;
        }

        if (!recipient.isOnline()){
            BastionNotification.error(player, "That player is not online.");
            return;
        }

        UUID recipientId = recipient.getUniqueId();

        if (nation.isMember(recipientId)) {
            BastionNotification.error(player, "That player is already in your nation.");
            return;
        }

        if (nation.isInvited(recipientId)) {
            nation.removeInvitation(recipientId);

            BastionNotification.infoMessage((Player) recipient, Component.text(
                            player.getName() + " revoked your invitation"));


            BastionNotification.infoMessage(nation,
                    Component.text(
                            player.getName() +
                                    " revoked " +
                                    recipient.getName() +
                                    "'s invitation."
                    ));
            return;
        }


        int inviteTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(
                Bastion.getInstance(),
                () -> {
                    if (nation.isInvited(recipientId)){
                        nation.removeInvitation(recipientId);

                        BastionNotification.infoMessage((Player) recipient, Component.empty().append(
                                Component.text("Your invite to ").append(
                                        Component.text(nation.name).color(NamedTextColor.YELLOW)
                                )
                        ).append(Component.text(" has expired.")));
                    }
                }, 1200L // 1-minute delay.


        );

        Integer joinRequestTask = nation.getJoinRequsetTask(recipientId);

        if (joinRequestTask != null) {
            Bukkit.getScheduler().cancelTask(joinRequestTask);
            nation.removeJoinRequest(recipientId);
        }
        nation.addInvitation(recipientId, inviteTaskId);


        BastionNotification.infoMessage((Player) recipient, Component.text(
                player.getName() + " invited you to join "

        ).append(
                Component.text(nation.name).color(NamedTextColor.YELLOW)
        ).decorate(TextDecoration.UNDERLINED)
                .hoverEvent(
                        Component.text("Click to accept!").color(NamedTextColor.GREEN)
                )
                .clickEvent(
                        ClickEvent.runCommand("nation join " + nation.name)
                ));


        BastionNotification.infoMessage(nation,
                Component.text(
                    player.getName() +
                    " invited " +
                    recipient.getName() +
                    " to join your nation."
                ));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
    }
}
