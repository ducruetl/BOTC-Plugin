package fr.ducruetl.BOTCPlugin.models.roles.folkstown;

import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class Librarian extends Role {

    public Librarian() {
        super(
            "Libraire",
            "Au début de la partie, tu connaitras un rôle étranger "
            + "et 2 personnes, une de ces personnes possède ce rôle.",
            Team.TOWNSFOLK
        );
    }
    
}
