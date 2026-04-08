package net.ducruetl.BOTCPlugin.gameobjects.roles.demons;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

import net.ducruetl.BOTCPlugin.gameobjects.Game;
import net.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import net.ducruetl.BOTCPlugin.gameobjects.NightActions;
import net.ducruetl.BOTCPlugin.gameobjects.Team;
import net.ducruetl.BOTCPlugin.gameobjects.roles.Role;
import net.ducruetl.BOTCPlugin.items.CustomItems;

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

                if (timeRemainingInSeconds <= 0) {
                    this.cancel();
                    player.getPlayer().closeInventory();
                    timerBar.removeAll();
                    
                    NightActions.processNextNightAction(game);
                    return;
                }

                timeRemainingInSeconds--;
            }
        }.runTaskTimer(game.getPlugin(), 0, Math.round(Bukkit.getServerTickManager().getTickRate()));
    }
    
}
