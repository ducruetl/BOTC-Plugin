package fr.ducruetl.BOTCPlugin.gameobjects.roles.demons;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

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
        game.setLastKilledByImp(null);

        Inventory playerSelectInventory = game.createTargetSelectionInventory();
        player.getPlayer().closeInventory();
        player.getPlayer().openInventory(playerSelectInventory);

        BossBar timerBar = Bukkit.getServer().createBossBar("Votre tour", BarColor.BLUE, BarStyle.SOLID);
        timerBar.setProgress(1.0);  
        timerBar.addPlayer(player.getPlayer());
        timerBar.setVisible(true);

        new BukkitRunnable() {
            double totalTimeInSeconds = (double) game.getPlugin().getConfig().getInt("timers.nightTimeDuration");
            double timeRemainingInSeconds = totalTimeInSeconds;

            @Override
            public void run() {
                int minutesRemaining = (int) Math.floor((int) timeRemainingInSeconds / 60);
                int secondsRemaining = (int) timeRemainingInSeconds % 60;

                timerBar.setTitle("Votre tour " + minutesRemaining + ":" + secondsRemaining);
                timerBar.setProgress(timeRemainingInSeconds/totalTimeInSeconds);

                if (timeRemainingInSeconds <= 0 || game.getSelectedPlayer() != null) {
                    this.cancel();
                    player.getPlayer().closeInventory();
                    timerBar.removeAll();

                    GamePlayer selectedPlayer = game.getSelectedPlayer();
                    if (selectedPlayer != null 
                        && !((selectedPlayer.getRole() instanceof Soldier) && !selectedPlayer.isPoisoned())
                        && (!(selectedPlayer.isProtected()))) {
                        selectedPlayer.setDead(true);
                        game.setLastKilledByImp(selectedPlayer);
                        game.setSelectedPlayer(null);
                    }
                    
                    NightActions.processNextNightAction(game);
                    return;
                }

                // If the player closed the inventory, we reopen it
                if (player.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING) {
                    player.getPlayer().openInventory(playerSelectInventory);
                }

                timeRemainingInSeconds--;
            }
        }.runTaskTimer(game.getPlugin(), 0, Math.round(Bukkit.getServerTickManager().getTickRate()));
    }
}
