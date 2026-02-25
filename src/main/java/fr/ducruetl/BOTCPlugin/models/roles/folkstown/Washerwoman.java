package fr.ducruetl.BOTCPlugin.models.roles.folkstown;

import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class Washerwoman extends Role {

    public Washerwoman() {
        super(
            "Lavendière",
            "Au début de la partie, tu connaitras un rôle citoyen et 2 personnes, "
            + "une de ces personnes possède ce rôle.",
            Team.TOWNSFOLK
        );
    }
    
}
