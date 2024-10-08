package me.marc3308.monsterplugin.eventlistener;

import me.marc3308.monsterplugin.Monsterplugin;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import static me.marc3308.monsterplugin.Monsterplugin.*;

public class oregen implements Listener {

    private static final Random random = new Random();
    private static final DecimalFormat f = new DecimalFormat("#0.00");

    @EventHandler
    public void onchunk(ChunkLoadEvent e){
        if (!e.isNewChunk())return;
        if(e.getChunk().getBlock(0,0,0).getX()>eiss.getInt("orgen"+".MaxX"))return;
        if(e.getChunk().getBlock(0,0,0).getZ()>eiss.getInt("orgen"+".MaxX"))return;
        if(e.getChunk().getBlock(0,0,0).getX()<-eiss.getInt("orgen"+".MaxZ"))return;
        if(e.getChunk().getBlock(0,0,0).getZ()<-eiss.getInt("orgen"+".MaxZ"))return;

        Monsterplugin.chunklust.add(e.getChunk().getBlock(0,0,0).getLocation());
    }

    public static void deletus(Location loc){
        Chunk chunk=loc.getChunk();
        // Only process newly generated chunks
        new BukkitRunnable() {
            int x = 0, y = chunk.getWorld().getMinHeight(), z = 0;

            @Override
            public void run() {
                for (int i = 0; i < 50; i++) { // Process 50 blocks per tick
                    Block block = chunk.getBlock(x, y, z);
                    Material type = block.getType();

                    if(block.getType().equals(Material.CHEST) || block.getType().equals(Material.SPAWNER)){
                        block.setBlockData(Material.AIR.createBlockData());
                        return;
                    }

                    if (eiss.get("orgen"+"."+block.getType())!=null) {
                        if (random.nextInt(1,101) > eiss.getInt("orgen"+"."+block.getType())){
                            block.setBlockData(block.getLocation().getY()>0 ? Material.STONE.createBlockData() : Material.DEEPSLATE.createBlockData());
                        }
                    }

                    z++;
                    if (z == 16) {
                        z = 0;
                        x++;
                    }
                    if (x == 16) {
                        x = 0;
                        y++;
                    }

                    if (y >=186) {//chunk.getWorld().getMaxHeight()
                        //safething.set(loc.toString().replace(".",""),loc.toString());
                        Bignumb++;
                        int a=((eiss.getInt("orgen"+".MaxX")*2)*(eiss.getInt("orgen"+".MaxZ")*2))/(16*16);
                        System.out.println("Chunk:"+(int)chunk.getBlock(0,0,0).getLocation().getX()+"x "
                                +(int)chunk.getBlock(0,0,0).getLocation().getZ()
                                +"z ist fertig generiert"+" ["+Bignumb+"/"+a+"]["+f.format((100.0/a)*Bignumb)+"%]");
                        cancel(); // Stop the task when done
                        break;
                    }
                }
            }
        }.runTaskTimer(Monsterplugin.getPlugin(), 1L, 1L); // Run every tick
    }

    public static void sortLocationsByMostNegativeX(ArrayList<Location> locations) {
        Collections.sort(locations, new Comparator<Location>() {
            @Override
            public int compare(Location loc1, Location loc2) {
                // First compare by X coordinate
                int result = Double.compare(loc1.getX(), loc2.getX());
                if (result != 0) {
                    return result;
                }
                // If X is the same, then compare by Z coordinate
                return Double.compare(loc1.getZ(), loc2.getZ());
            }
        });
    }

    public static void sortLocationsByMostPositiveX(ArrayList<Location> locations) {
        Collections.sort(locations, new Comparator<Location>() {
            @Override
            public int compare(Location loc1, Location loc2) {
                // First compare by X coordinate
                int result = Double.compare(loc2.getX(), loc1.getX());
                if (result != 0) {
                    return result;
                }
                // If X is the same, then compare by Z coordinate
                return Double.compare(loc2.getZ(), loc1.getZ());
            }
        });
    }

}
