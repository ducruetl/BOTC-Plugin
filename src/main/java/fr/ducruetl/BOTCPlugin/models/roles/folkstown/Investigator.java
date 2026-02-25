package fr.ducruetl.BOTCPlugin.models.roles.folkstown;

import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class Investigator extends Role {

    public Investigator() {
        super(
            "Détective",
            "Au début de la partie, tu connaitras un rôle démon et 2 personnes, "
            + "une de ces personnes possède ce rôle.",
            Team.TOWNSFOLK
        );
    }
    
}
