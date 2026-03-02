package fr.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks;

import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

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
