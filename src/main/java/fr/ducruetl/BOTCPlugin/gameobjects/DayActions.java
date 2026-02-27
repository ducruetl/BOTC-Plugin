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
        game.setState(GameState.DAY);
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Le jour se lève !");

        if (game.getLastKilledByImp() != null) {
            Bukkit.broadcastMessage(
                ChatColor.YELLOW + "" + ChatColor.BOLD + game.getLastKilledByImp().getPlayer().getDisplayName()
                + ChatColor.RESET + ChatColor.RED + " a été tué durant la nuit"
            );
            game.setLastKilledByImp(null);
        } else {
            Bukkit.broadcastMessage(ChatColor.BLUE + "Personne n'est mort cette nuit");
        }

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
                    MeetingActions.nextMeeting(game);
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
