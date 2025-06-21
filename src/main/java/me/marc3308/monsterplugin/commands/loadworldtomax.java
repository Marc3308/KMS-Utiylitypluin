package me.marc3308.monsterplugin.commands;

import me.marc3308.monsterplugin.objekts.ChunkPreloader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class loadworldtomax implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player)return false;
        if(args.length<1){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED +"Zahl");
            return false;
        }

        int maxX = Integer.parseInt(args[0]);
        int minX = -Integer.parseInt(args[0]);

        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN+"Start Loading Chunks to " + minX + " / " + maxX);

        new ChunkPreloader(minX, maxX, minX, maxX);

        return false;
    }
}
