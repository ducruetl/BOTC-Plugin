package fr.ducruetl.BOTCPlugin.models.roles.folkstown;

import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class Undertaker extends Role {

    public Undertaker() {
        super(
            "Fossoyeur",
            "Lorsque quelqu'un est tué lors du vote du village, tu apprend son rôle la nuit suivante.",
            Team.TOWNSFOLK
        );
    }
    
}
