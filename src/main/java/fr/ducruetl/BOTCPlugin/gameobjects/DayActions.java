package fr.ducruetl.BOTCPlugin.gameobjects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

public class DayActions {
    
    /**
     * Start the next day
     * @param game Game object related
     */
    public static void nextDay(Game game) {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Le jour se lève !");

        String timerName = "Jour " + game.getDay() + " | ";

        BossBar timerBar = Bukkit.getServer().createBossBar(timerName, BarColor.GREEN, BarStyle.SOLID);
        timerBar.setProgress(1.0);

        for (GamePlayer player : game.getPlayers()) {
            timerBar.addPlayer(player.getPlayer());
        }

        timerBar.setVisible(true);

        new BukkitRunnable() {
            double totalTimeInSeconds = (double) game.getPlugin().getConfig().getInt("timers.dayTimeDuration");
            double timeRemainingInSeconds = totalTimeInSeconds;
            
            @Override
            public void run() {
                if (timeRemainingInSeconds <= 0) {
                    timerBar.removeAll();
                    this.cancel();
                    return;
                }

                timeRemainingInSeconds--;
                int minutesRemaining = (int) Math.floor((int) timeRemainingInSeconds / 60);
                int secondsRemaining = (int) timeRemainingInSeconds % 60;
                timerBar.setProgress(timeRemainingInSeconds/totalTimeInSeconds);
                timerBar.setTitle(timerName + minutesRemaining + ":" + secondsRemaining);
            }
            
        }.runTaskTimer(game.getPlugin(), 0, Math.round(game.getPlugin().getServer().getServerTickManager().getTickRate()));
    }

}
