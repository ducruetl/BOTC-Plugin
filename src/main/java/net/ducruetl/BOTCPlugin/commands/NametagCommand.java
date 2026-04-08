package net.ducruetl.BOTCPlugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import net.ducruetl.BOTCPlugin.BOTCPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

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

            if (args[0].contentEquals("enable")) {
                enableNametags(plugin);
            } else if (args[0].contentEquals("disable")) {
                disableNametags(plugin);
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
        final Component message = MiniMessage.miniMessage().deserialize("<gray>Nametags enabled.</gray>");
        plugin.getServer().broadcast(message);
    }

    /**
     * Disable players nametags
     * @param plugin BOTCPlugin instance
     */
    public static void disableNametags(BOTCPlugin plugin) {
        plugin.getNametagTeam().setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.ALWAYS);
        final Component message = MiniMessage.miniMessage().deserialize("<gray>Nametags disabled.</gray>");
        plugin.getServer().broadcast(message);
    }
    
}
