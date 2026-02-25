package fr.ducruetl.BOTCPlugin.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.ducruetl.BOTCPlugin.BOTCPlugin;
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
import org.bukkit.ChatColor;

public class Game {

    private BOTCPlugin plugin;
    
    private ArrayList<GamePlayer> players;

    private Queue<GamePlayer> nightOrder;

    private GamePlayer currentNightActor;

    private ArrayList<Role> composition;

    // To keep track of the composition and show it at the end
    private Map<GamePlayer, Role> roleAttributionMap;

    private int day;

    private GameState state;

    private List<Role> minionPool = new ArrayList<>(List.of(
        new Poisoner(),
        new ScarletWoman(),
        new Spy()
    ));

    private List<Role> outsidersPool = new ArrayList<>(List.of(
        new Butler(),
        new Drunk(),
        new Saint(),
        new Recluse()
    ));

    private List<Role> folkstownPool = new ArrayList<>(List.of(
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
    ));

    public Game(BOTCPlugin plugin) {
        this.plugin = plugin;
        this.players = new ArrayList<>();
        this.nightOrder = new PriorityQueue<>();
        this.composition = new ArrayList<>();
        this.day = 1;
        this.state = GameState.NIGHT;
        this.roleAttributionMap = new HashMap<>();
    }

    public BOTCPlugin getPlugin() {
        return plugin;
    }

    public ArrayList<GamePlayer> getPlayers() {
        return players;
    }

    public ArrayList<Role> getComposition() {
        return composition;
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

        Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Début de la partie !");

        attributeRoles();
        nextNight();
    }

    public void nextNight() {
        setState(GameState.NIGHT);
        Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "La nuit tombe...");
        
        PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, PotionEffect.INFINITE_DURATION, 1);
        for (GamePlayer player : getPlayers()) {
            player.getPlayer().addPotionEffect(blindness);
        }

        processNextNightAction();
    }

    public void processNextNightAction() {
        if (nightOrder.isEmpty()) {
            endNight();
            return;
        }

        currentNightActor = nightOrder.poll();
        currentNightActor.getRole().onNightTurn(this, currentNightActor);
    }

    public void endNight() {
        setState(GameState.DAY);
        Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "Fin de la nuit !");
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

        public void attributeRoles() {
        Collections.shuffle(minionPool);
        Collections.shuffle(outsidersPool);
        Collections.shuffle(folkstownPool);

        getComposition().add(new Imp());

        for (int i = 0; i < getMinionsCount(); i++) {
            getComposition().add(minionPool.get(i));
        }

        for (int i = 0; i < getOutsidersCount(); i++) {
            getComposition().add(outsidersPool.get(i));
        }

        for (int i = 0; i < getFolkstownCount(); i++) {
            getComposition().add(folkstownPool.get(i));
        }

        Collections.shuffle(getComposition());

        for (int i = 0; i < getPlayers().size(); i++) {
            GamePlayer player = getPlayers().get(i);

            player.setRole(getComposition().get(i));
            getRoleAttributionMap().put(player, player.getRole());

            sendRoleMessage(player);
        }
    }

    public void sendRoleMessage(GamePlayer player) {
        player.getPlayer().sendMessage("");
        player.getPlayer().sendMessage(ChatColor.BLUE + "Vous êtes " + ChatColor.YELLOW + "" + ChatColor.BOLD + player.getRole().getName());
        
        if (player.getRole().getTeam() == Team.DEMON 
                || player.getRole().getTeam() == Team.MINION) {
            player.getPlayer().sendMessage(ChatColor.BLUE + "Vous appartenez au camp des " + ChatColor.RED + "" + ChatColor.BOLD + "Démons");
        } else {
            player.getPlayer().sendMessage(ChatColor.BLUE + "Vous appartenez au camp des " + ChatColor.GREEN + "" + ChatColor.BOLD + "Citoyens");
        }

        player.getPlayer().sendMessage(ChatColor.BLUE + player.getRole().getDescription());
        player.getPlayer().sendMessage("");
    }

    public void broadcastPlayersRoles() {
        for (GamePlayer player : getRoleAttributionMap().keySet()) {
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + player.getPlayer().getDisplayName()
                + ChatColor.RESET + ChatColor.DARK_PURPLE + " : " + getRoleAttributionMap().get(player).getName());
        }
    }

}
