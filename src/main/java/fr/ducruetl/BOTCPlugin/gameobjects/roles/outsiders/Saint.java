package fr.ducruetl.BOTCPlugin.gameobjects.roles.outsiders;

import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

public class Saint extends Role {

    public Saint() {
        super(
            "Saint",
            "Si tu meurt par vote du village, les Citoyens perdent la partie.",
            Team.OUTSIDER,
            0
        );
    }
    
}
