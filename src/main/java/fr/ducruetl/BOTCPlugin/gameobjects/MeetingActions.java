package fr.ducruetl.BOTCPlugin.gameobjects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

import fr.ducruetl.BOTCPlugin.items.CustomItems;

public class MeetingActions {

    /**
     * Start the next meeting
     * @param game Game object related
     */
    public static void nextMeeting(Game game) {
        game.setState(GameState.MEETING);
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Le meeting commence");

        String timerName = "Meeting " + game.getDay() + " | ";

        BossBar timerBar = Bukkit.getServer().createBossBar(timerName, BarColor.YELLOW, BarStyle.SOLID);
        timerBar.setProgress(1.0);

        for (GamePlayer player : game.getPlayers()) {
            timerBar.addPlayer(player.getPlayer());
            player.getPlayer().getInventory().clear();
            if (!player.isDead()) {
                player.getPlayer().getInventory().setItem(0, CustomItems.getNominationItem());
            }
        }

        timerBar.setVisible(true);

        new BukkitRunnable() {
            double totalTimeInSeconds = (double) game.getPlugin().getConfig().getInt("timers.meetingTimeDuration");
            double timeRemainingInSeconds = totalTimeInSeconds;
            
            @Override
            public void run() {
                if (timeRemainingInSeconds <= 0) {
                    timerBar.removeAll();
                    this.cancel();
                    MeetingActions.nextVote(game);
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

    public static void nextVote(Game game) {
        game.setState(GameState.VOTE);

        for (GamePlayer player : game.getPlayers()) {
            player.getPlayer().getInventory().clear();
        }

        if (game.getNominatedPlayers().isEmpty()) {
            game.setDay(game.getDay() + 1);
            NightActions.nextNight(game);
            return;
        }

        for (GamePlayer player : game.getPlayers()) {
            if (player.isDead() && player.isHasVotedAfterDeath()) {
                continue;
            }

            player.getPlayer().getInventory().setItem(0, CustomItems.getVoteToken());
        }

        GamePlayer voteTarget = game.getNominatedPlayers().getFirst();
        game.getPlayersVotes().put(voteTarget, 0);
        
        String timerName = "Vote de " + ChatColor.YELLOW + "" + ChatColor.BOLD + voteTarget.getPlayer().getDisplayName();

        BossBar timerBar = Bukkit.getServer().createBossBar(timerName, BarColor.YELLOW, BarStyle.SOLID);
        timerBar.setProgress(1.0);

        for (GamePlayer player : game.getPlayers()) {
            timerBar.addPlayer(player.getPlayer());
        }

        timerBar.setVisible(true);

        new BukkitRunnable() {
            double totalTimeInSeconds = (double) game.getPlugin().getConfig().getInt("timers.voteTimeDuration");
            double timeRemainingInSeconds = totalTimeInSeconds;
            
            @Override
            public void run() {
                if (timeRemainingInSeconds <= 0) {
                    timerBar.removeAll();
                    this.cancel();
                    game.getNominatedPlayers().removeFirst();
                    MeetingActions.nextVote(game);
                    return;
                }

                timeRemainingInSeconds--;
                timerBar.setProgress(timeRemainingInSeconds/totalTimeInSeconds);
            }
            
        }.runTaskTimer(game.getPlugin(), 0, Math.round(game.getPlugin().getServer().getServerTickManager().getTickRate()));
    }
    
}
