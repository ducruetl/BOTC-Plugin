package fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown;

import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

public class Slayer extends Role {

    public Slayer() {
        super(
            "Pourfendeur",
            "Une fois dans la partie, tu peut choisir de tirer sur quelqu'un, "
            + "si cette personne est diablotin, elle meurt.",
            Team.TOWNSFOLK,
            0
        );
    }
    
}
