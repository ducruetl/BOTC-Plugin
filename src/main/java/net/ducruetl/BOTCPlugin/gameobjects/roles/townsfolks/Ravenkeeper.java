package net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import net.ducruetl.BOTCPlugin.gameobjects.Game;
import net.ducruetl.BOTCPlugin.gameobjects.GamePlayer;
import net.ducruetl.BOTCPlugin.gameobjects.NightActions;
import net.ducruetl.BOTCPlugin.gameobjects.Team;
import net.ducruetl.BOTCPlugin.gameobjects.roles.Role;
import net.ducruetl.BOTCPlugin.gameobjects.roles.demons.Imp;
import net.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Drunk;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class Ravenkeeper extends Role {

    public Ravenkeeper() {
        super(
            "Croque-Mort",
            "Si le diablotin te tue pendant la nuit, tu peut "
            + "choisir de voir le rôle de la personne de ton choix.",
            Team.TOWNSFOLK,
            4
        );
    }

    @Override
    public void onNightTurn(Game game, GamePlayer player) {
        if (game.getLastKilledByImp().equals(player)) {

            Inventory playerSelectInventory = game.createTargetSelectionInventory();
            player.getPlayer().closeInventory();
            player.getPlayer().openInventory(playerSelectInventory);

            BossBar timerBar = Bukkit.getServer().createBossBar("Votre tour", BarColor.BLUE, BarStyle.SOLID);
            timerBar.setProgress(1.0);  
            timerBar.addPlayer(player.getPlayer());
            timerBar.setVisible(true);

            new BukkitRunnable() {
                double totalTimeInSeconds = (double) game.getPlugin().getConfig().getInt("timers.nightTimeDuration");
                double timeRemainingInSeconds = totalTimeInSeconds;

                @Override
                public void run() {
                    int minutesRemaining = (int) Math.floor((int) timeRemainingInSeconds / 60);
                    int secondsRemaining = (int) timeRemainingInSeconds % 60;

                    timerBar.setTitle("Votre tour " + minutesRemaining + ":" + secondsRemaining);
                    timerBar.setProgress(timeRemainingInSeconds/totalTimeInSeconds);

                    if (timeRemainingInSeconds <= 0 || game.getSelectedPlayer() != null) {
                        this.cancel();
                        player.getPlayer().closeInventory();
                        timerBar.removeAll();

                        GamePlayer selectedPlayer = game.getSelectedPlayer();
                        if (selectedPlayer != null) {
                            Role role = new Imp();
                            
                            if (player.isPoisoned() || player.getRole() instanceof Drunk) {
                                Random r = new Random();
                                List<Role> listRoles;

                                switch (r.nextInt(4)) {
                                    case 0:
                                        listRoles = game.getTownsfolksPool();
                                        Collections.shuffle(listRoles);
                                        role = listRoles.getFirst();
                                        break;
                                
                                    case 1:
                                        listRoles = game.getOutsidersPool();
                                        Collections.shuffle(listRoles);
                                        role = listRoles.getFirst();
                                        break;

                                    case 2:
                                        listRoles = game.getMinionPool();
                                        Collections.shuffle(listRoles);
                                        role = listRoles.getFirst();
                                        break;

                                    case 3:
                                        role = new Imp();
                                        break;
                                }
                            } else {
                                role = selectedPlayer.getRole();

                                if (role instanceof Drunk) {
                                    role = selectedPlayer.getFacadeRole();
                                }
                            }

                            sendInfoMessage(player, selectedPlayer, role);
                        }
                        
                        game.setSelectedPlayer(null);
                        NightActions.processNextNightAction(game);
                        return;
                    }

                    // If the player closed the inventory, we reopen it
                    if (player.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING) {
                        player.getPlayer().openInventory(playerSelectInventory);
                    }

                    timeRemainingInSeconds--;
                }
            }.runTaskTimer(game.getPlugin(), 0, Math.round(Bukkit.getServerTickManager().getTickRate()));
        }

        NightActions.processNextNightAction(game);
    }

    public void sendInfoMessage(GamePlayer player, GamePlayer selectedPlayer, Role role) {
        String color;
        String colorEnd;

        if (role.getTeam() == Team.TOWNSFOLK) {
            color = "<green>";
            colorEnd = "</green>";
        } else if (role.getTeam() == Team.OUTSIDER) {
            color = "<aqua>";
            colorEnd = "</aqua>";
        } else {
            color = "<red>";
            colorEnd = "</red>";
        }

        final Component message = MiniMessage.miniMessage().deserialize(
            "<yellow><bold><name></bold></yellow> <blue>a pour rôle</blue> " + color + "<role>" + colorEnd,
            Placeholder.unparsed("name", selectedPlayer.getRole().getName()),
            Placeholder.unparsed("role", role.getName())
        );
        player.getPlayer().sendMessage(message);
    }
    
}
