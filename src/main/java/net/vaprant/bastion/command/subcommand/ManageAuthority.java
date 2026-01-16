package net.vaprant.bastion.command.subcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.vaprant.bastion.command.NationTabCompleter;
import net.vaprant.bastion.nation.Nation;
import net.vaprant.bastion.nation.permission.Authority;
import net.vaprant.bastion.nation.permission.PermissionNode;
import net.vaprant.bastion.player.BastionProfile;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static net.vaprant.bastion.command.NationTabCompleter.filterCompletions;

public class ManageAuthority implements NationSubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            BastionNotification.error(sender, "This command is exclusive to players.");
            return;
        }

        BastionProfile bastionProfile = BastionProfile.registry.getPlayer(player.getUniqueId());
        Nation nation = bastionProfile.getNation();

        if (nation == null) {
            BastionNotification.error(player, "You aren't in a nation.");
            return;
        }

        if (!bastionProfile.hasPermission(PermissionNode.MANAGE_AUTHORITY)){
            BastionNotification.error(player, "Your authority is insufficient.");
            return;
        }

        if (args.length == 1){
            BastionNotification.error(player, "Select a player.");
            return;
        }

        OfflinePlayer subject = Bukkit.getOfflinePlayer(args[1]);

        if (!nation.isMember(subject.getUniqueId())) {
            BastionNotification.error(player, "That player is not a member of your nation.");
            return;
        }

        BastionProfile subjectProfile = BastionProfile.registry.getPlayer(subject.getUniqueId());

        if (nation.isOwner(bastionProfile.uuid)) {
            BastionNotification.error(player, "Owners cannot manage their own authority.");
            return;
        }

        if (!bastionProfile.getAuthority().isHigherThan(subjectProfile.getAuthority())) {
            BastionNotification.error(player, "Your authority is insufficient.");
            return;
        }

        if (args.length == 2){
            BastionNotification.info(player, Component.empty().append(
                    Component.text(subject.getName() + "'s authority is ").append(
                            Component.text(subjectProfile.getAuthority().toString()).color(NamedTextColor.YELLOW)
                    )
            ));
            return;
        }




        if (args.length == 3) {

            if (!isValidAuthority(args[2])) {
                BastionNotification.error(player, "That type of authority doesn't exist.");
                return;
            }

            //TODO: use a getAuthority(String) and check if it's null, rather than parsing the args twice for the same data.
            Authority authority = null;
            for (Authority a : Authority.values()) {
                if (Objects.equals(a.toString(), args[1])) {
                    authority = a;
                }
            }
            assert authority != null;

            if (!bastionProfile.getAuthority().isHigherThan(authority)) {
                BastionNotification.error(player, "Your authority is insufficient.");
                return;
            }

            nation.setAuthority(bastionProfile.uuid, authority);

            BastionNotification.info(player, Component.empty().append(
                    Component.text(subject.getName() + "'s authority is now ").append(
                            Component.text(authority.toString()).color(NamedTextColor.YELLOW)
                    )
            ));


            BastionNotification.infoMessage(nation, Component.empty().append(
                    Component.text(player.getName() + " set " + subject.getName() + "'s authority to ").append(
                            Component.text(authority.toString()).color(NamedTextColor.YELLOW)
                    )
            ));

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

        if (args.length == 3){
            return filterCompletions(Arrays.stream(Authority.values())
                    .filter(authority -> authority != Authority.OWNER)
                    .map(Enum::toString).toList(), args[2]);
        }


        return List.of();
    }

    private boolean isValidAuthority(String string){
        for (Authority authority : Authority.values()) {
            if (Objects.equals(authority.toString(), string) && authority != Authority.OWNER) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAccessible(CommandSender sender) {
        if (!(sender instanceof Player player)){
            return false;
        }

        BastionProfile bastionProfile = BastionProfile.registry.getPlayer(player.getUniqueId());

        return (bastionProfile.getNation() != null && bastionProfile.hasPermission(PermissionNode.MANAGE_AUTHORITY));
    }

}
