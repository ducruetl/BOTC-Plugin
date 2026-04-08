package net.ducruetl.BOTCPlugin.gameobjects.roles.outsiders;

import net.ducruetl.BOTCPlugin.gameobjects.Team;
import net.ducruetl.BOTCPlugin.gameobjects.roles.Role;

public class Drunk extends Role {

    public Drunk() {
        super(
            "Ivrogne",
            "Tu ne sait pas que tu es ivrogne, au début de la partie, "
            + "un rôle t'es assigné mais tu n'auras pas ses pouvoirs, "
            + "si ton rôle attribué est censé recevoir des informations, "
            + "tu recevra des informations aléatoires.",
            Team.OUTSIDER,
            0
        );
    }
}