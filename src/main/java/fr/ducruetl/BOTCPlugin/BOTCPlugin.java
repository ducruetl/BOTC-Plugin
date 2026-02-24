package fr.ducruetl.BOTCPlugin;

import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import fr.ducruetl.BOTCPlugin.commands.NametagCommand;
import fr.ducruetl.BOTCPlugin.listeners.PlayerListener;

public final class BOTCPlugin extends JavaPlugin {

    public static final String NAMETAG_TEAM_NAME = "hidenametag";

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin BOTC started.");

        // Create a new team if it doesn't exist for showing/hiding nametags
        if (getNametagTeam() == null) {
            getServer().getScoreboardManager().getMainScoreboard().registerNewTeam(NAMETAG_TEAM_NAME);
        }

        for (World world : getServer().getWorlds()) {
            world.setDifficulty(Difficulty.PEACEFUL);
            world.setPVP(false);
            world.setSpawnLocation(0, world.getHighestBlockYAt(0, 0) + 1, 0);
        }

        // Register listeners classes
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(this), this);
        
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

}
