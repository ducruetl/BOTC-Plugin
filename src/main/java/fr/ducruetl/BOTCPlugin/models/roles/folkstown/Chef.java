package fr.ducruetl.BOTCPlugin.models.roles.folkstown;

import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class Chef extends Role {

    public Chef() {
        super(
            "Chef",
            "Au début de la partie, tu recevra le nombre de paires de démons "
            + "qu'il y a autour de la table, 1 paire signifie qu'il y a 2 démons côte à côte.",
            Team.TOWNSFOLK
        );
    }
    
}
