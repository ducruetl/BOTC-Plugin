package fr.ducruetl.BOTCPlugin.gameobjects.roles.demons;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.gameobjects.NightActions;
import fr.ducruetl.BOTCPlugin.gameobjects.Team;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;

public class Poisoner extends Role {

    public Poisoner() {
        super(
            "Empoisonneur", 
            "Chaque nuit, tu peut empoisonner un joueur, ce qui a "
            + "pour effet de lui retirer son pouvoir jusqu'à la prochaine nuit. "
            + "Si il était censé recevoir une information, il en recevra une aléatoire.", 
            Team.MINION,
            1
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        if (game.getLastPoisoned() != null) {
            game.getLastPoisoned().setPoisoned(false);
            game.setLastPoisoned(null);
        }

        Inventory playerSelectInventory = game.createTargetSelectionInventory();
        player.getPlayer().closeInventory();
        player.getPlayer().openInventory(playerSelectInventory);

        BukkitTask timer = game.getPlugin().startTimer(
            "Votre tour", 
            game.getPlugin().getConfig().getInt("timers.nightTimeDuration"),
            new ArrayList<GamePlayer>(Arrays.asList(player))
        );
        
        new BukkitRunnable() {

            @Override
            public void run() {
                double timeRemainingInSeconds = (double) game.getPlugin().getConfig().getInt("timers.nightTimeDuration");

                if (timeRemainingInSeconds <= 0 || game.getSelectedPlayer() != null) {
                    this.cancel();
                    timer.cancel();
                    player.getPlayer().closeInventory();

                    GamePlayer selectedPlayer = game.getSelectedPlayer();
                    if (selectedPlayer != null) {
                        selectedPlayer.setPoisoned(true);
                        game.setLastPoisoned(selectedPlayer);
                        game.setSelectedPlayer(null);
                    }
                    
                    NightActions.processNextNightAction(game);
                    return;
                }

                timeRemainingInSeconds--;
            }
        }.runTaskTimer(game.getPlugin(), 0, Math.round(Bukkit.getServerTickManager().getTickRate()));  
    }
}
