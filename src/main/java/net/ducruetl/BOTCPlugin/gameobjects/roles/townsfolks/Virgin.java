package net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks;

import net.ducruetl.BOTCPlugin.gameobjects.Team;
import net.ducruetl.BOTCPlugin.gameobjects.roles.Role;

public class Virgin extends Role {

    public Virgin() {
        super(
            "Vièrge",
            "La première fois que tu est nommé, si la personne qui te nomme "
            + "appartient aux Citoyens, cette personne mourrera.",
            Team.TOWNSFOLK,
            0
        );
    }
    
}
