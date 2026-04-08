package net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import net.ducruetl.BOTCPlugin.gameobjects.Game;
import net.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import net.ducruetl.BOTCPlugin.gameobjects.NightActions;
import net.ducruetl.BOTCPlugin.gameobjects.Team;
import net.ducruetl.BOTCPlugin.gameobjects.roles.Role;
import net.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Drunk;

public class Monk extends Role {

    public Monk() {
        super(
            "Prêtre",
            "Chaque nuit, tu peut choisir une personne à défendre contre l'attaque du diablotin.",
            Team.TOWNSFOLK,
            2
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        if (game.getLastProtected() != null) {
            game.getLastProtected().setProtected(false);
            game.setLastProtected(null);
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
                    if (selectedPlayer != null 
                        && !player.isPoisoned()
                        && !(player.getRole() instanceof Drunk)) {
                        selectedPlayer.setProtected(true);
                        game.setLastProtected(selectedPlayer);
                    }
                    
                    game.setSelectedPlayer(null);
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
