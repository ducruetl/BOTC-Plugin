package fr.ducruetl.BOTCPlugin;

import java.util.ArrayList;

import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import fr.ducruetl.BOTCPlugin.commands.NametagCommand;
import fr.ducruetl.BOTCPlugin.gameobjects.Game;
import fr.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import fr.ducruetl.BOTCPlugin.listeners.InventoryListener;
import fr.ducruetl.BOTCPlugin.listeners.PlayerListener;

public final class BOTCPlugin extends JavaPlugin {

    public static final String NAMETAG_TEAM_NAME = "hidenametag";

    private Game game;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin BOTC started.");

        // Create a new team if it doesn't exist for showing/hiding nametags
        if (getNametagTeam() == null) {
            getServer().getScoreboardManager().getMainScoreboard().registerNewTeam(NAMETAG_TEAM_NAME);
        }

        setWorldsOptions();

        // Register listeners classes
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new InventoryListener(this), this);
        
        // Register commands executors
        getCommand("nametags").setExecutor(new NametagCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin BOTC stopped.");
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Team getNametagTeam() {
        return getServer()
                .getScoreboardManager()
                .getMainScoreboard()
                .getTeam(NAMETAG_TEAM_NAME);
    }

    public void setWorldsOptions() {
        for (World world : getServer().getWorlds()) {
            world.setDifficulty(Difficulty.PEACEFUL);
            world.setPVP(false);
            world.setSpawnLocation(0, world.getHighestBlockYAt(0, 0) + 1, 0);
        }
    }

    // TODO: Find a way to add a trailing 0 to minutes/seconds (ex. 01:05)
    public BukkitTask startTimer(String timerName, int timerDuration, ArrayList<GamePlayer> players) {
        BossBar timerBar = getServer().createBossBar(timerName, BarColor.BLUE, BarStyle.SOLID);
        timerBar.setProgress(1.0);
        timerBar.setVisible(true);

        for (GamePlayer player : players) {
            timerBar.addPlayer(player.getPlayer());
        }

        BukkitTask task = new BukkitRunnable() {
            double totalTimeInSeconds = (double) timerDuration;
            double timeRemainingInSeconds = totalTimeInSeconds;

            @Override
            public void run() {
                if (timeRemainingInSeconds <= 0) {
                    timerBar.setVisible(false);
                    this.cancel();
                    return;
                }

                timeRemainingInSeconds--;
                int minutesRemaining = (int) Math.floor((int) timeRemainingInSeconds / 60);
                int secondsRemaining = (int) timeRemainingInSeconds % 60;
                timerBar.setProgress(timeRemainingInSeconds/totalTimeInSeconds);
                timerBar.setTitle(timerName + " " + minutesRemaining + ":" + secondsRemaining);
            }
            
        }.runTaskTimer(this, 0, Math.round(getServer().getServerTickManager().getTickRate()));

        return task;
    }
}
