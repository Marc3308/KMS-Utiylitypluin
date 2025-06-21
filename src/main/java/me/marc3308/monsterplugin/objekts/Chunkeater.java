package me.marc3308.monsterplugin.objekts;

import me.marc3308.monsterplugin.Monsterplugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Chunkeater {


    private final World world = Bukkit.getWorld("world");
    private final int chunksPerTick = 4;
    private final int unloadIntervalTicks = 100; // every 5 seconds
    private final Queue<ChunkCoord> chunkQueue = new LinkedList<>();
    private final List<ChunkCoord> loadedChunks = new ArrayList<>();
    private final Random random = new Random();
    private long start;
    private int finished = 0;
    private int total= 0;

    public Chunkeater(int minX, int maxX, int minZ, int maxZ) {

        start = System.currentTimeMillis();

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Generating ... "+minX+"/"+maxX+"/"+minZ+"/"+maxZ);


        Bukkit.getScheduler().runTaskAsynchronously(Monsterplugin.getPlugin(), () -> {
            for (int z = minZ; z <= maxZ; z += 16) {
                for (int x = minX; x <= maxX; x += 16) {
                    int chunkX = x >> 4;
                    int chunkZ = z >> 4;
                    chunkQueue.add(new ChunkCoord(chunkX, chunkZ));
                }
            }

            total = chunkQueue.size();
            Bukkit.getScheduler().runTask(Monsterplugin.getPlugin(), () -> {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Preloading " + total + " chunks...");
                startPreloading(total);
                startUnloadTask();
            });
        });
    }

    private void startPreloading(int totalChunks) {
        new BukkitRunnable() {
            @Override
            public void run() {

                if(Bukkit.getTPS()[0] >= 10) {
                    if (chunkQueue.isEmpty()) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Chunk preloading completed!");
                        this.cancel();
                        return;
                    }

                    for (int i = 0; i < chunksPerTick && !chunkQueue.isEmpty(); i++) {
                        ChunkCoord coord = chunkQueue.poll();
                        world.getChunkAtAsync(coord.x, coord.z, true).thenAccept(chunk -> {
                            synchronized (loadedChunks) {
                                loadedChunks.add(coord);
                            }
                            // Async scan, sync replace
                            Bukkit.getScheduler().runTask(Monsterplugin.getPlugin(), () -> {
                                int replaced = 0;

                                int bx = chunk.getX() << 4; // chunk base X
                                int bz = chunk.getZ() << 4; // chunk base Z

                                for (int x = 0; x < 16; x++) {
                                    for (int z = 0; z < 16; z++) {
                                        for (int y = 0; y < world.getMaxHeight(); y++) {
                                            Block block = world.getBlockAt(bx + x, y, bz + z);

                                            if(Monsterplugin.settings.isDenied() && Monsterplugin.settings.getDeniedblocks().contains(block.getType())){
                                                block.setType(Material.AIR, false); // 'false' skips physics
                                                replaced++;
                                            } else if (Monsterplugin.settings.isPresent() && Monsterplugin.settings.getPersentblocks().containsKey(block.getType()) && Monsterplugin.settings.getPersentblocks().get(block.getType())>= random.nextInt(100)) {
                                                block.setType(block.getLocation().getY()>0 ? Material.STONE : Material.DEEPSLATE, false); // 'false' skips physics
                                                replaced++;
                                            }
                                        }
                                    }
                                }

                                finished++;
                                int percent = (int) ((finished / (double) totalChunks) * 100);

                                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Processed chunk: " + bx + ", " + bz +
                                " (" + finished + "/" + totalChunks + ") [" + percent + "%] | Replaced: " + replaced);
                            });
                        });
                    }
                } else {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[PAUSE] Wir pausieren den Chunk-Eater, da der Server zu wenig TPS hat: "+Bukkit.getTPS()[0]);
                }
            }
        }.runTaskTimer(Monsterplugin.getPlugin(), 0, 1); // runs every tick
    }

    private void startUnloadTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                int unloaded =0;
                List<ChunkCoord> toUnload;
                synchronized (loadedChunks) {
                    if (loadedChunks.isEmpty() && finished >= total) {
                        long duration = (System.currentTimeMillis() - start) / 1000;
                        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Dauerte: " + (int) (duration/60) +"m "+ duration%60 +"sec");
                        Monsterplugin.settings.setStart(false);
                        return;
                    }
                    toUnload = new ArrayList<>(loadedChunks);
                    loadedChunks.clear();
                }

                for (ChunkCoord coord : toUnload) {
                    if (world.isChunkLoaded(coord.x, coord.z)) {
                        boolean c = world.unloadChunk(coord.x, coord.z,true);
                        if(c)unloaded++;
                    }
                }

                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Unloaded " + toUnload.size() + " chunks : " + unloaded + " successfully.");
            }
        }.runTaskTimer(Monsterplugin.getPlugin(), unloadIntervalTicks, unloadIntervalTicks);
    }

    private static class ChunkCoord {
        final int x, z;
        ChunkCoord(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }
}
