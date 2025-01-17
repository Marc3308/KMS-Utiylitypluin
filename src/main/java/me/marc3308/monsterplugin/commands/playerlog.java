package me.marc3308.monsterplugin.commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Stream;

import static me.marc3308.monsterplugin.Monsterplugin.plugin;
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
                    p.sendMessage(ChatColor.GREEN+"--------Rp Infos--------");
                    p.sendMessage(ChatColor.DARK_GREEN+"RpName: "+of.getPersistentDataContainer().get(new NamespacedKey("klassensysteem", "secretname"), PersistentDataType.STRING));
                    p.sendMessage(ChatColor.DARK_GREEN+"Spezies: "+of.getPersistentDataContainer().get(new NamespacedKey("rassensystem", "rasse"), PersistentDataType.STRING)+"%");
                    p.sendMessage(ChatColor.DARK_GREEN+"Seelenenergie: "+of.getPersistentDataContainer().get(new NamespacedKey("klassensysteem", "seelenenergie"), PersistentDataType.STRING));
                    p.sendMessage(ChatColor.DARK_GREEN+"Whitlistet: "+(of.isWhitelisted() ? ChatColor.RED+"Nein" : ChatColor.GREEN+"Ja"));
                    p.sendMessage(ChatColor.GREEN+"--------Timestamp--------");
                    p.sendMessage(ChatColor.DARK_GREEN+"TimePlayed: "+ChatColor.GREEN+hours+"h "+minutes+"m "+seconds+"s");
                    p.sendMessage(ChatColor.DARK_GREEN+"FirstPlayed: "+ChatColor.GREEN+dateFormat.format(new Date(of.getFirstPlayed())));
                    p.sendMessage(ChatColor.DARK_GREEN+"LastLogin: "+ChatColor.GREEN+dateFormat.format(new Date(of.getLastLogin())));
                    p.sendMessage(ChatColor.DARK_GREEN+"LastLogOut: "+ChatColor.GREEN+dateFormat.format(new Date(of.getLastSeen())));
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

        ItemStack bestenbuch=new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta bestenbuch_meta= bestenbuch.getItemMeta();
        bestenbuch_meta.setRarity(ItemRarity.EPIC);
        bestenbuch_meta.setLore(new ArrayList<>(){{
            add(ChatColor.DARK_RED+"WARNUNG"+ChatColor.RED+" Performance Fressend");
        }});
        bestenbuch_meta.setDisplayName(ChatColor.GRAY+"§BBestenListe");
        bestenbuch.setItemMeta(bestenbuch_meta);

        ItemStack server=new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta server_meta= server.getItemMeta();
        server_meta.setDisplayName(ChatColor.YELLOW+"§lServer");
        server_meta.setLore(new ArrayList<>(){{
            add(ChatColor.MAGIC+"Server"+ChatColor.RESET+"§lInformationen"+ChatColor.MAGIC+"Server");
            int finalGesamtspielzeit = alltheplayers.stream().mapToInt(p -> p.getStatistic(Statistic.PLAY_ONE_MINUTE)/20).sum();
            int Stunden = finalGesamtspielzeit/60/60;
            int minutes = (finalGesamtspielzeit/60)-(Stunden*60);
            int seconds = finalGesamtspielzeit-((Stunden*60*60)+(minutes*60));
            add("Gesamtspielzeit: "+Stunden+":"+minutes+":"+seconds);
            add("");
            add(ChatColor.YELLOW+"Linksklick für mehr Informationen");
        }});
        server.setItemMeta(server_meta);

        Loginventar.setItem(53,server);
        Loginventar.setItem(51,vorpfeil);
        Loginventar.setItem(49,buch);
        Loginventar.setItem(47,suche);
        Loginventar.setItem(45,bestenbuch);
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
                alltheplayers.sort(Comparator.comparing(
                        of -> of.getPersistentDataContainer().get(new NamespacedKey("klassensysteem", "secretname"), PersistentDataType.STRING),
                        Comparator.nullsLast(String::compareTo)
                ));
                break;
            case "Spezien":
                alltheplayers.sort(Comparator.comparing(
                        of -> of.getPersistentDataContainer().get(new NamespacedKey("rassensystem", "rasse"), PersistentDataType.STRING),
                        Comparator.nullsLast(String::compareTo)
                ));
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
                skull_lore.add("Seelenenergie: "+sp.getPersistentDataContainer().get(new NamespacedKey("klassensysteem", "seelenenergie"), PersistentDataType.INTEGER)+"%");
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

    public static void openleaderinv(Player p, int Seite, int Sorter){

        //create the inventory
        Inventory Loginventar= Bukkit.createInventory(p,54,"§lSpieler Bestenliste");


        //creat the allways components
        ItemStack suche=new ItemStack(Material.ANVIL);
        ItemMeta suche_meta=suche.getItemMeta();
        suche_meta.setDisplayName(Statistic.values()[Sorter].toString());
        ArrayList<String> suche_lore=new ArrayList<>();
        suche_lore.add(String.valueOf(Sorter));
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

        ItemStack zurück=new ItemStack(Material.BARRIER);
        ItemMeta zurück_meta= zurück.getItemMeta();
        zurück_meta.setDisplayName(ChatColor.RED+"§lZurück");
        zurück.setItemMeta(zurück_meta);

        Loginventar.setItem(51,vorpfeil);
        Loginventar.setItem(49,buch);
        Loginventar.setItem(47,suche);
        Loginventar.setItem(45,zurück);

        // Include data from stats files
        ArrayList<OfflinePlayer> alltheplayers= new ArrayList<>();
        Stream.of(Bukkit.getOfflinePlayers()).forEach(alltheplayers::add);

        //sorter
        try {
            Collections.sort(alltheplayers, (a1,a2) -> Long.compare(a2.getStatistic(Statistic.values()[Sorter]),a1.getStatistic(Statistic.values()[Sorter])));
        } catch (IllegalArgumentException e){
            //skipp illegel arguments
            openleaderinv(p
                    ,Seite
                    ,Statistic.values().length-1==Integer.valueOf(suche.getItemMeta().getLore().getFirst())
                            ? 1 : Integer.valueOf(suche.getItemMeta().getLore().getFirst())+1);
            return;
        }

        //köpfe
        for (OfflinePlayer sp : alltheplayers){
            if(alltheplayers.indexOf(sp)>=44*(Seite-1) && alltheplayers.indexOf(sp)<=44*Seite){

                //Create head
                ItemStack head=new ItemStack(Material.PLAYER_HEAD,1,(short) 3);
                SkullMeta skull=(SkullMeta) head.getItemMeta();

                ArrayList<String> skull_lore=new ArrayList<>();
                skull_lore.add("--------Wert--------");
                skull_lore.add(Statistic.values()[Sorter].toString()+": "+sp.getStatistic(Statistic.values()[Sorter]));
                skull_lore.add("--------Rang--------");
                skull_lore.add((alltheplayers.indexOf(sp)+1)+"/"+alltheplayers.size());
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

    public static void openplayerinv(Player p, OfflinePlayer of, int Woche){

        // Get the current date
        LocalDate today = LocalDate.now(ZoneId.of("Europe/Berlin")).plusWeeks(Woche);

        // Find the Monday of the current week
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        //create the inventory
        Inventory Loginventar= Bukkit.createInventory(p,54,"§lSpieler Tracker");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");


        ItemStack vorpfeil=new ItemStack(Material.ARROW);
        ItemMeta vorpfeil_meta=vorpfeil.getItemMeta();
        vorpfeil_meta.setDisplayName(sunday.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).format(dateFormat)+" - "+sunday.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).format(dateFormat));
        vorpfeil_meta.setLore(new ArrayList<>(){{
            add(String.valueOf(Woche+1));
        }});
        vorpfeil.setItemMeta(vorpfeil_meta);

        ItemStack zurückpfeil=new ItemStack(Material.ARROW);
        ItemMeta zurückpfeil_meta=zurückpfeil.getItemMeta();
        zurückpfeil_meta.setDisplayName(monday.with(TemporalAdjusters.previous(DayOfWeek.MONDAY)).format(dateFormat)+" - "+monday.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).format(dateFormat));
        zurückpfeil_meta.setLore(new ArrayList<>(){{
            add(String.valueOf(Woche-1));
        }});
        zurückpfeil.setItemMeta(zurückpfeil_meta);

        ItemStack buch=new ItemStack(Material.BOOK);
        ItemMeta buch_meta= buch.getItemMeta();
        buch_meta.setDisplayName(monday.format(dateFormat)+" - "+sunday.format(dateFormat));
        buch_meta.setLore(new ArrayList<>(){{
            add("");
            add(ChatColor.YELLOW+"Linkscklick für "+LocalDate.now().format(dateFormat));
        }});
        buch.setItemMeta(buch_meta);

        ItemStack zurück=new ItemStack(Material.BARRIER);
        ItemMeta zurück_meta= zurück.getItemMeta();
        zurück_meta.setDisplayName(ChatColor.RED+"§lZurück");
        zurück.setItemMeta(zurück_meta);

        //spieler
        ItemStack head=new ItemStack(Material.PLAYER_HEAD,1,(short) 3);
        SkullMeta skull=(SkullMeta) head.getItemMeta();
        skull.setDisplayName(of.getName());
        skull.setLore(new ArrayList<>(){{
            add("");
            add(ChatColor.YELLOW+"Linksklick für mehr Informationen");
        }});
        if(Bukkit.getPlayer(of.getName())==null){
            String base64 = of.isWhitelisted() ? "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWU3NzAwMDk2YjVhMmE4NzM4NmQ2MjA1YjRkZGNjMTRmZDMzY2YyNjkzNjJmYTY4OTM0OTk0MzFjZTc3YmY5In19fQ=="
                    : "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==";

            // Create a PlayerProfile with a random UUID and apply the base64 texture
            PlayerProfile profile = getServer().createProfile(UUID.randomUUID(), "CustomHead");
            profile.getProperties().add(new ProfileProperty("textures", base64));

            // Set the profile to the skull meta
            skull.setPlayerProfile(profile);
        } else {
            skull.setOwner(of.getName());
        }
        head.setItemMeta(skull);

        //glass
        for (int i = 0; i < 7; i++) {

            ItemStack glass=new ItemStack(Material.GLASS);
            ItemMeta glass_meta= glass.getItemMeta();
            glass_meta.setDisplayName(monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[i])).format(dateFormat));
            if(of.getPersistentDataContainer().has(new NamespacedKey(plugin
                    ,monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[i])).format(dateFormat)+"auslog")
                    ,PersistentDataType.STRING)) {
                int finalI = i;
                glass_meta.setLore(new ArrayList<>(){{
                    add("Ausgelogt: "+of.getPersistentDataContainer().get(new NamespacedKey(plugin
                                    ,monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[finalI])).format(dateFormat)+"auslog")
                                    ,PersistentDataType.STRING));
                    add("");
                    add(ChatColor.YELLOW+"Linksklick zum Teleportieren");
        }});
            }
            glass.setItemMeta(glass_meta);

            Loginventar.setItem(37+i,glass);
        }

        //onzeit
        for (int i = 0; i < 7; i++) {

            if(of.getPersistentDataContainer().has(new NamespacedKey(plugin
                            ,monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[i])).format(dateFormat)+"playtime")
                    ,PersistentDataType.INTEGER)){

                ItemStack gelwolle = new ItemStack(Material.YELLOW_WOOL);
                ItemMeta gelwolle_meta = gelwolle.getItemMeta();
                gelwolle_meta.setDisplayName(ChatColor.YELLOW+monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[i])).format(dateFormat));
                int finalI = i;
                gelwolle_meta.setLore(new ArrayList<>(){{
                    add(ChatColor.YELLOW+"Ausgelogt: "+of.getPersistentDataContainer().get(new NamespacedKey(plugin
                                    ,monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[finalI])).format(dateFormat)+"auslogtime")
                            ,PersistentDataType.STRING));
                    int allthesec=of.getPersistentDataContainer().get(new NamespacedKey(plugin
                                    ,monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[finalI])).format(dateFormat)+"playtime")
                            ,PersistentDataType.INTEGER);

                    //wenn spieler noch on
                    if(of.isOnline())allthesec+=((of.getLastSeen()-of.getLastLogin())/20/50);

                    int Stunden = allthesec/60/60;
                    int minutes = (allthesec/60)-(Stunden*60);
                    int seconds = allthesec-((Stunden*60*60)+(minutes*60));
                    add(ChatColor.YELLOW+"Gesamtspielzeit: "+Stunden+":"+minutes+":"+seconds);
                    add(ChatColor.YELLOW+"Login: "+of.getPersistentDataContainer().get(new NamespacedKey(plugin
                                    ,monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[finalI])).format(dateFormat)+"logintime")
                            ,PersistentDataType.STRING));
                }});

                gelwolle.setItemMeta(gelwolle_meta);

                for (int j = 0; (j*3) <= (of.getPersistentDataContainer().get(new NamespacedKey(plugin
                                ,monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[finalI])).format(dateFormat)+"playtime")
                        ,PersistentDataType.INTEGER)/60/60); j++) {
                    if(28+i-(9*j)<0)break; //savty first
                    Loginventar.setItem(28+i-(9*j),gelwolle);
                }

            } else {

                boolean isafter = monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[i])).isAfter(new Date(of.getFirstPlayed()).toInstant()
                        .atZone(ZoneId.of("Europe/Berlin")) // Specify German time zone
                        .toLocalDate());
                LocalDate timedate =monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[i]));
                ItemStack wegwolle = new ItemStack(isafter ? timedate.isAfter(LocalDate.now()) ? Material.WHITE_WOOL : Material.RED_WOOL : Material.GRAY_WOOL);
                ItemMeta wegwolle_meta = wegwolle.getItemMeta();
                wegwolle_meta.setDisplayName(isafter ? timedate.isAfter(LocalDate.now()) ? String.valueOf(monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[i])))
                        : ChatColor.RED+ String.valueOf(monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[i])))
                        : ChatColor.GRAY+String.valueOf(monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[i]))) );
                wegwolle_meta.setLore(new ArrayList<>(){{
                    add(isafter ? timedate.isAfter(LocalDate.now()) ? ChatColor.WHITE+"Zukunft" : ChatColor.RED+"Spieler Offline" : ChatColor.GRAY+"Noch nicht auf dem Server");
                }});
                wegwolle.setItemMeta(wegwolle_meta);
                Loginventar.setItem(28+i,wegwolle);
            }
        }

        Loginventar.setItem(53,head);
        Loginventar.setItem(51,vorpfeil);
        Loginventar.setItem(49,buch);
        Loginventar.setItem(47,zurückpfeil);
        Loginventar.setItem(45,zurück);

        p.openInventory(Loginventar);
    }

    public static void openserverinv(Player p, int Woche){

        // Include data from stats files
        ArrayList<OfflinePlayer> alltheplayers= new ArrayList<>();
        Stream.of(Bukkit.getOfflinePlayers()).forEach(alltheplayers::add);

        // Get the current date
        LocalDate today = LocalDate.now(ZoneId.of("Europe/Berlin")).plusWeeks(Woche);

        // Find the Monday of the current week
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        //create the inventory
        Inventory Loginventar= Bukkit.createInventory(p,54,"§lServer Tracker");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");


        ItemStack vorpfeil=new ItemStack(Material.ARROW);
        ItemMeta vorpfeil_meta=vorpfeil.getItemMeta();
        vorpfeil_meta.setDisplayName(sunday.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).format(dateFormat)+" - "+sunday.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).format(dateFormat));
        vorpfeil_meta.setLore(new ArrayList<>(){{
            add(String.valueOf(Woche+1));
        }});
        vorpfeil.setItemMeta(vorpfeil_meta);

        ItemStack zurückpfeil=new ItemStack(Material.ARROW);
        ItemMeta zurückpfeil_meta=zurückpfeil.getItemMeta();
        zurückpfeil_meta.setDisplayName(monday.with(TemporalAdjusters.previous(DayOfWeek.MONDAY)).format(dateFormat)+" - "+monday.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).format(dateFormat));
        zurückpfeil_meta.setLore(new ArrayList<>(){{
            add(String.valueOf(Woche-1));
        }});
        zurückpfeil.setItemMeta(zurückpfeil_meta);

        ItemStack buch=new ItemStack(Material.BOOK);
        ItemMeta buch_meta= buch.getItemMeta();
        buch_meta.setDisplayName(monday.format(dateFormat)+" - "+sunday.format(dateFormat));
        buch_meta.setLore(new ArrayList<>(){{
            add("");
            add(ChatColor.YELLOW+"Linkscklick für "+LocalDate.now().format(dateFormat));
        }});
        buch.setItemMeta(buch_meta);

        ItemStack zurück=new ItemStack(Material.BARRIER);
        ItemMeta zurück_meta= zurück.getItemMeta();
        zurück_meta.setDisplayName(ChatColor.RED+"§lZurück");
        zurück.setItemMeta(zurück_meta);

        //glass
        for (int i = 0; i < 7; i++) {

            ItemStack glass=new ItemStack(Material.GLASS);
            ItemMeta glass_meta= glass.getItemMeta();
            glass_meta.setDisplayName(monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[i])).format(dateFormat));
            glass.setItemMeta(glass_meta);

            Loginventar.setItem(37+i,glass);
        }

        //onzeit
        for (int i = 0; i < 7; i++) {

            int spieler=0;
            int gesamtspielzeit=0;

            for (OfflinePlayer of : alltheplayers){
                if(of.getPersistentDataContainer().has(new NamespacedKey(plugin
                                ,monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[i])).format(dateFormat)+"playtime")
                        ,PersistentDataType.INTEGER)){
                    spieler++;
                    gesamtspielzeit+=of.getPersistentDataContainer().get(new NamespacedKey(plugin
                                    ,monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[i])).format(dateFormat)+"playtime")
                            ,PersistentDataType.INTEGER);
                }
            }

            if(spieler!=0){

            ItemStack gelwolle = new ItemStack(Material.YELLOW_WOOL);
            ItemMeta gelwolle_meta = gelwolle.getItemMeta();
            gelwolle_meta.setDisplayName(ChatColor.YELLOW+monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[i])).format(dateFormat));
                int finalSpieler = spieler;
                int finalGesamtspielzeit = gesamtspielzeit;
                gelwolle_meta.setLore(new ArrayList<>(){{
                add(ChatColor.YELLOW+"MaxSpieler: "+ finalSpieler);
                    int Stunden = finalGesamtspielzeit/60/60;
                    int minutes = (finalGesamtspielzeit/60)-(Stunden*60);
                    int seconds = finalGesamtspielzeit-((Stunden*60*60)+(minutes*60));
                add(ChatColor.YELLOW+"Gesamtspielzeit: "+Stunden+":"+minutes+":"+seconds);
            }});

            gelwolle.setItemMeta(gelwolle_meta);

            for (int j = 0; (j*3) <= (gesamtspielzeit/60/60); j++) {
                if(28+i-(9*j)<0)break; //savty first
                Loginventar.setItem(28+i-(9*j),gelwolle);
            }

            } else {

                LocalDate timedate =monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[i]));
                ItemStack wegwolle = new ItemStack(timedate.isAfter(LocalDate.now()) ? Material.WHITE_WOOL : Material.GRAY_WOOL);
                ItemMeta wegwolle_meta = wegwolle.getItemMeta();
                wegwolle_meta.setDisplayName(timedate.isAfter(LocalDate.now()) ? String.valueOf(monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[i])))
                        : ChatColor.GRAY+ String.valueOf(monday.with(TemporalAdjusters.nextOrSame(DayOfWeek.values()[i]))));
                wegwolle_meta.setLore(new ArrayList<>(){{
                    add(timedate.isAfter(LocalDate.now()) ? ChatColor.WHITE+"Zukunft" : ChatColor.GRAY+"Keine Spieler Online");
                }});
                wegwolle.setItemMeta(wegwolle_meta);
                Loginventar.setItem(28+i,wegwolle);
            }
        }

        Loginventar.setItem(51,vorpfeil);
        Loginventar.setItem(49,buch);
        Loginventar.setItem(47,zurückpfeil);
        Loginventar.setItem(45,zurück);

        p.openInventory(Loginventar);
    }
}