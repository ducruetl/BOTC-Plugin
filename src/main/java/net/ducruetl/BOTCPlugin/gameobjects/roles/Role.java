package net.ducruetl.BOTCPlugin.gameobjects.roles;

import net.ducruetl.BOTCPlugin.gameobjects.Game;
import net.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import net.ducruetl.BOTCPlugin.gameobjects.NightActions;
import net.ducruetl.BOTCPlugin.gameobjects.Team;

public class Role {
    
    private String name;

    private String description;

    private Team team;

    private int nightPriority;

    public Role(String name, String description, Team team, int nightPriority) {
        this.name = name;
        this.description = description;
        this.team = team;
        this.nightPriority = nightPriority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getNightPriority() {
        return nightPriority;
    }

    public void setNightPriority(int nightPriority) {
        this.nightPriority = nightPriority;
    }

    /**
     * Do the night actions related to the role
     * @param game The related game
     * @param player The player that has the role
     */
    public void onNightTurn(Game game, GamePlayer player) {
        NightActions.processNextNightAction(game);
    }
}
