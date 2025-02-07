package me.marc3308.monsterplugin.spiele.tiktaktoe.objekte;

import org.bukkit.Location;

public class tiktakbord {

    private String name;
    private Location feltstart;
    private Location feltend;


    public void tiktakbord(String name, Location loc) {
        this.name=name;
        this.feltstart = loc.getBlock().getLocation();
        this.feltend = loc.getBlock().getLocation().add(3,0,3);
    }

    public Location getFeltend() {
        return feltend;
    }

    public Location getFeltstart() {
        return feltstart;
    }

    public String getName() {
        return name;
    }
}
