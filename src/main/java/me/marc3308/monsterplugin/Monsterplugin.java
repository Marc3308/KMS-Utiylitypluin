package me.marc3308.monsterplugin;

import me.marc3308.monsterplugin.commands.*;
import me.marc3308.monsterplugin.eventlistener.*;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public final class Monsterplugin extends JavaPlugin implements Listener {

    public static File file = new File("plugins/KMS Plugins/DingeDiePassierenMussen","Eistellungen.yml");
    public static FileConfiguration eiss= YamlConfiguration.loadConfiguration(file);

    public static File file2 = new File("plugins/KMS Plugins/DingeDiePassierenMussen","NichtFertig.yml");
    public static FileConfiguration safething3= YamlConfiguration.loadConfiguration(file2);
    public static ArrayList<Location> chunklust=new ArrayList<>();
    public static int Bignumb=0;

    public static LocalDateTime cycleStartTime =LocalDateTime.now();
    public static Monsterplugin plugin;

    @Override
    public void onEnable() {

        plugin = this;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                    @Override
                    public void run() {
                        if(chunklust.size()>=1 && eiss.getBoolean("orgen"+".Starteating")){
                            oregen.deletus(chunklust.get(0));
                            chunklust.remove(0);
                        }

                        if(eiss.get("dodaynighttime")!=null && eiss.getBoolean("dodaynighttime")){
                            //tagnacht
                            int wishtime=eiss.getInt("daynightinminuten")*60;
                            double ticksPerSecond = 24000L / (double) wishtime;

                            // Get the current time in the real world
                            LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Berlin"));

                            // Calculate the time passed since the cycle started in seconds
                            long timePassedInSeconds = Duration.between(cycleStartTime, now).getSeconds();

                            // Calculate how far into the 3-hour cycle we are (as a percentage)
                            double cycleProgress = (double) timePassedInSeconds / wishtime;

                            // Calculate the new Minecraft time based on cycle progress
                            long newMinecraftTime = (long) (cycleProgress * 24000L);

                            //warnungen
                            if(now.getHour()==0 && now.getMinute()==0 && now.getSecond()<2)for (Player p : Bukkit.getOnlinePlayers())p.sendTitle(ChatColor.GREEN+"Warnung",ChatColor.GREEN+"Der Server schließt in einer Stunde");
                            if(now.getHour()==0 && now.getMinute()==30 && now.getSecond()<2)for (Player p : Bukkit.getOnlinePlayers())p.sendTitle(ChatColor.YELLOW+"Warnung",ChatColor.YELLOW+"Der Server schließt in einer Halben Stunde");
                            if(now.getHour()==0 && now.getMinute()==55 && now.getSecond()<2)for (Player p : Bukkit.getOnlinePlayers())p.sendTitle(ChatColor.RED+"Warnung",ChatColor.RED+"Der Server schließt in fünf Minuten");
                            if(now.getHour()==0 && now.getMinute()==59 && now.getSecond()>=45)for (Player p : Bukkit.getOnlinePlayers())p.sendTitle(ChatColor.DARK_RED+String.valueOf((58-now.getSecond())),"");
                            if(now.getHour()==0 && now.getMinute()==59 && now.getSecond()>=58)for (Player p : Bukkit.getOnlinePlayers())p.kick();

                            // Set the world time, making sure it stays within the 0-24000 ticks range
                            World world = Bukkit.getWorld("world");
                            world.setTime(newMinecraftTime % 24000L);
                        }
                    }
        },0,20);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {

                if(eiss.getBoolean("orgen"+".LowTOBig")){
                    oregen.sortLocationsByMostNegativeX(chunklust);
                } else {
                    oregen.sortLocationsByMostPositiveX(chunklust);
                }
                File eisfile = new File("plugins/KMS Plugins/DingeDiePassierenMussen","Eistellungen.yml");
                eiss=YamlConfiguration.loadConfiguration(eisfile);

            }
        },0,20*10);

        Bignumb=eiss.getInt("orgen"+".Bignumb");

        for (int i=1; i<100000;i++){
            if(safething3.get("Nochzu"+"."+String.valueOf(i))==null)break;
            chunklust.add(new Location(Bukkit.getWorld(safething3.getString("Nochzu"+"."+String.valueOf(i)+".welt"))
                    ,safething3.getInt("Nochzu"+"."+String.valueOf(i)+".x"),0
                    ,safething3.getInt("Nochzu"+"."+String.valueOf(i)+".z")));
            safething3.set("Nochzu"+"."+String.valueOf(i),null);
        }

        if(eiss.getBoolean("orgen"+".LowTOBig")){
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN+"Chunk generation: "+ChatColor.GREEN+"Low To High"+ChatColor.WHITE+", Plugin: "+ChatColor.YELLOW+eiss.getBoolean("orgen"+".Starteating"));
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN+"Chunk generation: "+ChatColor.GREEN+"High To Low"+ChatColor.WHITE+", Plugin: "+ChatColor.YELLOW+eiss.getBoolean("orgen"+".Starteating"));
        }

        int x=eiss.getBoolean("orgen"+".LowTOBig") ? -eiss.getInt("orgen"+".MaxX")+16 : eiss.getInt("orgen"+".MaxX");
        int z=eiss.getBoolean("orgen"+".LowTOBig") ? -eiss.getInt("orgen"+".MaxZ")+16 : eiss.getInt("orgen"+".MaxZ");
        if(chunklust.size()==0){
            chunklust.add(new Location(Bukkit.getWorlds().get(0),x,0,z));
            Bukkit.getConsoleSender().sendMessage("Startet Generating chunk at: "+x+"x "+z+"z");
        }

        Bukkit.getPluginManager().registerEvents(new joinleavetracker(),this);
        Bukkit.getPluginManager().registerEvents(new mobdrops(),this);
        Bukkit.getPluginManager().registerEvents(new spiderspawn(),this);
        Bukkit.getPluginManager().registerEvents(new spawnerdelete(),this);
        Bukkit.getPluginManager().registerEvents(new infentare(),this);
        Bukkit.getPluginManager().registerEvents(new noenchants(),this);
        Bukkit.getPluginManager().registerEvents(new nogrow(),this);
        Bukkit.getPluginManager().registerEvents(new destorydiamonds(),this);
        Bukkit.getPluginManager().registerEvents(new onspawn(),this);
        Bukkit.getPluginManager().registerEvents(new guicontroller(),this);
        if(eiss.getBoolean("orgen"+".Starteating"))Bukkit.getPluginManager().registerEvents(new oregen(),this);
        if(eiss.getBoolean("orgen"+".RemoveRegion"))Bukkit.getPluginManager().registerEvents(new regiondelete(),this);

        getCommand("loadit").setExecutor(new loadit());
        getCommand("spinfo").setExecutor(new info());
        getCommand("givetime").setExecutor(new timecom());
        getCommand("worldtimeset").setExecutor(new settime());
        getCommand("spielerinfo").setExecutor(new playerlog());
        //getCommand("delwol").setExecutor(new delet());

        if(eiss.get("orgen")==null){

            eiss.set("orgen"+".Bignumb",0);
            eiss.set("orgen"+".RemoveRegion",false);
            eiss.set("orgen"+".Starteating",false);
            eiss.set("orgen"+".LowTOBig",true);
            eiss.set("orgen"+".MaxX",5000);
            eiss.set("orgen"+".MaxZ",5000);
            eiss.set("orgen"+".DIAMOND_ORE",0);
            eiss.set("orgen"+".DEEPSLATE_DIAMOND_ORE",0);
            eiss.set("orgen"+".IRON_ORE",60);
            eiss.set("orgen"+".DEEPSLATE_IRON_ORE",60);
            eiss.set("orgen"+".GOLD_ORE",60);
            eiss.set("orgen"+".DEEPSLATE_GOLD_ORE",60);
            eiss.set("orgen"+".COAL_ORE",60);
            eiss.set("orgen"+".DEEPSLATE_COAL_ORE",60);
            eiss.set("orgen"+".LAPIS_ORE",60);
            eiss.set("orgen"+".DEEPSLATE_LAPIS_ORE",60);
            eiss.set("orgen"+".COPPER_ORE",60);
            eiss.set("orgen"+".DEEPSLATE_COPPER_ORE",60);
            eiss.set("orgen"+".REDSTONE_ORE",60);
            eiss.set("orgen"+".DEEPSLATE_REDSTONE_ORE",60);
            eiss.set("orgen"+".EMERALD_ORE",60);
            eiss.set("orgen"+".DEEPSLATE_EMERALD_ORE",60);
            eiss.set("orgen"+".NETHER_QUARTZ_ORE",60);
            eiss.set("orgen"+".NETHER_GOLD_ORE",60);
            eiss.set("orgen"+".REDSTONE_ORE",60);
            eiss.set("orgen"+".REDSTONE_ORE",60);
            eiss.set("orgen"+".ANCIENT_DEBRIS",60);

            eiss.set("dodaynighttime",true);
            eiss.set("daynightinminuten",360);
            eiss.set("spawnschutz",true);

            eiss.set("mobs"+".WITCH",false);
            eiss.set("mobs"+".EVOKER",false);
            eiss.set("mobs"+".VILLAGER",false);
            eiss.set("mobs"+".ZOMBIE_VILLAGER",false);
            eiss.set("mobs"+".WANDERING_TRADER",false);

            eiss.set("nomobdrops"+".WITCH",false);

            eiss.set("nomobxp"+".WITCH",false);


            eiss.set("enchantments"+".MaxLv",10);
            eiss.set("enchantments"+".MENDING",false);
            eiss.set("enchantments"+".ARROW_INFINITE",false);

            eiss.set("Blocks"+".BREWING_STAND",false);
            eiss.set("Ambos2slot",false);

            eiss.set("Wachstum"+".VINE",false);


        }

        try {
            eiss.save(file);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static Monsterplugin getPlugin() {
        return plugin;
    }


    @Override
    public void onDisable() {

        int firstfree=1;
        for (Location l : chunklust){
            safething3.set("Nochzu"+"."+String.valueOf(firstfree)+".welt",l.getWorld().getName());
            safething3.set("Nochzu"+"."+String.valueOf(firstfree)+".x",l.getBlock().getX());
            safething3.set("Nochzu"+"."+String.valueOf(firstfree)+".z",l.getBlock().getZ());
            firstfree++;
        }


        eiss.set("orgen"+".Bignumb",Bignumb);
        try {
            eiss.save(file);
            //safething.save(filee);
            safething3.save(file2);
        } catch (IOException i) {
            i.printStackTrace();
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED+"Shit is Saved");
    }

}
