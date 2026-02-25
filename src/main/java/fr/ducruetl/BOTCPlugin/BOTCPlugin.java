package fr.ducruetl.BOTCPlugin;

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
import fr.ducruetl.BOTCPlugin.listeners.InventoryListener;
import fr.ducruetl.BOTCPlugin.listeners.PlayerListener;

public final class BOTCPlugin extends JavaPlugin {

    public static final String NAMETAG_TEAM_NAME = "hidenametag";
    public static final int TICK_PER_SECONDS = 20;

    private BossBar timerBar;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin BOTC started.");

        // Create a new team if it doesn't exist for showing/hiding nametags
        if (getNametagTeam() == null) {
            getServer().getScoreboardManager().getMainScoreboard().registerNewTeam(NAMETAG_TEAM_NAME);
        }

        setWorldsOptions();
        this.timerBar = createTimerBar();

        // Register listeners classes
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new InventoryListener(), this);
        
        // Register commands executors
        getCommand("nametags").setExecutor(new NametagCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin BOTC stopped.");
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

    // TODO: Find better way to stop the timer
    // TODO: Find a way to add a trailing 0 to minutes/seconds (ex. 01:05)
    public BossBar createTimerBar() {
        BossBar timerBar = getServer().createBossBar("Timer", BarColor.BLUE, BarStyle.SOLID);
        timerBar.setProgress(1.0);
        timerBar.setVisible(true);

        BukkitTask timer = new BukkitRunnable() {
            double totalTimeInSeconds = (double) getConfig().getInt("timers.freeTimeDuration");
            double timeRemainingInSeconds = totalTimeInSeconds;

            @Override
            public void run() {
                timeRemainingInSeconds--;
                int minutesRemaining = (int) Math.floor((int) timeRemainingInSeconds / 60);
                int secondsRemaining = (int) timeRemainingInSeconds % 60;
                timerBar.setProgress(timeRemainingInSeconds/totalTimeInSeconds);
                timerBar.setTitle(minutesRemaining + ":" + secondsRemaining + " avant la réunion.");
            }
            
        }.runTaskTimer(this, 0, TICK_PER_SECONDS);

        getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                getServer().getScheduler().cancelTask(timer.getTaskId());
                timerBar.setVisible(false);
            }
        }, getConfig().getInt("timers.freeTimeDuration") * TICK_PER_SECONDS);

        return timerBar;
    }

    public BossBar getTimerBar() {
        return this.timerBar;
    }

}
