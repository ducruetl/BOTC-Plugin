package net.ducruetl.BOTCPlugin.gameobjects;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class DayActions {
    
    /**
     * Start the next day
     * @param game Game object related
     */
    public static void nextDay(Game game) {
        game.setState(GameState.DAY);
        Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<yellow>Le jour se lève !</yellow>"));

        if (game.getLastKilledByImp() != null) {
            final Component message = MiniMessage.miniMessage().deserialize(
                "<yellow><bold><name></bold></yellow> <red>a été tué durant la nuit</red>",
                Placeholder.unparsed("name", game.getLastKilledByImp().getPlayer().getName())
            );
            Bukkit.broadcast(message);

            Player killedPlayer = game.getLastKilledByImp().getPlayer();
            killedPlayer.getWorld().strikeLightning(killedPlayer.getLocation());
            game.setLastKilledByImp(null);
        } else {
            final Component message = MiniMessage.miniMessage().deserialize("<blue>Personne n'est mort cette nuit</blue>");
            Bukkit.broadcast(message);
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
