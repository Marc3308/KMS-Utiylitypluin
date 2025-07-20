package me.marc3308.monsterplugin.objekts;

import me.marc3308.monsterplugin.Monsterplugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Chunkeater {


    private final World world = Bukkit.getWorld("world");
    private final int chunksPerTick = 4;
    private final Queue<ChunkCoord> chunkQueue = new LinkedList<>();
    private final Random random = new Random();
    private long start;
    private int finished = 0;
    private int total= 0;
    private int replaced=0;

    public Chunkeater(int minX, int maxX, int minZ, int maxZ) {

        start = System.currentTimeMillis();

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Generating ... "+minX+"/"+maxX+"/"+minZ+"/"+maxZ);

        Bukkit.getScheduler().runTaskAsynchronously(Monsterplugin.getPlugin(), () -> {
            for (int z = minZ; z <= maxZ; z += 16) {
                for (int x = minX; x <= maxX; x += 16) {
                    int chunkX = x >> 4;
                    int chunkZ = z >> 4;
                    chunkQueue.add(new ChunkCoord(chunkX, chunkZ));
                    if(chunkQueue.size()>=2000 && chunkQueue.size()%2000==0){
                        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Catched " + chunkQueue.size() + " chunks...");
                    }
                }
            }

            total = chunkQueue.size();
            Bukkit.getScheduler().runTask(Monsterplugin.getPlugin(), () -> {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Preloading " + total + " chunks...");
                startPreloading();
            });
        });
    }

    private void startPreloading() {
        new BukkitRunnable() {
            @Override
            public void run() {

                if(Bukkit.getTPS()[0] >= 10) {
                    if (chunkQueue.isEmpty()) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Chunk preloading Que completed!");
                        this.cancel();
                        return;
                    }

                    for (int i = 0; i < chunksPerTick && !chunkQueue.isEmpty(); i++) {
                        ChunkCoord coord = chunkQueue.poll();
                        world.getChunkAtAsync(coord.x, coord.z, true).thenAccept(chunk -> {
                            // Async scan, sync replace
                            Bukkit.getScheduler().runTask(Monsterplugin.getPlugin(), () -> {
                                int bx = chunk.getX() << 4; // chunk base X
                                int bz = chunk.getZ() << 4; // chunk base Z
                                for (int x = 0; x < 16; x++) {
                                    for (int z = 0; z < 16; z++) {
                                        for (int y = world.getMinHeight(); y < world.getMaxHeight(); y++) {
                                            Block block = world.getBlockAt(bx + x, y, bz + z);
                                            if(Monsterplugin.settings.isDenied() && Monsterplugin.settings.getDeniedblocks().contains(block.getType())){
                                                block.setType(Material.AIR, false); // 'false' skips physics
                                                replaced++;
                                            } else if (Monsterplugin.settings.isPresent() && Monsterplugin.settings.getPersentblocks().containsKey(block.getType()) && Monsterplugin.settings.getPersentblocks().get(block.getType())<random.nextInt(1,100)){
                                                block.setType(block.getLocation().getY()>0 ? Material.STONE : Material.DEEPSLATE, false); // 'false' skips physics
                                                replaced++;
                                            }
                                        }
                                    }
                                }
                                finished++;
                                if (finished % 50 == 0 || finished == total) {
                                    int percent = (int) ((finished / (double) total) * 100);
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "✔ Processed: " +
                                            (chunk.getX() << 4) + ", " + (chunk.getZ() << 4) +
                                            " (" + finished + "/" + total + ") [" + percent + "%] | Replaced: " + replaced + " blocks");
                                    replaced = 0;
                                    if(finished >= total) {
                                        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Chunk preloading completed in " + (System.currentTimeMillis() - start) + "ms");
                                        tryunload();
                                        this.cancel();
                                    }
                                }
                                if (finished>=2000 && finished%2000 == 0) {
                                    tryunload();
                                }
                            });
                        });
                    }
                } else {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[PAUSE] Wir pausieren den Chunk-Eater, da der Server zu wenig TPS hat: "+Bukkit.getTPS()[0]);
                }
            }
        }.runTaskTimer(Monsterplugin.getPlugin(), 0, 1); // runs every tick
    }


    private void tryunload() {
        for (Chunk chunk : world.getLoadedChunks()) {
            int x = chunk.getX();
            int z = chunk.getZ();
            if (world.isChunkForceLoaded(x, z))world.setChunkForceLoaded(x, z, false);
            world.unloadChunk(x, z, true); // save = true
        }
        //bosbar just for the fun
        BossBar bar = Bukkit.createBossBar(finished + " / "+total, BarColor.RED, BarStyle.SEGMENTED_20);
        bar.setColor(finished/total>=0.6 ? BarColor.GREEN
                : finished/total>=0.3 ? BarColor.YELLOW
                : BarColor.RED);
        bar.setProgress(finished/total);
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.activeBossBars().forEach(b ->p.hideBossBar(b));
        });
        System.gc(); // Hint GC
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[Cleard] Some chunks to free memory.");
    }

    static class ChunkCoord {
        final int x, z;
        ChunkCoord(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }
}
