package me.marc3308.monsterplugin.eventlistener;

import me.marc3308.monsterplugin.Monsterplugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.Collections;
import java.util.List;

import static me.marc3308.monsterplugin.Monsterplugin.eiss;

public class nogrow implements Listener {

    @EventHandler
    public void onrechtscklick(PlayerInteractEvent e){
        Player p=e.getPlayer();
        if(e.getClickedBlock()==null)return;
        if(e.getAction().isLeftClick())return;
        if(!e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SHEARS))return;
        if(eiss.get("Wachstum"+"."+e.getClickedBlock().getType())!=null && !eiss.getBoolean("Wachstum"+"."+e.getClickedBlock().getType())){
            if(e.getClickedBlock().hasMetadata("stopGrowth") && e.getClickedBlock().getMetadata("stopGrowth").get(0).asBoolean() && p.isSneaking()){
                e.getClickedBlock().setMetadata("stopGrowth", new FixedMetadataValue(Monsterplugin.getPlugin(), false));
                p.sendMessage(ChatColor.GREEN+"Diese Plflanze wächst nun  wieder");
            } else {
                if(e.getClickedBlock().hasMetadata("stopGrowth") && !e.getClickedBlock().getMetadata("stopGrowth").get(0).asBoolean() && !p.isSneaking()){
                    e.getClickedBlock().setMetadata("stopGrowth", new FixedMetadataValue(Monsterplugin.getPlugin(), true));
                    p.sendMessage(ChatColor.DARK_GREEN+"Diese Plflanze wächst nun nicht mehr");
                } else if(!e.getClickedBlock().hasMetadata("stopGrowth")){
                    e.getClickedBlock().setMetadata("stopGrowth", new FixedMetadataValue(Monsterplugin.getPlugin(), true));
                    p.sendMessage(ChatColor.DARK_GREEN+"Diese Plflanze wächst nun nicht mehr");
                }
            }
        }
    }

    @EventHandler
    public void onwachsen(BlockSpreadEvent e){
        if(eiss.get("Wachstum"+"."+e.getSource().getType())==null)return;
        if(eiss.getBoolean("Wachstum"+"."+e.getSource().getType()))return;
        if(e.getBlock().hasMetadata("stopGrowth") && !e.getBlock().getMetadata("stopGrowth").get(0).asBoolean())return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onbreack(BlockBreakEvent e){
        if(e.getBlock().hasMetadata("stopGrowth") && e.getBlock().getMetadata("stopGrowth").get(0).asBoolean()){
            e.getBlock().setMetadata("stopGrowth", new FixedMetadataValue(Monsterplugin.getPlugin(), false));
        }
    }

}
