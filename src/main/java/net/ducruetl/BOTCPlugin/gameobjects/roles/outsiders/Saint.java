package net.ducruetl.BOTCPlugin.gameobjects.roles.outsiders;

import net.ducruetl.BOTCPlugin.gameobjects.Team;
import net.ducruetl.BOTCPlugin.gameobjects.roles.Role;

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
