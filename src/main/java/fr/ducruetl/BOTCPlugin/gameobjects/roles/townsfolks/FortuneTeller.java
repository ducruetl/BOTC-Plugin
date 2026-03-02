package fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import fr.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Drunk;

public class FortuneTeller extends Role {

    private GamePlayer lure;

    public FortuneTeller() {
        super(
            "Voyante",
            "Chaque nuit, tu choisis 2 joueurs, tu recevra l'information de si "
            + "il y a le diablotin parmis eux ou non. Attention, il y a également un "
            + "citoyen dans la partie que tu verra aussi comme diablotin.",
            Team.TOWNSFOLK,
            2
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        if (game.getDay() == 1) {
            ArrayList<GamePlayer> players = game.getPlayers();
            Collections.shuffle(players);

            int i = 0;
            while (i < players.size() && players.get(i).isSeenAsDemon()) {
                i++;
            }

            lure = players.get(i);
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
            GamePlayer firstSelectedPlayer = null;

            @Override
            public void run() {
                int minutesRemaining = (int) Math.floor((int) timeRemainingInSeconds / 60);
                int secondsRemaining = (int) timeRemainingInSeconds % 60;

                timerBar.setTitle("Votre tour " + minutesRemaining + ":" + secondsRemaining);
                timerBar.setProgress(timeRemainingInSeconds/totalTimeInSeconds);

                if (timeRemainingInSeconds <= 0 || game.getSelectedPlayer() != null) {
                    // The player need to select a second player, so we store the first
                    // selected player and continue
                    if (firstSelectedPlayer == null) {
                        firstSelectedPlayer = game.getSelectedPlayer();
                        game.setSelectedPlayer(null);
                    }

                    this.cancel();
                    player.getPlayer().closeInventory();
                    timerBar.removeAll();

                    GamePlayer selectedPlayer = game.getSelectedPlayer();
                    if (selectedPlayer != null && firstSelectedPlayer != null) {
                        boolean impFound = selectedPlayer.isSeenAsImp() || lure.equals(selectedPlayer)
                            || firstSelectedPlayer.isSeenAsImp() || lure.equals(firstSelectedPlayer);

                        if (player.isPoisoned() || player.getRole() instanceof Drunk) {
                            Random r = new Random();
                            impFound = r.nextBoolean();
                        }

                        sendInfoMessage(player, firstSelectedPlayer, selectedPlayer, impFound);
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

    public void sendInfoMessage(GamePlayer player, GamePlayer firstSelectedPlayer,
        GamePlayer secondSelectedPlayer, boolean impFound) {
        
        if (impFound) {
            player.getPlayer().sendMessage(
                ChatColor.BLUE + "Le diablotin se trouve entre "
                + ChatColor.YELLOW + "" + ChatColor.BOLD + firstSelectedPlayer.getPlayer().getDisplayName()
                + ChatColor.RESET + ChatColor.BLUE + " et "
                + ChatColor.YELLOW + "" + ChatColor.BOLD + secondSelectedPlayer.getPlayer().getDisplayName()
            );
        } else {
            player.getPlayer().sendMessage(
                ChatColor.BLUE + "Le diablotin n'est pas présent entre "
                + ChatColor.YELLOW + "" + ChatColor.BOLD + firstSelectedPlayer.getPlayer().getDisplayName()
                + ChatColor.RESET + ChatColor.BLUE + " et "
                + ChatColor.YELLOW + "" + ChatColor.BOLD + secondSelectedPlayer.getPlayer().getDisplayName()
            );
        }
    }
    
}
