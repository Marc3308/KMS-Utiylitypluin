package me.marc3308.monsterplugin.commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static org.bukkit.Bukkit.getServer;

public class playerlog implements CommandExecutor{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))return false;
        Player p= (Player) sender;
        if(!p.isOp())return false;
        openinv(p,args.length>=1 ? args[0] : null,1,"LastOfLog");
        return false;
    }

    public static void openinv(Player p, String one, int Seite, String Sorter){
        // Include data from stats files
        ArrayList<OfflinePlayer> alltheplayers= new ArrayList<>();
        Stream.of(Bukkit.getOfflinePlayers()).forEach(alltheplayers::add);

        //fast info
        if (one!=null){
            for (OfflinePlayer of : alltheplayers){
                if(of.getName().equals(one)){
                    int hours = (of.getStatistic(Statistic.PLAY_ONE_MINUTE)/20/60)/60;
                    int minutes = (of.getStatistic(Statistic.PLAY_ONE_MINUTE)/20/60)-(hours*60);
                    int seconds = of.getStatistic(Statistic.PLAY_ONE_MINUTE)/20-((hours*60*60)+(minutes*60));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
                    p.sendMessage(ChatColor.DARK_GREEN+"RpName: "+of.getPersistentDataContainer().get(new NamespacedKey("klassensysteem", "secretname"), PersistentDataType.STRING));
                    p.sendMessage(ChatColor.DARK_GREEN+"Spezies: "+of.getPersistentDataContainer().get(new NamespacedKey("rassensystem", "rasse"), PersistentDataType.STRING));
                    p.sendMessage(ChatColor.DARK_GREEN+"TimePlayed: "+ChatColor.GREEN+hours+"h "+minutes+"m "+seconds+"s");
                    p.sendMessage(ChatColor.DARK_GREEN+"FirstPlayed: "+ChatColor.GREEN+dateFormat.format(new Date(of.getFirstPlayed())));
                    p.sendMessage(ChatColor.DARK_GREEN+"LastLogin: "+ChatColor.GREEN+dateFormat.format(new Date(of.getLastLogin())));
                    p.sendMessage(ChatColor.DARK_GREEN+"LastLogOut: "+ChatColor.GREEN+dateFormat.format(new Date(of.getLastSeen())));
                    p.sendMessage(ChatColor.DARK_GREEN+"Whitlistet: "+(of.isWhitelisted() ? ChatColor.RED+"Nein" : ChatColor.GREEN+"Ja"));
                    return;
                }
            }
        }


        //create the inventory
        Inventory Loginventar= Bukkit.createInventory(p,54,"§lSpielerLog");

        //creat the allways components
        ItemStack suche=new ItemStack(Material.ANVIL);
        ItemMeta suche_meta=suche.getItemMeta();
        suche_meta.setDisplayName(Sorter);
        ArrayList<String> suche_lore=new ArrayList<>();
        suche_lore.add("Klicke hier um die Sotierung zu ändern");
        suche_meta.setLore(suche_lore);
        suche.setItemMeta(suche_meta);

        ItemStack vorpfeil=new ItemStack(Material.ARROW);
        ItemMeta vorpfeil_meta=vorpfeil.getItemMeta();
        vorpfeil_meta.setDisplayName(""+Seite);
        vorpfeil.setItemMeta(vorpfeil_meta);

        ItemStack buch=new ItemStack(Material.BOOK);
        ItemMeta buch_meta= buch.getItemMeta();
        buch_meta.setDisplayName(ChatColor.GRAY+"§lSeite: 1");
        buch.setItemMeta(buch_meta);

        Loginventar.setItem(51,vorpfeil);
        Loginventar.setItem(49,buch);
        Loginventar.setItem(47,suche);

        //sort it
        switch (Sorter){
            case "LastOfLog":
                Collections.sort(alltheplayers, (a1,a2) -> Long.compare(a2.getLastSeen(),a1.getLastSeen()));
                break;
            case "FirstOnLock":
                Collections.sort(alltheplayers, (a1,a2) -> Long.compare(a1.getFirstPlayed(),a2.getFirstPlayed()));
                break;
            case "MostPlayTime":
                Collections.sort(alltheplayers, (a1,a2) -> Long.compare(a2.getStatistic(Statistic.PLAY_ONE_MINUTE),a1.getStatistic(Statistic.PLAY_ONE_MINUTE)));
                break;
            case "PlayerName":
                alltheplayers.sort(Comparator.comparing(OfflinePlayer::getName, String.CASE_INSENSITIVE_ORDER));
                break;
            case "RpName":
                Collections.sort(alltheplayers, (a1,a2) -> CharSequence.compare(a2.getPersistentDataContainer().get(new NamespacedKey("klassensysteem", "secretname"), PersistentDataType.STRING)
                        ,a1.getPersistentDataContainer().get(new NamespacedKey("klassensysteem", "secretname"), PersistentDataType.STRING)));
                break;
            case "Spezien":
                Collections.sort(alltheplayers, (a1,a2) -> CharSequence.compare(a2.getPersistentDataContainer().get(new NamespacedKey("rassensystem", "rasse"), PersistentDataType.STRING)
                        ,a1.getPersistentDataContainer().get(new NamespacedKey("rassensystem", "rasse"), PersistentDataType.STRING)));
                break;
        }

        for (OfflinePlayer sp : alltheplayers){
            if(alltheplayers.indexOf(sp)>=44*(Seite-1) && alltheplayers.indexOf(sp)<=44*Seite){
                // Extract playtime in ticks
                int hours = (sp.getStatistic(Statistic.PLAY_ONE_MINUTE)/20/60)/60;
                int minutes = (sp.getStatistic(Statistic.PLAY_ONE_MINUTE)/20/60)-(hours*60);
                int seconds = sp.getStatistic(Statistic.PLAY_ONE_MINUTE)/20-((hours*60*60)+(minutes*60));

                //time
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");


                //Create head
                ItemStack head=new ItemStack(Material.PLAYER_HEAD,1,(short) 3);
                SkullMeta skull=(SkullMeta) head.getItemMeta();

                ArrayList<String> skull_lore=new ArrayList<>();
                skull_lore.add("--------Rp Infos--------");
                skull_lore.add("Rp Name: "+sp.getPersistentDataContainer().get(new NamespacedKey("klassensysteem", "secretname"), PersistentDataType.STRING));
                skull_lore.add("Spezies: "+sp.getPersistentDataContainer().get(new NamespacedKey("rassensystem", "rasse"), PersistentDataType.STRING));
                skull_lore.add("Whitlistet: "+sp.isWhitelisted());
                skull_lore.add("--------Timestamp--------");
                skull_lore.add("TimePlayed: "+hours+"h "+minutes+"m "+seconds+"s");
                skull_lore.add("FirstPlayed: "+dateFormat.format(new Date(sp.getFirstPlayed())));
                skull_lore.add("LastLogin: "+dateFormat.format(new Date(sp.getLastLogin())));
                skull_lore.add("LastLogOut: "+dateFormat.format(new Date(sp.getLastSeen())));
                skull_lore.add("");
                skull_lore.add(ChatColor.YELLOW+"Linksklick für mehr Informationen");
                skull.setDisplayName(sp.getName());
                if(Bukkit.getPlayer(sp.getName())==null){
                    String base64 = sp.isWhitelisted() ? "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWU3NzAwMDk2YjVhMmE4NzM4NmQ2MjA1YjRkZGNjMTRmZDMzY2YyNjkzNjJmYTY4OTM0OTk0MzFjZTc3YmY5In19fQ=="
                            : "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==";

                    // Create a PlayerProfile with a random UUID and apply the base64 texture
                    PlayerProfile profile = getServer().createProfile(UUID.randomUUID(), "CustomHead");
                    profile.getProperties().add(new ProfileProperty("textures", base64));

                    // Set the profile to the skull meta
                    skull.setPlayerProfile(profile);
                } else {
                    skull.setOwner(sp.getName());
                }
                skull.setLore(skull_lore);
                head.setItemMeta(skull);
                Loginventar.setItem(Loginventar.firstEmpty(),head);
            }
        }

        p.openInventory(Loginventar);
    }
}