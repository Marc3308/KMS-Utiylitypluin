package me.marc3308.monsterplugin.eventlistener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import static me.marc3308.monsterplugin.Monsterplugin.eiss;

public class spawnerdelete implements Listener {

    @EventHandler
    public void onspawn(EntitySpawnEvent e){
        if(eiss.get("mobs"+"."+e.getEntity().getType())!=null && !eiss.getBoolean("mobs"+"."+e.getEntity().getType()))e.setCancelled(true);
    }
}
