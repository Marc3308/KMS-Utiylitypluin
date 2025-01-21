package me.marc3308.monsterplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static me.marc3308.monsterplugin.Monsterplugin.cycleStartTime;

public class settime implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))return false;
        Player p= (Player) sender;
        if(!sender.isOp())return false;
        try {
            Date newtime=new Date();

            newtime.setYear(cycleStartTime.getYear()-1900);
            newtime.setMonth(cycleStartTime.getMonthValue()-1);
            newtime.setDate(cycleStartTime.getDayOfMonth());
            newtime.setHours(Integer.valueOf(args[0]));
            newtime.setMinutes(Integer.valueOf(args[1]));
            newtime.setSeconds(cycleStartTime.getSecond());

            cycleStartTime= LocalDateTime.from(newtime.toInstant().atZone(ZoneId.systemDefault()));

            p.sendMessage(ChatColor.DARK_GREEN+"Die Neue Startzeit ist nun: "+ChatColor.GREEN+args[0]+":"+args[1]);
        } catch (NumberFormatException e){
            p.sendMessage(ChatColor.RED+"Versucge /worldtimeset <stunden> <minuten>");
        } catch (ArrayIndexOutOfBoundsException e){
            p.sendMessage(ChatColor.RED+"Versucge /worldtimeset <stunden> <minuten>");
        } catch (NullPointerException e){
            p.sendMessage(ChatColor.RED+"Versucge /worldtimeset <stunden> <minuten>");
        }
        return false;
    }
}
