package net.ducruetl.BOTCPlugin.gameobjects;

import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.ducruetl.BOTCPlugin.items.CustomItems;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.bossbar.BossBarViewer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class MeetingActions {

    /**
     * Start the next meeting
     * @param game Game object related
     */
    public static void nextMeeting(Game game) {
        game.setState(GameState.MEETING);
        final Component message = MiniMessage.miniMessage().deserialize("<yellow>Le meeting commence</yellow>");
        Bukkit.broadcast(message);

        final Component barMessage = MiniMessage.miniMessage().deserialize("Meeting <day> | ",
            Placeholder.unparsed("day", Integer.toString(game.getDay()))
        );

        BossBar timerBar = BossBar.bossBar(barMessage, 1.0f, Color.YELLOW, Overlay.PROGRESS);

        for (GamePlayer player : game.getPlayers()) {
            timerBar.addViewer(player.getPlayer());
            if (!player.isDead()) {
                player.getPlayer().getInventory().setItem(0, CustomItems.getNominationItem());
            }
        }

        new BukkitRunnable() {
            float totalTimeInSeconds = (float) game.getPlugin().getConfig().getInt("timers.meetingTimeDuration");
            float timeRemainingInSeconds = totalTimeInSeconds;
            
            @Override
            public void run() {
                if (timeRemainingInSeconds <= 0) {
                    Iterator<? extends BossBarViewer> it = timerBar.viewers().iterator();
                    while (it.hasNext()) {
                        BossBarViewer viewer = it.next();
                        if (viewer instanceof Player player) {
                            timerBar.removeViewer(player);
                        }
                    }

                    this.cancel();
                    nextVote(game);
                    return;
                }

                timeRemainingInSeconds--;
                int minutesRemaining = (int) Math.floor((int) timeRemainingInSeconds / 60);
                int secondsRemaining = (int) timeRemainingInSeconds % 60;
                timerBar.progress(timeRemainingInSeconds/totalTimeInSeconds);
                final Component updatedBarMessage = MiniMessage.miniMessage().deserialize(
                    "Meeting <day> | <min>:<sec>",
                    Placeholder.unparsed("day", Integer.toString(game.getDay())),
                    Placeholder.unparsed("min", Integer.toString(minutesRemaining)),
                    Placeholder.unparsed("sec", Integer.toString(secondsRemaining))
                );
                timerBar.name(updatedBarMessage);
            }
            
        }.runTaskTimer(game.getPlugin(), 0, Math.round(game.getPlugin().getServer().getServerTickManager().getTickRate()));
    }

    /**
     * Start the vote for the next player in nominated players list,
     * if there are no more nominated players, go to the next game state
     * @param game The related Game object
     */
    public static void nextVote(Game game) {
        game.setState(GameState.VOTE);
        game.setLastKilledByVote(null);

        for (GamePlayer player : game.getPlayers()) {
            player.getPlayer().getInventory().remove(CustomItems.getNominationItem());
            player.getPlayer().getInventory().remove(CustomItems.getVoteToken());
        }

        if (game.getNominatedPlayers().isEmpty()) {
            killMostVotedPlayer(game);
            game.setDay(game.getDay() + 1);
            NightActions.nextNight(game);
            return;
        }

        for (GamePlayer player : game.getPlayers()) {
            if (player.isDead() && player.getHasVotedAfterDeath()) {
                continue;
            }

            player.getPlayer().getInventory().setItem(0, CustomItems.getVoteToken());
        }

        GamePlayer voteTarget = game.getNominatedPlayers().getFirst();
        game.getPlayersVotes().put(voteTarget, 0);
        
        final Component timerName = MiniMessage.miniMessage().deserialize(
            "Vote de <yellow><bold><name></bold></yellow>",
            Placeholder.unparsed("name", voteTarget.getPlayer().getName())
        );

        BossBar timerBar = BossBar.bossBar(timerName, 1.0f, Color.BLUE, Overlay.PROGRESS);

        for (GamePlayer player : game.getPlayers()) {
            timerBar.addViewer(player.getPlayer());
        }

        new BukkitRunnable() {
            float totalTimeInSeconds = (float) game.getPlugin().getConfig().getInt("timers.voteTimeDuration");
            float timeRemainingInSeconds = totalTimeInSeconds;
            
            @Override
            public void run() {
                if (timeRemainingInSeconds <= 0) {
                    Iterator<? extends BossBarViewer> it = timerBar.viewers().iterator();
                    while (it.hasNext()) {
                        BossBarViewer viewer = it.next();
                        if (viewer instanceof Player player) {
                            timerBar.removeViewer(player);
                        }
                    }

                    this.cancel();
                    game.getNominatedPlayers().removeFirst();
                    MeetingActions.nextVote(game);
                    return;
                }

                timeRemainingInSeconds--;
                timerBar.progress(timeRemainingInSeconds/totalTimeInSeconds);
            }
            
        }.runTaskTimer(game.getPlugin(), 0, Math.round(game.getPlugin().getServer().getServerTickManager().getTickRate()));
    }

    /**
     * Kill a player or not, based on the votes and the alive players count
     * @param game The related Game object
     */
    public static void killMostVotedPlayer(Game game) {
        Map<GamePlayer, Integer> votes = game.getPlayersVotes();
        GamePlayer mostVoted = null;
        GamePlayer mostVotedEqually = null;

        for (GamePlayer player : votes.keySet()) {
            if (mostVoted == null) {
                mostVoted = player;
            } else {
                if (votes.get(mostVoted) < votes.get(player)) {
                    mostVoted = player;
                    mostVotedEqually = null;
                } else if (votes.get(mostVoted) == votes.get(player)) {
                    mostVotedEqually = player;
                }
            }
        }

        if (mostVoted == null 
        || votes.get(mostVoted) < Math.ceil((double) game.getAlivePlayersCount() / 2.0)) {
            final Component message = MiniMessage.miniMessage().deserialize("<blue>Personne n'est mort durant ce vote.</blue>");
            Bukkit.broadcast(message);
            return;
        }

        if (mostVoted != null && mostVotedEqually != null) {
            final Component message = MiniMessage.miniMessage().deserialize(
                "<blue>Il y a eu une égalité entre</blue> <yellow><bold><firstname></bold></yellow> <blue>et</blue> <yellow><bold><secondname>.</bold></yellow> <blue>Personne ne meurt.</blue>",
                Placeholder.unparsed("firstname", mostVoted.getPlayer().getName()),
                Placeholder.unparsed("secondname", mostVotedEqually.getPlayer().getName())
            );
            Bukkit.broadcast(message);
            return;
        }

        if (mostVoted != null 
        && mostVotedEqually == null 
        && votes.get(mostVoted) >= Math.ceil((double) game.getAlivePlayersCount() / 2.0)) {
            mostVoted.setDead(true);
            game.setLastKilledByVote(mostVoted);
            final Component message = MiniMessage.miniMessage().deserialize(
                "<yellow><bold><name></bold></yellow> <blue>a été éliminé par les citoyens.</blue>",
                Placeholder.unparsed("name", mostVoted.getPlayer().getName())
            );
            Bukkit.broadcast(message);
        }

        for (GamePlayer player : game.getPlayers()) {
            player.setHasVoted(false);
        }
    }
    
}
