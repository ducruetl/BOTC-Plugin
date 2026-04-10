package net.ducruetl.BOTCPlugin;

import java.util.ArrayList;

import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import net.ducruetl.BOTCPlugin.commands.NametagCommand;
import net.ducruetl.BOTCPlugin.commands.PositionsCommands;
import net.ducruetl.BOTCPlugin.gameobjects.Game;
import net.ducruetl.BOTCPlugin.listeners.InventoryListener;
import net.ducruetl.BOTCPlugin.listeners.PlayerListener;

public final class BOTCPlugin extends JavaPlugin {

    public static final String NAMETAG_TEAM_NAME = "hidenametag";

    private Game game;

    // Positions of the rooms where players are during the night
    public ArrayList<Location> roomPositions = new ArrayList<>();

    // Positions of the seat where players are during the meeting
    public ArrayList<Location> seatPositions = new ArrayList<>();

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
        getCommand("botc").setExecutor(new PositionsCommands(this));
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

    public ArrayList<Location> getRoomPositions() {
        return roomPositions;
    }

    public void addRoomPosition(Location position) {
        roomPositions.add(position);
    }

    public void resetRoomPositions() {
        roomPositions.clear();
    }

    public ArrayList<Location> getSeatPositions() {
        return seatPositions;
    }

    public void addSeatPosition(Location position) {
        seatPositions.add(position);
    }

    public void resetSeatPositions() {
        seatPositions.clear();
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
            world.setGameRule(GameRule.DO_FIRE_TICK, false);
            world.setSpawnLocation(0, world.getHighestBlockYAt(0, 0) + 1, 0);
        }
    }
}
