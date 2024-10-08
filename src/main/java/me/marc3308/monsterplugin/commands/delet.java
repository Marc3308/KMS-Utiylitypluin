package me.marc3308.monsterplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static org.apache.logging.log4j.LogManager.getLogger;

public class delet implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player)return false;
        String folderPath = "world";
        File folder = new File(folderPath);
        if (deleteFolder(folder)) {
            getLogger().info("Successfully deleted the folder: " + folderPath);
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "reload confirm");
        } else {
            getLogger().warn("Failed to delete the folder: " + folderPath);
        }
        return false;
    }

    private boolean deleteFolder(File folder) {
        if (!folder.exists()) {
            return false; // Folder does not exist
        }

        File[] files = folder.listFiles();
        if (files != null) { // If it's a directory, recursively delete contents
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    file.delete();
                }
            }
        }

        return folder.delete(); // Finally, delete the directory itself
    }
}
