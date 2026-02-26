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
import fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown.Soldier;

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
        game.setLastKilled(null);

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
                    if (selectedPlayer != null 
                        && !((selectedPlayer.getRole() instanceof Soldier) && !selectedPlayer.isPoisoned())
                        && (!(selectedPlayer.isProtected()))) {
                        selectedPlayer.setDead(true);
                        game.setLastKilled(selectedPlayer);
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
