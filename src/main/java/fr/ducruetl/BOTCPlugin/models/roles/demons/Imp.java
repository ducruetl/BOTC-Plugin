package fr.ducruetl.BOTCPlugin.models.roles.demons;

import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class Imp extends Role {

    public Imp() {
        super(
            "Diablotin", 
            "Chaque nuit, tu choisi une cible à éliminer, " 
            + "tu peut te choisir toi-même, ce qui aura pour " 
            + "conséquence de te tuer et de transférer ton "
            + "rôle de diablotin à un de tes sbires démons. "
            + "Si tu meurt lors du vote du village, les Citoyens "
            + "gagnent (sauf si il y a une Femme Ecarlate).", 
            Team.DEMON
        );
    }
    
}
