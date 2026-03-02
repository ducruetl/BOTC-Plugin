package fr.ducruetl.BOTCPlugin.gameobjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.ducruetl.BOTCPlugin.BOTCPlugin;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.Role;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.demons.Imp;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.demons.Poisoner;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.demons.ScarletWoman;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.demons.Spy;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown.Chef;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown.Empath;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown.FortuneTeller;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown.Investigator;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown.Librarian;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown.Mayor;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown.Monk;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown.Ravenkeeper;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown.Slayer;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown.Soldier;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown.Undertaker;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown.Virgin;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.folkstown.Washerwoman;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Butler;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Drunk;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Recluse;
import fr.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Saint;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Game {

    private BOTCPlugin plugin;
    
    private ArrayList<GamePlayer> players;

    private Map<Player, GamePlayer> playersToGamePlayers;

    private ArrayList<GamePlayer> nominatedPlayers;

    private Map<GamePlayer, Integer> playersVotes;

    private Queue<GamePlayer> nightOrder;

    private GamePlayer currentNightActor;

    private GamePlayer selectedPlayer;

    private GamePlayer lastKilledByImp;

    private GamePlayer lastKilledByVote;

    private GamePlayer lastPoisoned;

    private GamePlayer lastProtected;

    private ArrayList<Role> composition;

    private ArrayList<Role> outOfComposition;

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
        this.playersToGamePlayers = new HashMap<>();
        this.nominatedPlayers = new ArrayList<>();
        this.playersVotes = new HashMap<>();
        this.nightOrder = new PriorityQueue<>(
            Comparator.comparingInt(gp -> {
                Role role = gp.getRole();

                if (role instanceof Drunk) {
                    return gp.getFacadeRole().getNightPriority();
                }

                return role.getNightPriority();
            })
        );
        this.composition = new ArrayList<>();
        this.outOfComposition = new ArrayList<>();
        this.day = 1;
        this.state = GameState.NOT_STARTED;
        this.roleAttributionMap = new HashMap<>();
    }

    public BOTCPlugin getPlugin() {
        return plugin;
    }

    public ArrayList<GamePlayer> getPlayers() {
        return players;
    }

    public int getAlivePlayersCount() {
        int count = 0;
        for (GamePlayer player : players) {
            if (!player.isDead()) {
                count++;
            }
        }

        return count;
    }

    public Map<Player, GamePlayer> getPlayersToGamePlayers() {
        return playersToGamePlayers;
    }

    public ArrayList<GamePlayer> getNominatedPlayers() {
        return nominatedPlayers;
    }

    public Map<GamePlayer, Integer> getPlayersVotes() {
        return playersVotes;
    }

    public ArrayList<Role> getComposition() {
        return composition;
    }

    public ArrayList<Role> getOutOfComposition() {
        return outOfComposition;
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

    public Queue<GamePlayer> getNightOrder() {
        return nightOrder;
    }

    public GamePlayer getCurrentNightActor() {
        return currentNightActor;
    }

    public void setCurrentNightActor(GamePlayer currentNightActor) {
        this.currentNightActor = currentNightActor;
    }

    public GamePlayer getSelectedPlayer() {
        return selectedPlayer;
    }

    public void setSelectedPlayer(GamePlayer selectedPlayer) {
        this.selectedPlayer = selectedPlayer;
    }

    public GamePlayer getLastKilledByImp() {
        return lastKilledByImp;
    }

    public void setLastKilledByImp(GamePlayer lastKilledByImp) {
        this.lastKilledByImp = lastKilledByImp;
    }

    public GamePlayer getLastKilledByVote() {
        return lastKilledByVote;
    }

    public void setLastKilledByVote(GamePlayer lastKilledByVote) {
        this.lastKilledByVote = lastKilledByVote;
    }

    public GamePlayer getLastPoisoned() {
        return lastPoisoned;
    }

    public void setLastPoisoned(GamePlayer lastPoisoned) {
        this.lastPoisoned = lastPoisoned;
    }

    public GamePlayer getLastProtected() {
        return lastProtected;
    }

    public void setLastProtected(GamePlayer lastProtected) {
        this.lastProtected = lastProtected;
    }

    /**
     * If there are enough players online (5 minimum), start the game
     */
    public void startGame() {
        if (Bukkit.getServer().getOnlinePlayers().size() < 5) {
            Bukkit.broadcastMessage(ChatColor.RED 
                + "Il n'y a pas assez de joueurs pour commencer la partie (" 
                + (5 - Bukkit.getServer().getOnlinePlayers().size()) + " joueurs manquants)");
            return;
        }

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            GamePlayer gamePlayer = new GamePlayer(player);
            getPlayers().add(gamePlayer);
            getPlayersToGamePlayers().put(player, gamePlayer);
        }

        Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Début de la partie !");

        attributeRoles();
        Game game = this;
        new BukkitRunnable() {

            @Override
            public void run() {
                NightActions.nextNight(game);
            }   

        }.runTaskLater(plugin, 20 * 7); // 7 seconds, to let the role title before starting the game
    }

    /**
     * Calculate the number of folkstown roles that should be present in composition, based on the number of players
     * @return The suggested number of folkstown roles based on the number of players
     */
    public int getFolkstownCount() {
        int playersCount = getPlayers().size();

        return playersCount - 1 - getOutsidersCount() - getMinionsCount();
    }

    /**
     * Calculate the number of minions roles that should be present in composition, based on the number of players
     * @return The suggested number of minions roles based on the number of players
     */
    public int getMinionsCount() {
        int playersCount = getPlayers().size();

        return (int) Math.floor((playersCount - 1 - getOutsidersCount()) / 10 * 2);
    }

    /**
     * Calculate the number of outsiders roles that should be present in composition, based on the number of players
     * @return The suggested number of outsiders roles based on the number of players
     */
    public int getOutsidersCount() {
        int playersCount = getPlayers().size();

        return (playersCount - 1) % 3;
    }

    /**
     * Attribute a role to each player in the game
     */
    public void attributeRoles() {
        Collections.shuffle(minionPool);
        Collections.shuffle(outsidersPool);
        Collections.shuffle(folkstownPool);

        getComposition().add(new Imp());

        for (int i = 0; i < getMinionsCount(); i++) {
            getComposition().add(minionPool.get(i));
        }

        for (int i = getMinionsCount(); i < minionPool.size(); i++) {
            getOutOfComposition().add(minionPool.get(i));
        }

        for (int i = 0; i < getOutsidersCount(); i++) {
            getComposition().add(outsidersPool.get(i));
        }

        for (int i = getOutsidersCount(); i < outsidersPool.size(); i++) {
            getOutOfComposition().add(outsidersPool.get(i));
        }

        for (int i = 0; i < getFolkstownCount(); i++) {
            getComposition().add(folkstownPool.get(i));
        }

        for (int i = getFolkstownCount(); i < folkstownPool.size(); i++) {
            getOutOfComposition().add(folkstownPool.get(i));
        }

        Collections.shuffle(getComposition());
        Collections.shuffle(getOutOfComposition());

        for (int i = 0; i < getPlayers().size(); i++) {
            GamePlayer player = getPlayers().get(i);

            player.setRole(getComposition().get(i));
            getRoleAttributionMap().put(player, player.getRole());

            if (player.getRole() instanceof Drunk) {
                int j = 0;
                while (getOutOfComposition().get(j).getTeam() != Team.TOWNSFOLK) {
                    j++;
                }

                player.setFacadeRole(getOutOfComposition().get(j));
                player.setFacadeTeam(Team.TOWNSFOLK);
            }

            sendRoleMessage(player);
        }
    }

    /**
     *  Send his role description message to a player
     * @param player The player to send a message
     */
    public void sendRoleMessage(GamePlayer player) {
        String roleName = player.getRole() instanceof Drunk ? player.getFacadeRole().getName() : player.getRole().getName();
        String roleDesc = player.getRole() instanceof Drunk ? player.getFacadeRole().getDescription() : player.getRole().getDescription();

        player.getPlayer().sendTitle(ChatColor.BLUE + "Vous êtes " + ChatColor.YELLOW + "" + ChatColor.BOLD + roleName, null, 20, 80, 20);

        player.getPlayer().sendMessage("");
        player.getPlayer().sendMessage(ChatColor.BLUE + "Vous êtes " + ChatColor.YELLOW + "" + ChatColor.BOLD + roleName);
        
        if (player.getRole().getTeam() == Team.DEMON 
                || player.getRole().getTeam() == Team.MINION) {
            player.getPlayer().sendMessage(ChatColor.BLUE + "Vous appartenez au camp des " + ChatColor.RED + "" + ChatColor.BOLD + "Démons");
        } else {
            player.getPlayer().sendMessage(ChatColor.BLUE + "Vous appartenez au camp des " + ChatColor.GREEN + "" + ChatColor.BOLD + "Citoyens");
        }

        player.getPlayer().sendMessage(ChatColor.BLUE + roleDesc);
        player.getPlayer().sendMessage("");
    }

    /**
     * Broadcast the roles of all players
     */
    public void broadcastPlayersRoles() {
        for (GamePlayer player : getRoleAttributionMap().keySet()) {
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + player.getPlayer().getDisplayName()
                + ChatColor.RESET + ChatColor.DARK_PURPLE + " : " + getRoleAttributionMap().get(player).getName());
        }
    }

    /**
     * Create an inventory with an head for each player.
     * @return The created inventory
     */
    public Inventory createTargetSelectionInventory() {
        int size = ((getPlayers().size() - 1) / 9 + 1) * 9; // multiple of 9
        Inventory inv = Bukkit.createInventory(null, size, "Choose a player");

        for (GamePlayer target : getPlayers()) {
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setOwningPlayer(target.getPlayer());
            meta.setDisplayName(ChatColor.YELLOW + target.getPlayer().getName());

            head.setItemMeta(meta);
            inv.addItem(head);
        }

        return inv;
    }

}
