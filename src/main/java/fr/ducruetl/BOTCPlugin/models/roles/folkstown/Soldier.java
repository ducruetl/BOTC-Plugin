package fr.ducruetl.BOTCPlugin.models.roles.folkstown;

import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class Soldier extends Role {

    public Soldier() {
        super(
            "Soldat",
            "Tu ne peut par mourir par le diablotin pendant la nuit.",
            Team.TOWNSFOLK
        );
    }
    
}
