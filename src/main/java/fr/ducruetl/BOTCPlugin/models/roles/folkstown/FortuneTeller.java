package fr.ducruetl.BOTCPlugin.models.roles.folkstown;

import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class FortuneTeller extends Role {

    public FortuneTeller() {
        super(
            "Voyante",
            "Chaque nuit, tu choisis 2 joueurs, tu recevra l'information de si "
            + "il y a le diablotin parmis eux ou non. Attention, il y a également un "
            + "citoyen dans la partie que tu verra aussi comme diablotin.",
            Team.TOWNSFOLK
        );
    }
    
}
