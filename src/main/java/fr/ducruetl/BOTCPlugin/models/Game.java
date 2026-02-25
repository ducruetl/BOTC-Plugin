package fr.ducruetl.BOTCPlugin.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.ducruetl.BOTCPlugin.models.roles.Role;
import fr.ducruetl.BOTCPlugin.models.roles.demons.Imp;
import fr.ducruetl.BOTCPlugin.models.roles.demons.Poisoner;
import fr.ducruetl.BOTCPlugin.models.roles.demons.ScarletWoman;
import fr.ducruetl.BOTCPlugin.models.roles.demons.Spy;
import fr.ducruetl.BOTCPlugin.models.roles.folkstown.Chef;
import fr.ducruetl.BOTCPlugin.models.roles.folkstown.Empath;
import fr.ducruetl.BOTCPlugin.models.roles.folkstown.FortuneTeller;
import fr.ducruetl.BOTCPlugin.models.roles.folkstown.Investigator;
import fr.ducruetl.BOTCPlugin.models.roles.folkstown.Librarian;
import fr.ducruetl.BOTCPlugin.models.roles.folkstown.Mayor;
import fr.ducruetl.BOTCPlugin.models.roles.folkstown.Monk;
import fr.ducruetl.BOTCPlugin.models.roles.folkstown.Ravenkeeper;
import fr.ducruetl.BOTCPlugin.models.roles.folkstown.Slayer;
import fr.ducruetl.BOTCPlugin.models.roles.folkstown.Soldier;
import fr.ducruetl.BOTCPlugin.models.roles.folkstown.Undertaker;
import fr.ducruetl.BOTCPlugin.models.roles.folkstown.Virgin;
import fr.ducruetl.BOTCPlugin.models.roles.folkstown.Washerwoman;
import fr.ducruetl.BOTCPlugin.models.roles.outsiders.Butler;
import fr.ducruetl.BOTCPlugin.models.roles.outsiders.Drunk;
import fr.ducruetl.BOTCPlugin.models.roles.outsiders.Recluse;
import fr.ducruetl.BOTCPlugin.models.roles.outsiders.Saint;
import net.md_5.bungee.api.ChatColor;

public class Game {
    
    private ArrayList<GamePlayer> players;

    private int day;

    private GameState state;

    private List<Role> minionPool = List.of(
        new Poisoner(),
        new ScarletWoman(),
        new Spy()
    );

    private List<Role> outsidersPool = List.of(
        new Butler(),
        new Drunk(),
        new Saint(),
        new Recluse()
    );

    private List<Role> folkstownPool = List.of(
        new Chef(),
        new Empath(),
        new FortuneTeller(),
        new Investigator(),
        new Librarian(),
        new Mayor(),
        new Monk(),
        new Ravenkeeper(),
        new Slayer(),
        new Soldier(),
        new Undertaker(),
        new Virgin(),
        new Washerwoman()
    );

    // To keep track of the composition and show it at the end
    private Map<GamePlayer, Role> roleAttributionMap;

    public Game() {
        this.players = new ArrayList<>();
        this.day = 1;
        this.state = GameState.NIGHT;
        this.roleAttributionMap = new HashMap<>();
    }

    public ArrayList<GamePlayer> getPlayers() {
        return players;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Map<GamePlayer, Role> getRoleAttributionMap() {
        return roleAttributionMap;
    }

    public void startGame() {

        if (Bukkit.getServer().getOnlinePlayers().size() < 5) {
            Bukkit.broadcastMessage(ChatColor.RED 
                + "Il n'y a pas assez de joueurs pour commencer la partie (" 
                + (5 - Bukkit.getServer().getOnlinePlayers().size()) + " joueurs manquants)");
            return;
        }

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            getPlayers().add(new GamePlayer(player));
        }

        attributeRoles();

        // TODO: Start timer to start the game (move the timer from BOTCPlugin here)
    }

    public void attributeRoles() {
        List<Role> composition = new ArrayList<>();

        Collections.shuffle(minionPool);
        Collections.shuffle(outsidersPool);
        Collections.shuffle(folkstownPool);

        composition.add(new Imp());

        for (int i = 0; i < getMinionsCount(); i++) {
            composition.add(minionPool.get(i));
        }

        for (int i = 0; i < getOutsidersCount(); i++) {
            composition.add(outsidersPool.get(i));
        }

        for (int i = 0; i < getFolkstownCount(); i++) {
            composition.add(folkstownPool.get(i));
        }

        Collections.shuffle(composition);

        for (int i = 0; i < getPlayers().size(); i++) {
            GamePlayer player = getPlayers().get(i);

            player.setRole(composition.get(i));
            getRoleAttributionMap().put(player, player.getRole());
        }
    }

    public int getFolkstownCount() {
        int playersCount = getPlayers().size();

        return playersCount - 1 - getOutsidersCount() - getMinionsCount();
    }

    public int getMinionsCount() {
        int playersCount = getPlayers().size();

        return (int) Math.floor((playersCount - 1 - getOutsidersCount()) / 10 * 2);
    }

    public int getOutsidersCount() {
        int playersCount = getPlayers().size();

        return (playersCount - 1) % 3;
    }

}
