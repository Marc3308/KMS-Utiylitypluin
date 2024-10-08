package me.marc3308.monsterplugin.eventlistener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.ArrayList;

public class spiderspawn implements Listener {

    private final ArrayList<Material> Steine = new ArrayList<>();

    public spiderspawn(){

        Steine.add(Material.STONE);
        Steine.add(Material.GRANITE);
        Steine.add(Material.DIORITE);
        Steine.add(Material.ANDESITE);
        Steine.add(Material.COBBLESTONE);
        Steine.add(Material.BLACKSTONE);

    }

    @EventHandler
    public void onspawn(EntitySpawnEvent e){

        //check if spider
        if(!(e.getEntity() instanceof Spider))return;
        if(e.getEntity() instanceof CaveSpider)return;
        Spider p=(Spider) e.getEntity();

        //check if in cave
        if(p.getLocation().getBlock().getLightLevel()>=7)return;
        Block block = p.getLocation().getBlock();
        if(block.getLocation().getBlockY()==p.getWorld().getHighestBlockAt(p.getLocation()).getLocation().add(0,1,0).getBlockY())return;

        while (block.getType().isAir()){
            block = block.getRelative(0,1,0);
        }

        if(!Steine.contains(block.getType()))return;

        e.setCancelled(true);

        Mob spinne= (Mob) e.getEntity().getWorld().spawnEntity(p.getLocation(), EntityType.CAVE_SPIDER);

    }

}
