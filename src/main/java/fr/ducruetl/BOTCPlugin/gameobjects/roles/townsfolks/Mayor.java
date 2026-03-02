package fr.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks;

import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

public class Mayor extends Role {

    public Mayor() {
        super(
            "Maire",
            "Si il reste 3 personne en vie, que tu en fait partie, et que personne "
            + "ne meurt pendant ce tour, les Citoyens remportent la partie. De plus, "
            + "si le diablotin tente de te tuer la nuit, il y a 1/2 que quelqu'un d'autre meurt à ta place.",
            Team.TOWNSFOLK,
            0
        );
    }

}
