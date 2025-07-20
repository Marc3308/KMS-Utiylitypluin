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

    private boolean cancelled = false;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (cancelled){
            System.out.println("Already running");
            return false;
        }
        if(sender instanceof Player)return false;
        if(args.length<2 || Bukkit.getWorld(args[1]) == null){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED +"<Zahl> <Worldname>");
            return false;
        }

        cancelled=true;

        int maxX = Integer.parseInt(args[0]);
        int minX = -Integer.parseInt(args[0]);

        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN+"Start Loading Chunks to " + minX + " / " + maxX);

        new ChunkPreloader(minX, maxX, minX, maxX,Bukkit.getWorld(args[1]));

        return false;
    }
}
