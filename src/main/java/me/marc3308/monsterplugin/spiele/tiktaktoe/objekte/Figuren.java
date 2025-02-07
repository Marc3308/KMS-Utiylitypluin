package me.marc3308.monsterplugin.spiele.tiktaktoe.objekte;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public abstract class Figuren {

    private ArmorStand ar;

    abstract public ArmorStand getArmorStand();
}

class Schwartz extends Figuren {

    private ArmorStand ar;

    public Schwartz(Location loc) {
        ar = loc.getWorld().spawn(loc.clone().add(0.5,-3,0.5),ArmorStand.class);

    }

    @Override
    public ArmorStand getArmorStand() {
        return ar;
    }
}
