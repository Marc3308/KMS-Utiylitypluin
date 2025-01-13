package me.marc3308.monsterplugin.eventlistener;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import static me.marc3308.monsterplugin.Monsterplugin.plugin;

public class joinleavetracker implements Listener {

    @EventHandler
    public void onjoin(PlayerJoinEvent e){

        Player p = e.getPlayer();
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter clockdate = DateTimeFormatter.ofPattern("HH:mm:ss");
        if(!p.getPersistentDataContainer().has(new NamespacedKey(plugin
                        ,today.format(dateFormat)+"logintime")
                , PersistentDataType.STRING))p.getPersistentDataContainer().set(new NamespacedKey(plugin
                        ,today.format(dateFormat)+"logintime")
                , PersistentDataType.STRING,today.atTime(LocalTime.now()).format(clockdate));
    }

    @EventHandler
    public void logout(PlayerQuitEvent e){
        Player p = e.getPlayer();
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter clockdate = DateTimeFormatter.ofPattern("HH:mm:ss");
        p.getPersistentDataContainer().set(new NamespacedKey(plugin
                        ,today.format(dateFormat)+"auslogtime")
                , PersistentDataType.STRING,today.atTime(LocalTime.now()).format(clockdate));
        p.getPersistentDataContainer().set(new NamespacedKey(plugin
                        ,today.format(dateFormat)+"auslog")
                , PersistentDataType.STRING,"X:"+p.getLocation().getBlockX()+" Y:"+p.getLocation().getBlockY()+" Z:"+p.getLocation().getBlockZ());
        int playtime = p.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin
                        ,today.format(dateFormat)+"playtime")
                ,PersistentDataType.INTEGER,0);
        playtime+=((p.getLastSeen()-p.getLastLogin())/20/50);
        p.getPersistentDataContainer().set(new NamespacedKey(plugin
                        ,today.format(dateFormat)+"playtime")
                ,PersistentDataType.INTEGER,playtime);
    }
}
