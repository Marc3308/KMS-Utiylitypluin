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
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

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
        File statsFolder = new File(getServer().getWorldContainer(), "world/stats");
        ArrayList<SpielerStempel> alltheplayers=new ArrayList<>();

        if (statsFolder.exists() && statsFolder.isDirectory()) {
            for (File statsFile : statsFolder.listFiles()) {
                try {

                    //get Offline Player
                    String uuidString = statsFile.getName().replace(".json", "");
                    OfflinePlayer pp= Bukkit.getOfflinePlayer(UUID.fromString(uuidString));



                    //add all players
                    alltheplayers.add(new SpielerStempel(pp.getName()!=null ? pp.getName() : "Error"
                            ,pp.getStatistic(Statistic.PLAY_ONE_MINUTE)/20
                            ,pp.getFirstPlayed(),pp.getLastLogin(),pp.getLastSeen()
                            ,pp.isWhitelisted()));
                } catch (Exception e) {
                    System.out.println("Failed to process stats file: " + statsFile.getName());
                }
            }
        }

        //fast info
        if (one!=null){
            for (SpielerStempel sp : alltheplayers){
                if(sp.getName().equals(one)){
                    // Extract playtime in ticks
                    int allthesecends = sp.getAllthesecends();
                    int hours = (allthesecends/60)/60;
                    int minutes = (allthesecends/60)-(hours*60);
                    int seconds = allthesecends-((hours*60*60)+(minutes*60));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
                    p.sendMessage(ChatColor.DARK_GREEN+"TimePlayed: "+ChatColor.GREEN+hours+"h "+minutes+"m "+seconds+"s");
                    p.sendMessage(ChatColor.DARK_GREEN+"FirstPlayed: "+ChatColor.GREEN+dateFormat.format(new Date(sp.getFirstonlock())));
                    p.sendMessage(ChatColor.DARK_GREEN+"LastLogin: "+ChatColor.GREEN+dateFormat.format(new Date(sp.getLastonlock())));
                    p.sendMessage(ChatColor.DARK_GREEN+"LastLogOut: "+ChatColor.GREEN+dateFormat.format(new Date(sp.getLastoflock())));
                    p.sendMessage(ChatColor.DARK_GREEN+"Whitlistet: "+(sp.isWhitlistet() ? ChatColor.RED+"Nein" : ChatColor.GREEN+"Ja"));
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
                Collections.sort(alltheplayers, (a1,a2) -> Long.compare(a2.getLastoflock(),a1.getLastoflock()));
                break;
            case "FirstOnLock":
                Collections.sort(alltheplayers, (a1,a2) -> Long.compare(a1.getFirstonlock(),a2.getFirstonlock()));
                break;
            case "MostPlayTime":
                Collections.sort(alltheplayers, (a1,a2) -> Long.compare(a2.getAllthesecends(),a1.getAllthesecends()));
                break;
            case "PlayerName":
                alltheplayers.sort(Comparator.comparing(SpielerStempel::getName, String.CASE_INSENSITIVE_ORDER));
                break;
        }

        for (SpielerStempel sp : alltheplayers){
            if(alltheplayers.indexOf(sp)>=44*(Seite-1) && alltheplayers.indexOf(sp)<=44*Seite){
                // Extract playtime in ticks
                int allthesecends = sp.getAllthesecends();
                int hours = (allthesecends/60)/60;
                int minutes = (allthesecends/60)-(hours*60);
                int seconds = allthesecends-((hours*60*60)+(minutes*60));

                //time
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");


                //Create head
                ItemStack head=new ItemStack(Material.PLAYER_HEAD,1,(short) 3);
                SkullMeta skull=(SkullMeta) head.getItemMeta();

                ArrayList<String> skull_lore=new ArrayList<>();
                skull_lore.add("TimePlayed: "+hours+"h "+minutes+"m "+seconds+"s");
                skull_lore.add("FirstPlayed: "+dateFormat.format(new Date(sp.getFirstonlock())));
                skull_lore.add("LastLogin: "+dateFormat.format(new Date(sp.getLastonlock())));
                skull_lore.add("LastLogOut: "+dateFormat.format(new Date(sp.getLastoflock())));
                skull_lore.add("Whitlistet: "+sp.isWhitlistet());
                skull.setDisplayName(sp.getName());
                if(Bukkit.getPlayer(sp.getName())==null){
                    String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWU3NzAwMDk2YjVhMmE4NzM4NmQ2MjA1YjRkZGNjMTRmZDMzY2YyNjkzNjJmYTY4OTM0OTk0MzFjZTc3YmY5In19fQ==";

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

    public static void opneplayerinf(Player p,UUID uuid){
        // Locate the player's .dat file
        File playerFile = new File(getServer().getWorldContainer(), "world/playerdata/" + uuid + ".dat");
        if (!playerFile.exists()) {
            p.sendMessage(ChatColor.RED+"Die datei wurde nicht gefunden");
            p.closeInventory();
            return;
        }

        playerFile






    }

}

class SpielerStempel {

    private String Name;
    private int Allthesecends;
    private long Firstonlock;
    private long Lastonlock;
    private long Lastoflock;
    private boolean Whitlistet;


    public SpielerStempel(String name, int allthesecends, long firstonlock, long lastonlock, long lastoflock, boolean whitlistet){
        this.Name=name;
        this.Allthesecends=allthesecends;
        this.Firstonlock=firstonlock;
        this.Lastonlock=lastonlock;
        this.Lastoflock=lastoflock;
        this.Whitlistet=whitlistet;
    }

    public String getName() {
        return Name;
    }

    public boolean isWhitlistet() {
        return Whitlistet;
    }

    public int getAllthesecends() {
        return Allthesecends;
    }

    public long getFirstonlock() {
        return Firstonlock;
    }

    public long getLastonlock() {
        return Lastonlock;
    }

    public long getLastoflock() {
        return Lastoflock;
    }

}