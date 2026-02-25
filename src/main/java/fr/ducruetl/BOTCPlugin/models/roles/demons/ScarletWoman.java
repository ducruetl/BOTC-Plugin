package fr.ducruetl.BOTCPlugin.models.roles.demons;

import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class ScarletWoman extends Role {

    public ScarletWoman() {
        super(
            "Femme Ecarlate",
            "Si il reste 5 joueurs ou plus, et que le diablotin "
            + "meurt lors du vote du village, tu prend sa place en tant que diablotin.",
            Team.MINION
        );
    }
    
}
