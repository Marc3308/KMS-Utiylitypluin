package me.marc3308.monsterplugin.eventlistener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static org.apache.logging.log4j.LogManager.getLogger;

public class regiondelete implements Listener {

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        // Specify the path to the folder you want to delete
        String folderPath = "world/region";
        File folder = new File(folderPath);

        // Call the method to delete the folder
        if (deleteFolder(folder)) {
            getLogger().info("Successfully deleted the folder: " + folderPath);
        } else {
            getLogger().warn("Failed to delete the folder: " + folderPath);
        }

        // Specify the source and destination paths
        String sourcePath = "plugins/KMS Plugins/Terralith_1.21_v2.5.4.zip";
        String destinationPath = "world/datapacks/Terralith_1.21_v2.5.4.zip";

        // Create File objects for source and destination
        File sourceFile = new File(sourcePath);
        File destinationFile = new File(destinationPath);

        // Create the destination directory if it doesn't exist
        destinationFile.getParentFile().mkdirs();

        try {
            // Copy the file from source to destination
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            getLogger().info("Successfully copied the file to: " + destinationPath);
        } catch (IOException e) {
            getLogger().entry("Failed to copy the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to recursively delete a folder and its contents
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
