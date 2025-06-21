package me.marc3308.monsterplugin.objekts;

import me.marc3308.monsterplugin.Monsterplugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ChunkPreloader {

    private final World world = Bukkit.getWorld("world");
    private final int chunksPerTick = 4;
    private final int batchSize = 5000;

    private final List<Queue<ChunkCoord>> batchQueues = new ArrayList<>();
    private final Queue<ChunkCoord> chunkQueue = new LinkedList<>();
    private int currentBatchIndex = 0;

    private long start;
    private int finished = 0;
    private int total = 0;
    private int mult = 1;
    private boolean gen = false;

    private int batchFinished = 0;
    private int batchTotal = 0;

    public ChunkPreloader(int minX, int maxX, int minZ, int maxZ) {
        if (gen) return;
        gen = true;
        start = System.currentTimeMillis();

        Bukkit.getScheduler().runTaskAsynchronously(Monsterplugin.getPlugin(), () -> {
            Queue<ChunkCoord> currentBatch = new LinkedList<>();

            for (int z = minZ; z <= maxZ; z += 16) {
                for (int x = minX; x <= maxX; x += 16) {
                    int chunkX = x >> 4;
                    int chunkZ = z >> 4;
                    if (!world.isChunkGenerated(chunkX, chunkZ)) {
                        currentBatch.add(new ChunkCoord(chunkX, chunkZ));
                        if (currentBatch.size() >= batchSize) {
                            batchQueues.add(currentBatch);
                            currentBatch = new LinkedList<>();
                            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Batch of " + batchSize*batchQueues.size() + " chunks created.");
                        }
                    }
                }
            }

            if (!currentBatch.isEmpty()) {
                batchQueues.add(currentBatch);
            }

            total = batchQueues.stream().mapToInt(Queue::size).sum();
            Bukkit.getScheduler().runTask(Monsterplugin.getPlugin(), this::startNextBatch);

        });
    }

    private void startNextBatch() {
        if (currentBatchIndex >= batchQueues.size()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "✅ All chunk batches completed!");
            tryunload();
            long duration = (System.currentTimeMillis() - start) / 1000;
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Total time: " + (duration / 60) + "m " + (duration % 60) + "s");
            return;
        }

        chunkQueue.clear();
        Queue<ChunkCoord> batch = batchQueues.get(currentBatchIndex);
        chunkQueue.addAll(batch);
        batchTotal = batch.size();
        batchFinished = 0;
        currentBatchIndex++;

        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "▶ Starting batch " + currentBatchIndex +
                "/" + batchQueues.size() + " (" + batchTotal + " chunks)");

        startPreloading();
    }

    private void startPreloading() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (chunkQueue.isEmpty()) {
                    this.cancel(); // Wait for remaining async loads to finish
                    return;
                }

                int loadCount = Bukkit.getTPS()[0] >= 19 ? chunksPerTick * mult : chunksPerTick * Math.max(mult - 1, 1);
                for (int i = 0; i < loadCount && !chunkQueue.isEmpty(); i++) {
                    ChunkCoord coord = chunkQueue.poll();
                    world.getChunkAtAsync(coord.x, coord.z, true).thenAccept(chunk -> {
                        finished++;
                        batchFinished++;

                        if (finished % 10 == 0 || finished == total) {
                            int percent = (int) ((finished / (double) total) * 100);
                            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "✔ Processed: " +
                                    (chunk.getX() << 4) + ", " + (chunk.getZ() << 4) +
                                    " (" + finished + "/" + total + ") [" + percent + "%]");
                        }

                        if (batchFinished >= batchTotal) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "✅ Batch " + currentBatchIndex + " finished loading.");
                            tryunload();
                            Bukkit.getScheduler().runTask(Monsterplugin.getPlugin(), ChunkPreloader.this::startNextBatch);
                        }
                    });
                }

                if (Bukkit.getTPS()[0] >= 19 && mult < 8) mult++;
                else if (Bukkit.getTPS()[0] <= 18 && mult > 1) mult--;
            }
        }.runTaskTimer(Monsterplugin.getPlugin(), 0L, 1L);
    }

    private void tryunload() {
        int unloaded = 0;
        for (Chunk chunk : world.getLoadedChunks()) {
            int x = chunk.getX();
            int z = chunk.getZ();

            if (world.isChunkForceLoaded(x, z)) {
                world.setChunkForceLoaded(x, z, false);
            }

            boolean success = world.unloadChunk(x, z, true); // save = true
            if (success) unloaded++;
        }

        System.gc(); // Hint GC

        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "[Unload] Unloaded " + unloaded + " chunks.");
    }

    private static class ChunkCoord {
        final int x, z;

        ChunkCoord(int x, int z) {
            this.x = x;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ChunkCoord)) return false;
            ChunkCoord that = (ChunkCoord) o;
            return x == that.x && z == that.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, z);
        }
    }
}
