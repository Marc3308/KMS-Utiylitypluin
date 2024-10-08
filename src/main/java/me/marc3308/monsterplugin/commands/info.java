package me.marc3308.monsterplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class info implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player)return false;
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN +"Biom ist: "+Bukkit.getWorlds().get(0).getBiome(new Location(Bukkit.getWorlds().get(0),0,0,0)).name());
        return false;
    }
}
