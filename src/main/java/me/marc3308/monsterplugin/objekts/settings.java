package me.marc3308.monsterplugin.objekts;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class settings {

    private ArrayList<Material> deniedblocks = new ArrayList<>();
    private HashMap<Material, Integer> persentblocks = new HashMap<>();
    private boolean denied;
    private boolean present;
    private int big;
    private boolean start;


    public settings() {

        File cosfile = new File("plugins/KMS Plugins/DingeDiePassierenMussen","Settings.yml");
        FileConfiguration grundconf= YamlConfiguration.loadConfiguration(cosfile);

        start=false;
        denied = grundconf.getBoolean("den");
        present = grundconf.getBoolean("pres");
        big = grundconf.getInt("big");
        deniedblocks = new ArrayList<>(){{
            Arrays.stream(Material.values()).filter(m -> grundconf.contains("denM."+m.name()))
                    .forEach(m -> add(m));
        }};
        persentblocks = new HashMap<Material, Integer>(){{
            Arrays.stream(Material.values()).filter(m -> grundconf.contains("Prb."+m.name()))
                    .forEach(m -> put(m, grundconf.getInt("Prb."+m.name())));
        }};

    }

    public void setDenied(boolean denied) {
        this.denied = denied;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public void setBig(int big) {
        this.big = big;
    }

    public boolean isDenied() {
        return denied;
    }

    public boolean isPresent() {
        return present;
    }

    public boolean isStart() {
        return start;
    }

    public int getBig() {
        return big;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public ArrayList<Material> getDeniedblocks() {
        return deniedblocks;
    }

    public HashMap<Material, Integer> getPersentblocks() {
        return persentblocks;
    }

    public void start(){
        if(!start){
            start = true;
            new Chunkeater(-big, big, -big, big);
        } else {
            System.out.println("Chunkeater is already running.");
        }
    }

    public void save(){
        File cosfile = new File("plugins/KMS Plugins/DingeDiePassierenMussen","Settings.yml");
        cosfile.delete();
        FileConfiguration grundconf= YamlConfiguration.loadConfiguration(cosfile);

        grundconf.set("den",denied);
        grundconf.set("pres",present);
        grundconf.set("big",big);
        deniedblocks.forEach(m -> grundconf.set("denM."+m.name(), true));
        persentblocks.forEach((mat, prob) -> grundconf.set("Prb."+mat.name(), prob));

        try {
            grundconf.save(cosfile);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
