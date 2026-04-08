package net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks;

import net.ducruetl.BOTCPlugin.gameobjects.Team;
import net.ducruetl.BOTCPlugin.gameobjects.roles.Role;

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
