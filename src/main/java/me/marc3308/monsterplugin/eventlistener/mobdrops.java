package me.marc3308.monsterplugin.eventlistener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import static me.marc3308.monsterplugin.Monsterplugin.eiss;

public class mobdrops implements Listener {

    @EventHandler
    public void ondrops(EntityDeathEvent e){

        if(eiss.get("nomobdrops"+"."+e.getEntity().getType())!=null && !eiss.getBoolean("nomobdrops"+"."+e.getEntity().getType()))e.getDrops().clear();
        if(eiss.get("nomobxp"+"."+e.getEntity().getType())!=null && !eiss.getBoolean("nomobxp"+"."+e.getEntity().getType()))e.setDroppedExp(0);

    }
}
