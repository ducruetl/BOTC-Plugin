package net.ducruetl.BOTCPlugin.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import net.ducruetl.BOTCPlugin.BOTCPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PositionsCommands implements CommandExecutor {

    private BOTCPlugin plugin;

    public PositionsCommands(BOTCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                            @NotNull Command command,
                            @NotNull String label,
                            @NotNull String[] args) {

        if (!(sender instanceof Player player)) return true;
        if (!command.getName().equalsIgnoreCase("botc")) return true;
        if (args.length != 2) return false;

        Location location = player.getLocation();

        switch (args[0].toLowerCase()) {
            case "add" -> {
                switch (args[1].toLowerCase()) {
                    case "room" -> {
                        plugin.addRoomPosition(location);
                        player.sendMessage(Component.text("Room position added.", NamedTextColor.GREEN));
                    }
                    case "seat" -> {
                        plugin.addSeatPosition(location);
                        player.sendMessage(Component.text("Seat position added.", NamedTextColor.GREEN));
                    }
                    default -> { return false; }
                }
            }
            case "reset" -> {
                switch (args[1].toLowerCase()) {
                    case "room" -> {
                        plugin.resetRoomPositions();
                        player.sendMessage(Component.text("Room positions removed.", NamedTextColor.RED));
                    }
                    case "seat" -> {
                        plugin.resetSeatPositions();
                        player.sendMessage(Component.text("Seat positions removed.", NamedTextColor.RED));
                    }
                    default -> { return false; }
                }
            }
            default -> { return false; }
        }

        return true;
    }
    
}
