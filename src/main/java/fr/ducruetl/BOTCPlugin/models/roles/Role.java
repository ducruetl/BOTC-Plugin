package fr.ducruetl.BOTCPlugin.models.roles;

import fr.ducruetl.BOTCPlugin.models.Team;

public class Role {
    
    private String name;

    private String description;

    private Team team;

    public Role(String name, String description, Team team) {
        this.name = name;
        this.description = description;
        this.team = team;
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
}
