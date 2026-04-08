package net.ducruetl.BOTCPlugin.gameobjects;

import org.bukkit.entity.Player;

import net.ducruetl.BOTCPlugin.gameobjects.roles.Role;
import net.ducruetl.BOTCPlugin.items.CustomItems;

public class GamePlayer {

    private Player player;

    private Role role;

    private Role facadeRole;

    private Team facadeTeam;

    private boolean isDead;

    private boolean isPoisoned;

    private boolean isProtected;

    private boolean hasVotedAfterDeath;

    private boolean hasUsedSlayerPower;
    
    private boolean hasVoted;

    public GamePlayer(Player player) {
        this.player = player;
        this.role = null;
        this.facadeRole = null;
        this.facadeTeam = null;
        this.isDead = false;
        this.isPoisoned = false;
        this.isProtected = false;
        this.hasVotedAfterDeath = false;
        this.hasUsedSlayerPower = false;
        this.hasVoted = false;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getFacadeRole() {
        return facadeRole;
    }

    public void setFacadeRole(Role facadeRole) {
        this.facadeRole = facadeRole;
    }

    public Team getFacadeTeam() {
        return facadeTeam;
    }

    public void setFacadeTeam(Team facadeTeam) {
        this.facadeTeam = facadeTeam;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean isDead) {
        if (isDead) {
            getPlayer().getInventory().setHelmet(CustomItems.getDeathHelmet());    
        }

        this.isDead = isDead;
    }

    public boolean isPoisoned() {
        return this.isPoisoned;
    }

    public void setPoisoned(boolean isPoisoned) {
        this.isPoisoned = isPoisoned;
    }

    public boolean isProtected() {
        return this.isProtected;
    }

    public void setProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }

    public boolean getHasVotedAfterDeath() {
        return hasVotedAfterDeath;
    }

    public void setHasVotedAfterDeath(boolean hasVotedAfterDeath) {
        this.hasVotedAfterDeath = hasVotedAfterDeath;
    }

    public boolean getHasUsedSlayerPower() {
        return hasUsedSlayerPower;
    }

    public void setHasUsedSlayerPower(boolean hasUsedSlayerPower) {
        this.hasUsedSlayerPower = hasUsedSlayerPower;
    }

    public boolean getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    public boolean isSeenAsDemon() {
        if (getFacadeTeam() != null
        && (getFacadeTeam() == Team.DEMON
        || getFacadeTeam() == Team.MINION)) {
            return true;
        }

        if (getRole().getTeam() == Team.DEMON
        || getRole().getTeam() == Team.MINION) {
            return true;
        }

        return false;
    }

    public boolean isSeenAsImp() {
        if (getFacadeTeam() != null && getFacadeTeam() == Team.DEMON) {
            return true;
        }

        if (getRole().getTeam() == Team.DEMON) {
            return true;
        }

        return false;
    }
    
}
