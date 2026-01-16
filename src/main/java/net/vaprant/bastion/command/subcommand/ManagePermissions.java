package net.vaprant.bastion.command.subcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.vaprant.bastion.nation.Nation;
import net.vaprant.bastion.nation.permission.Authority;
import net.vaprant.bastion.nation.permission.PermissionNode;
import net.vaprant.bastion.player.BastionProfile;
import net.vaprant.bastion.util.BastionNotification;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static net.vaprant.bastion.command.NationTabCompleter.filterCompletions;

public class ManagePermissions implements NationSubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)){
            BastionNotification.error(sender, "This command is exclusive to players.");
            return;
        }

        BastionProfile bastionProfile = BastionProfile.registry.getPlayer(player.getUniqueId());
        Nation nation = bastionProfile.getNation();

        if (nation == null) {
            BastionNotification.error(player, "You aren't in a nation.");
            return;
        }

        if (!(bastionProfile.hasPermission(PermissionNode.MANAGE_PERMISSIONS))) {
            BastionNotification.error(player, "Your authority is insufficient.");
            return;
        }


        if (args.length == 1) {
            BastionNotification.error(player, "No type of authority was selected.");
            return;
        }

        if (!isValidAuthority(args[1])) {
            BastionNotification.error(player, "That type of authority does not exist.");
            return;
        }


        if (args.length == 2) {
            BastionNotification.error(player, "No permission was selected.");
            return;
        }

        if (!isValidPermissionNode(args[2])) {
            BastionNotification.error(player, "That permission doesn't exist.");
            return;
        }


        Authority authority = null;
        for (Authority a : Authority.values()) {
            if (Objects.equals(a.toString(), args[1])) {
                authority = a;
            }
        }
        assert authority != null;

        PermissionNode permissionNode = null;
        for (PermissionNode p : PermissionNode.values()) {
            if (Objects.equals(p.toString(), args[2])) {
                permissionNode = p;
            }
        }
        assert permissionNode != null;

        if (!bastionProfile.getAuthority().isHigherThan(authority)) {
            BastionNotification.error(player, "Your authority is insufficient.");
            return;
        }

        if (args.length == 3) {

            BastionNotification.info(player, Component.empty().append(
                            Component.text(args[2]).color(NamedTextColor.YELLOW)
                    ).append(Component.text(" is set to ")).append(
                            nation.hasPermission(authority, permissionNode) ?
                                    Component.text("ALLOW").color(NamedTextColor.GREEN)
                                    :
                                    Component.text("DENY").color(NamedTextColor.RED)
                    ).append(Component.text("."))
            );
            return;
        }

        boolean isPermitted = Objects.equals(args[3], "ALLOW");


        if (args.length == 4){


            nation.setPermission(authority, permissionNode, isPermitted);


            BastionNotification.info(player, Component.empty().append(
                            Component.text(args[2]).color(NamedTextColor.YELLOW)
                    ).append(Component.text(" has been set to ")).append(
                            isPermitted ?
                                    Component.text("ALLOW").color(NamedTextColor.GREEN)
                                    :
                                    Component.text("DENY").color(NamedTextColor.RED)
                    ).append(Component.text("."))
            );
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {

        return switch (args.length) {
            case 2 -> filterCompletions(Arrays.stream(Authority.values())
                    .filter(authority -> authority != Authority.OWNER)
                    .map(Enum::toString).toList(), args[1]);
            case 3 -> {
                if (isValidAuthority(args[1])) {
                    yield  filterCompletions(Arrays.stream(PermissionNode.values())
                            .filter(permissionNode ->
                                    permissionNode != PermissionNode.MANAGE_PERMISSIONS &&
                                    permissionNode != PermissionNode.MANAGE_AUTHORITY)
                            .map(Enum::toString)
                            .toList(), args[2]);
                }
                yield List.of();
            }

            case 4 -> {
                if (isValidPermissionNode(args[2])) {
                    yield filterCompletions(List.of("ALLOW", "DENY"), args[3]);
                }
                yield List.of();
            }
            default -> List.of();
        };
    }

    private boolean isValidPermissionNode(String string){
        for (PermissionNode permissionNode : PermissionNode.values()) {
            if (Objects.equals(permissionNode.toString(), string) &&
                    permissionNode != PermissionNode.MANAGE_PERMISSIONS &&
                    permissionNode != PermissionNode.MANAGE_AUTHORITY
            ) {
                return true;
            }
        }
        return false;
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

        return (bastionProfile.getNation() != null && bastionProfile.hasPermission(PermissionNode.MANAGE_PERMISSIONS));
    }

}
