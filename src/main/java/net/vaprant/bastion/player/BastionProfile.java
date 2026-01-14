package net.vaprant.bastion.player;

import net.vaprant.bastion.nation.Nation;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class BastionProfile {

    public static final BastionProfileRegistry registry = new BastionProfileRegistry();


    public final UUID uuid;
    private Nation nation;

    public BastionProfile(UUID uuid, Nation nation) {
        this.uuid = uuid;
        this.nation = nation;
    }

    public BastionProfile(UUID playerId) {
        this.uuid = playerId;
        this.nation = Nation.registry.getNationByMember(playerId);

    }



    public OfflinePlayer getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    public boolean isOnline() {
        return BastionProfile.registry.containsPlayer(this.uuid);
    }


}
