package me.marc3308.monsterplugin.eventlistener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import static me.marc3308.monsterplugin.Monsterplugin.eiss;

public class infentare implements Listener {

    @EventHandler
    public void oninf(PlayerInteractEvent e){
        if(e.getClickedBlock()==null)return;
        if (eiss.get("Blocks"+"."+e.getClickedBlock().getType())!=null) {
            if (!eiss.getBoolean("Blocks"+"."+e.getClickedBlock().getType())){
                e.getClickedBlock().getWorld().setBlockData(e.getClickedBlock().getLocation(),Material.AIR.createBlockData());
                e.getPlayer().closeInventory();
            }
        }
    }
}
