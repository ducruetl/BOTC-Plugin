package fr.ducruetl.BOTCPlugin.models.roles.folkstown;

import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class Virgin extends Role {

    public Virgin() {
        super(
            "Vièrge",
            "La première fois que tu est nommé, si la personne qui te nomme "
            + "appartient aux Citoyens, cette personne mourrera.",
            Team.TOWNSFOLK
        );
    }
    
}
