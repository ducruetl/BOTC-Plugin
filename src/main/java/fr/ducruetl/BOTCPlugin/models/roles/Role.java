package fr.ducruetl.BOTCPlugin.models.roles;

import fr.ducruetl.BOTCPlugin.models.Game;
import fr.ducruetl.BOTCPlugin.models.GamePlayer;
import fr.ducruetl.BOTCPlugin.models.Team;

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

    public void onNightTurn(Game game, GamePlayer player) {
        game.processNextNightAction();
    }
}
