package fr.ducruetl.BOTCPlugin.models.roles.outsiders;

import fr.ducruetl.BOTCPlugin.models.Game;
import fr.ducruetl.BOTCPlugin.models.GamePlayer;
import fr.ducruetl.BOTCPlugin.models.Team;
import fr.ducruetl.BOTCPlugin.models.roles.Role;

public class Saint extends Role {

    public Saint() {
        super(
            "Saint",
            "Si tu meurt par vote du village, les Citoyens perdent la partie.",
            Team.OUTSIDER,
            0
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        game.processNextNightAction();
    }
    
}
