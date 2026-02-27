package fr.ducruetl.BOTCPlugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import fr.ducruetl.BOTCPlugin.BOTCPlugin;

public class NametagCommand implements CommandExecutor {

    BOTCPlugin plugin;

    public NametagCommand(BOTCPlugin plugin) {
        this.plugin = plugin;
    }

     @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.contentEquals("nametags")) {
            if (args.length != 1) {
                return false;
            }

            Team nametagTeam = plugin.getNametagTeam();
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
    
    /**
     * Enable players nametags
     * @param plugin BOTCPlugin instance
     */
    public static void enableNametags(BOTCPlugin plugin) {
        plugin.getNametagTeam().setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.ALWAYS);
        plugin.getServer().broadcastMessage("Nametags enabled.");
    }

    /**
     * Disable players nametags
     * @param plugin BOTCPlugin instance
     */
    public static void disableNametags(BOTCPlugin plugin) {
        plugin.getNametagTeam().setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.ALWAYS);
        plugin.getServer().broadcastMessage("Nametags disabled.");
    }
    
}
