package net.vaprant.bastion.nation.permission;

import java.util.Set;

public enum Authority {
    OWNER,
    OFFICER,
    MEMBER;

    public Set<PermissionNode> getDefaultPermissions() {
        return switch (this){
            case OWNER -> Set.of(PermissionNode.RENAME, PermissionNode.KICK, PermissionNode.INVITE);
            case OFFICER -> Set.of(PermissionNode.KICK, PermissionNode.INVITE);
            case MEMBER -> Set.of();
        };
    }

}
