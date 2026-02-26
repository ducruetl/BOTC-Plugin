package fr.ducruetl.BOTCPlugin.gameobjects.roles.demons;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

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
            Team.DEMON,
            3
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        NightActions.processNextNightAction(game);
    }
    
}
