package net.ducruetl.BOTCPlugin.gameobjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.ducruetl.BOTCPlugin.BOTCPlugin;
import net.ducruetl.BOTCPlugin.gameobjects.roles.Role;
import net.ducruetl.BOTCPlugin.gameobjects.roles.demons.Imp;
import net.ducruetl.BOTCPlugin.gameobjects.roles.demons.Poisoner;
import net.ducruetl.BOTCPlugin.gameobjects.roles.demons.ScarletWoman;
import net.ducruetl.BOTCPlugin.gameobjects.roles.demons.Spy;
import net.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Butler;
import net.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Drunk;
import net.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Recluse;
import net.ducruetl.BOTCPlugin.gameobjects.roles.outsiders.Saint;
import net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks.Chef;
import net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks.Empath;
import net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks.FortuneTeller;
import net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks.Investigator;
import net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks.Librarian;
import net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks.Mayor;
import net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks.Monk;
import net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks.Ravenkeeper;
import net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks.Slayer;
import net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks.Soldier;
import net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks.Undertaker;
import net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks.Virgin;
import net.ducruetl.BOTCPlugin.gameobjects.roles.townsfolks.Washerwoman;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;

import org.bukkit.Material;

public class Game {

    private BOTCPlugin plugin;
    
    private ArrayList<GamePlayer> players;

    private Map<UUID, GamePlayer> uuidToGamePlayers;

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

    private List<Role> townsfolksPool = new ArrayList<>(List.of(
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
        this.uuidToGamePlayers = new HashMap<>();
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

    public Map<UUID, GamePlayer> getUuidToGamePlayers() {
        return uuidToGamePlayers;
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

    public List<Role> getTownsfolksPool() {
        return townsfolksPool;
    }

    public List<Role> getMinionPool() {
        return minionPool;
    }

    public List<Role> getOutsidersPool() {
        return outsidersPool;
    }

    /**
     * If there are enough players online (5 minimum), start the game
     */
    public void startGame() {
        if (Bukkit.getServer().getOnlinePlayers().size() < 5) {
            final Component message = MiniMessage.miniMessage().deserialize(
                "<red>Il n'y a pas assez de joueurs pour commencer la partie (<playernum> joueurs manquants)</red>",
                Placeholder.unparsed("playernum", String.valueOf(5 - Bukkit.getOnlinePlayers().size()))
            );

            Bukkit.broadcast(message);
            return;
        } else if (plugin.getRoomPositions().size() < Bukkit.getServer().getOnlinePlayers().size()) {
            final Component message = MiniMessage.miniMessage().deserialize(
                "<red>Il n'y a pas assez de rooms pour accueillir tous les joueurs (<num> rooms manquantes)</red>",
                Placeholder.unparsed("num", String.valueOf(Bukkit.getOnlinePlayers().size() - plugin.getRoomPositions().size()))
            );

            Bukkit.broadcast(message);
            return;
        } else if (plugin.getSeatPositions().size() < Bukkit.getServer().getOnlinePlayers().size()) {
            final Component message = MiniMessage.miniMessage().deserialize(
                "<red>Il n'y a pas assez de seats pour accueillir tous les joueurs (<num> seats manquantes)</red>",
                Placeholder.unparsed("num", String.valueOf(Bukkit.getOnlinePlayers().size() - plugin.getSeatPositions().size()))
            );

            Bukkit.broadcast(message);
            return;
        }

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            GamePlayer gamePlayer = new GamePlayer(player);
            getPlayers().add(gamePlayer);
            getUuidToGamePlayers().put(player.getUniqueId(), gamePlayer);
        }

        final Component message = MiniMessage.miniMessage().deserialize("<yellow><bold>Début de la partie !</bold></yellow>");
        Bukkit.broadcast(message);

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
    public int getTownsfolksCount() {
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
        Collections.shuffle(townsfolksPool);

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

        for (int i = 0; i < getTownsfolksCount(); i++) {
            getComposition().add(townsfolksPool.get(i));
        }

        for (int i = getTownsfolksCount(); i < townsfolksPool.size(); i++) {
            getOutOfComposition().add(townsfolksPool.get(i));
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
        String color;
        String colorEnd;

        if (player.getRole().getTeam() == Team.TOWNSFOLK) {
            color = "<green>";
            colorEnd = "</green>";
        } else if (player.getRole().getTeam() == Team.OUTSIDER) {
            color = "<aqua>";
            colorEnd = "</aqua>";
        } else {
            color = "<red>";
            colorEnd = "</red>";
        }

        String roleName = player.getRole() instanceof Drunk ? player.getFacadeRole().getName() : player.getRole().getName();
        String roleDesc = player.getRole() instanceof Drunk ? player.getFacadeRole().getDescription() : player.getRole().getDescription();

        final Component titleComponent = MiniMessage.miniMessage().deserialize(
            "<blue>Vous êtes</blue> " + color + "<role>" + colorEnd,
            Placeholder.unparsed("role", roleName)
        );

        Title title = Title.title(titleComponent, null);
        player.getPlayer().showTitle(title);

        player.getPlayer().sendMessage(Component.empty());

        final Component roleMessage = MiniMessage.miniMessage().deserialize(
            "<blue>Vous êtes</blue> " + color + "<role>" + colorEnd,
            Placeholder.unparsed("role", roleName)
        );
        player.getPlayer().sendMessage(roleMessage);
        
        if (player.getRole().getTeam() == Team.DEMON 
                || player.getRole().getTeam() == Team.MINION) {
            final Component teamMessage = MiniMessage.miniMessage().deserialize(
                "<blue>Vous appartenez au camp des</blue> <red><bold>Démons</bold></red>"
            );
            player.getPlayer().sendMessage(teamMessage);
        } else {
            final Component teamMessage = MiniMessage.miniMessage().deserialize(
                "<blue>Vous appartenez au camp des</blue> <green><bold>Démons</bold></green>"
            );
            player.getPlayer().sendMessage(teamMessage);
        }

        final Component descMessage = MiniMessage.miniMessage().deserialize(
                "<blue><roleDesc></blue>",
                Placeholder.unparsed("roleDesc", roleDesc)
            );
        player.getPlayer().sendMessage(descMessage);
        player.getPlayer().sendMessage(Component.empty());
    }

    /**
     * Broadcast the roles of all players
     */
    public void broadcastPlayersRoles() {
        for (GamePlayer player : getRoleAttributionMap().keySet()) {
            final Component message = MiniMessage.miniMessage().deserialize(
                "<dark_purple><bold><name></bold> : <role></dark_purple>",
                Placeholder.unparsed("name", player.getPlayer().getName()),
                Placeholder.unparsed("role", getRoleAttributionMap().get(player).getName())
            );
            Bukkit.broadcast(message);
        }
    }

    /**
     * Create an inventory with an head for each player.
     * @return The created inventory
     */
    public Inventory createTargetSelectionInventory() {
        int size = ((getPlayers().size() - 1) / 9 + 1) * 9; // multiple of 9
        Inventory inv = Bukkit.createInventory(null, size, Component.text("Choose a player"));

        for (GamePlayer target : getPlayers()) {
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setOwningPlayer(target.getPlayer());
            final Component displayName = MiniMessage.miniMessage().deserialize(
                "<yellow><name></yellow>",
                Placeholder.unparsed("name", target.getPlayer().getName())
            );
            meta.displayName(displayName);

            head.setItemMeta(meta);
            inv.addItem(head);
        }

        return inv;
    }

}
