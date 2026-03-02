package fr.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks;

import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

public class Soldier extends Role {

    public Soldier() {
        super(
            "Soldat",
            "Tu ne peut par mourir par le diablotin pendant la nuit.",
            Team.TOWNSFOLK,
            0
        );
    }
    
}
