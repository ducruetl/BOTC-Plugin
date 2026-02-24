package fr.ducruetl.BOTCPlugin.listeners;

import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldListner implements Listener {
    
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();        
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setPVP(false);
        world.setSpawnLocation(0, world.getHighestBlockYAt(0, 0) + 1, 0);
    }

}
