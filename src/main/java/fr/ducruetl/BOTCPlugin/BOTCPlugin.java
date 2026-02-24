package fr.ducruetl.BOTCPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

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

        // Register listeners classes
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin BOTC stopped.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (label.contentEquals("nametags")) {
            if (args.length != 1) {
                return false;
            }

            Team nametagTeam = getNametagTeam();
            if (args[0].contentEquals("enable")) {
                nametagTeam.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.ALWAYS);
                sender.sendMessage("Nametags enabled.");
            } else if (args[0].contentEquals("disable")) {
                nametagTeam.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.NEVER);
                sender.sendMessage("Nametags disabled.");
            } else {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public Team getNametagTeam() {
        return getServer()
                .getScoreboardManager()
                .getMainScoreboard()
                .getTeam(NAMETAG_TEAM_NAME);
    }

}
