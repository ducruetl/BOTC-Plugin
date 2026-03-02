package fr.ducruetl.BOTCPlugin;

import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import fr.ducruetl.BOTCPlugin.commands.NametagCommand;
import fr.ducruetl.BOTCPlugin.gameobjects.Game;
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

    /**
     * Give the team related to nametag enabling/disabling
     * @return The Team object
     */
    public Team getNametagTeam() {
        return getServer()
                .getScoreboardManager()
                .getMainScoreboard()
                .getTeam(NAMETAG_TEAM_NAME);
    }

    /**
     * Disable PvP, change difficulty to peaceful, disable fire spread 
     * and change spawn location for all worlds
     */
    public void setWorldsOptions() {
        for (World world : getServer().getWorlds()) {
            world.setDifficulty(Difficulty.PEACEFUL);
            world.setPVP(false);
            world.setGameRule(GameRule.FIRE_SPREAD_RADIUS_AROUND_PLAYER, 0);
            world.setSpawnLocation(0, world.getHighestBlockYAt(0, 0) + 1, 0);
        }
    }
}
