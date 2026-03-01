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
                    if (selectedPlayer != null) {
                        selectedPlayer.setPoisoned(true);
                        game.setLastPoisoned(selectedPlayer);
                        game.setSelectedPlayer(null);
                    }

                    // If the player closed the inventory, we reopen it
                    if (player.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING) {
                        player.getPlayer().openInventory(playerSelectInventory);
                    }
                    
                    NightActions.processNextNightAction(game);
                    return;
                }

                timeRemainingInSeconds--;
            }
        }.runTaskTimer(game.getPlugin(), 0, Math.round(Bukkit.getServerTickManager().getTickRate()));  
    }
}
