package fr.ducruetl.BOTCPlugin.models.roles.demons;

import fr.ducruetl.BOTCPlugin.models.Game;
import fr.ducruetl.BOTCPlugin.models.GamePlayer;
import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class ScarletWoman extends Role {

    public ScarletWoman() {
        super(
            "Femme Ecarlate",
            "Si il reste 5 joueurs ou plus, et que le diablotin "
            + "meurt lors du vote du village, tu prend sa place en tant que diablotin.",
            Team.MINION,
            0
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        game.processNextNightAction();
    }
    
}
