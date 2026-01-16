package net.vaprant.bastion.nation.permission;

import java.util.Set;

public enum Authority {
    OWNER(4),
    ADMIN(3),
    OFFICER(2),
    MEMBER(1);

    private final int rank;

    Authority(int rank) {
        this.rank = rank;
    }

    public Set<PermissionNode> getDefaultPermissions() {
        return switch (this){
            case OWNER, ADMIN -> Set.of(PermissionNode.RENAME, PermissionNode.KICK, PermissionNode.INVITE, PermissionNode.MANAGE_PERMISSIONS, PermissionNode.MANAGE_AUTHORITY);
            case OFFICER -> Set.of(PermissionNode.KICK, PermissionNode.INVITE);
            case MEMBER -> Set.of();
        };
    }

    public boolean isHigherThan(Authority authority) {
        return rank > authority.rank;
    }


}
