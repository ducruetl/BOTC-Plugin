package fr.ducruetl.BOTCPlugin.models;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Game {
    
    private ArrayList<GamePlayer> players;

    private int day;

    private GameState state;

    public Game() {
        this.players = new ArrayList<>();
        this.day = 1;
        this.state = GameState.NIGHT;
    }

    public ArrayList<GamePlayer> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<GamePlayer> players) {
        this.players = players;
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

    public void startGame() {
        ArrayList<GamePlayer> players = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            players.add(new GamePlayer(player));
        }

        if (players.size() < 5) {
            Bukkit.broadcastMessage(ChatColor.RED 
                + "Il n'y a pas assez de joueurs pour commencer la partie (" 
                + (5 - players.size()) + " joueurs manquants)");
            return;
        }

        setPlayers(players);
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
