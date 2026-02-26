package fr.ducruetl.BOTCPlugin.gameobjects.roles.demons;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;
import fr.ducruetl.BOTCPlugin.items.CustomItems;

public class Spy extends Role {

    public Spy() {
        super(
            "Espion",
            "Lors de la première nuit, tu pourra voir les rôles de tout le monde. "
            + "De plus, tu aura un rôle Citoyen/Etranger de façade, les rôles à informations "
            + "te verront donc comme ce rôle au lieu d'Espion.",
            Team.MINION,
            2
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        if (game.getDay() != 1) {
            NightActions.processNextNightAction(game);
            return;
        }

        player.getPlayer().closeInventory();
        player.getPlayer().openBook(CustomItems.getRolesBook(game));

        BukkitTask timer = game.getPlugin().startTimer(
            "Votre tour", 
            game.getPlugin().getConfig().getInt("timers.nightTimeDuration"),
            new ArrayList<GamePlayer>(Arrays.asList(player))
        );
        
        new BukkitRunnable() {
            double timeRemainingInSeconds = (double) game.getPlugin().getConfig().getInt("timers.nightTimeDuration");

            @Override
            public void run() {
                if (timeRemainingInSeconds <= 0) {
                    this.cancel();
                    timer.cancel();
                    player.getPlayer().closeInventory();
                    
                    NightActions.processNextNightAction(game);
                    return;
                }

                timeRemainingInSeconds--;
            }
        }.runTaskTimer(game.getPlugin(), 0, Math.round(Bukkit.getServerTickManager().getTickRate()));
    }
    
}
