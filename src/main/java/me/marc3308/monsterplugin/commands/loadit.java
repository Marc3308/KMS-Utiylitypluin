package me.marc3308.monsterplugin.commands;

import me.marc3308.monsterplugin.Monsterplugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import static me.marc3308.monsterplugin.Monsterplugin.eiss;

public class loadit implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player)return false;
        if(args.length<1){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED +"Falsch Benutzt");
            return false;
        }
        try {
            Player p=Bukkit.getPlayer(args[0]);
            final int[] x = {args.length==1 ? eiss.getInt("orgen" + ".MaxX") * (-1) : Integer.valueOf(args[1])};
            final int[] z = {args.length==1 ? eiss.getInt("orgen" + ".MaxZ") * (-1) : Integer.valueOf(args[2])};
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(p.isDead() || !p.isOnline())return;
                    p.teleport(p.getLocation().set(x[0],p.getWorld().getHighestBlockYAt(x[0], z[0])+1, z[0]));
                    if(z[0] >=eiss.getInt("orgen"+".MaxZ")){
                        z[0] =eiss.getInt("orgen" + ".MaxZ") * (-1);
                        x[0] +=140;
                        if(x[0]>=eiss.getInt("orgen" + ".MaxX"))cancel();
                    } else {
                        z[0] +=140;
                    }
                }
            }.runTaskTimer(Monsterplugin.getPlugin(),0,20*3);

        } catch (NullPointerException e){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED +"Falsch Spieler");
        }
        return false;
    }
}
