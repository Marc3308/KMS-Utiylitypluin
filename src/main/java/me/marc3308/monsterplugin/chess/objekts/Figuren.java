package me.marc3308.monsterplugin.chess.objekts;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static me.marc3308.monsterplugin.chess.utilitys.movefigure;

public abstract class Figuren {

    private boolean white;
    private ArmorStand ar;

    abstract public ArmorStand getArmorStand();
    abstract public boolean isWhite();

}

class Bauer extends Figuren{

    private boolean white;
    private ArmorStand ar;
    public Bauer(Location loc,Material mat,int customModelData, boolean white){
        this.white=white;

        ar = loc.getWorld().spawn(loc.clone().add(0.5,-3,0.5),ArmorStand.class);
        ar.setInvulnerable(true);
        ar.setCustomName((white ? ChatColor.WHITE : ChatColor.DARK_GRAY)+"Bauer");
        ar.setCustomNameVisible(false);
        ar.setGravity(false);
        ar.setVisible(false);
        ar.setMarker(true);
        ItemStack helmet = new ItemStack(mat);
        ItemMeta meta = helmet.getItemMeta();
        meta.setCustomModelData(customModelData);
        helmet.setItemMeta(meta);
        ar.setHelmet(helmet);

        movefigure(ar,loc.clone().add(0.5,-1.9,0.5),3);
    }

    @Override
    public ArmorStand getArmorStand() {
        return ar;
    }

    @Override
    public boolean isWhite() {
        return white;
    }
}

class Turm extends Figuren{

    private boolean white;
    private ArmorStand ar;
    public Turm(Location loc,Material mat,int customModelData, boolean white){
        this.white=white;

        ar = loc.getWorld().spawn(loc.clone().add(0.5,-3,0.5),ArmorStand.class);
        ar.setInvulnerable(true);
        ar.setCustomName((white ? ChatColor.WHITE : ChatColor.DARK_GRAY)+"Turm");
        ar.setCustomNameVisible(false);
        ar.setGravity(false);
        ar.setVisible(false);
        ar.setMarker(true);
        ItemStack helmet = new ItemStack(mat);
        ItemMeta meta = helmet.getItemMeta();
        meta.setCustomModelData(customModelData);
        helmet.setItemMeta(meta);
        ar.setHelmet(helmet);

        movefigure(ar,loc.clone().add(0.5,-1.9,0.5),3);
    }

    @Override
    public ArmorStand getArmorStand() {
        return ar;
    }

    @Override
    public boolean isWhite() {
        return white;
    }
}

class Springer extends Figuren{

    private boolean white;
    private ArmorStand ar;
    public Springer(Location loc,Material mat,int customModelData, boolean white){
        this.white=white;

        ar = loc.getWorld().spawn(loc.clone().add(0.5,-3,0.5),ArmorStand.class);
        ar.setInvulnerable(true);
        ar.setCustomName((white ? ChatColor.WHITE : ChatColor.DARK_GRAY)+"Springer");
        ar.setCustomNameVisible(false);
        ar.setGravity(false);
        ar.setVisible(false);
        ar.setMarker(true);
        ItemStack helmet = new ItemStack(mat);
        ItemMeta meta = helmet.getItemMeta();
        meta.setCustomModelData(customModelData);
        helmet.setItemMeta(meta);
        ar.setHelmet(helmet);

        movefigure(ar,loc.clone().add(0.5,-1.9,0.5),3);
    }

    @Override
    public ArmorStand getArmorStand() {
        return ar;
    }

    @Override
    public boolean isWhite() {
        return white;
    }
}

class Laufer extends Figuren{

    private boolean white;
    private ArmorStand ar;
    public Laufer(Location loc,Material mat,int customModelData, boolean white){
        this.white=white;

        ar = loc.getWorld().spawn(loc.clone().add(0.5,-3,0.5),ArmorStand.class);
        ar.setInvulnerable(true);
        ar.setCustomName((white ? ChatColor.WHITE : ChatColor.DARK_GRAY)+"Laufer");
        ar.setCustomNameVisible(false);
        ar.setGravity(false);
        ar.setVisible(false);
        ar.setMarker(true);
        ItemStack helmet = new ItemStack(mat);
        ItemMeta meta = helmet.getItemMeta();
        meta.setCustomModelData(customModelData);
        helmet.setItemMeta(meta);
        ar.setHelmet(helmet);

        movefigure(ar,loc.clone().add(0.5,-1.9,0.5),3);
    }

    @Override
    public ArmorStand getArmorStand() {
        return ar;
    }

    @Override
    public boolean isWhite() {
        return white;
    }
}

class Dame extends Figuren{

    private boolean white;
    private ArmorStand ar;
    public Dame(Location loc, Material mat, int customModelData, boolean white){
        this.white=white;

        ar = loc.getWorld().spawn(loc.clone().add(0.5,-3,0.5),ArmorStand.class);
        ar.setInvulnerable(true);
        ar.setCustomName((white ? ChatColor.WHITE : ChatColor.DARK_GRAY)+"Dame");
        ar.setCustomNameVisible(false);
        ar.setGravity(false);
        ar.setVisible(false);
        ar.setMarker(true);
        ItemStack helmet = new ItemStack(mat);
        ItemMeta meta = helmet.getItemMeta();
        meta.setCustomModelData(customModelData);
        helmet.setItemMeta(meta);
        ar.setHelmet(helmet);

        movefigure(ar,loc.clone().add(0.5,-1.9,0.5),3);
    }

    @Override
    public ArmorStand getArmorStand() {
        return ar;
    }

    @Override
    public boolean isWhite() {
        return white;
    }
}

class Koenig extends Figuren{

    private boolean white;
    private ArmorStand ar;
    public Koenig(Location loc,Material mat,int customModelData, boolean white){
        this.white=white;

        ar = loc.getWorld().spawn(loc.clone().add(0.5,-3,0.5),ArmorStand.class);
        ar.setInvulnerable(true);
        ar.setCustomName((white ? ChatColor.WHITE : ChatColor.DARK_GRAY)+"König");
        ar.setCustomNameVisible(false);
        ar.setGravity(false);
        ar.setVisible(false);
        ar.setMarker(true);
        ItemStack helmet = new ItemStack(mat);
        ItemMeta meta = helmet.getItemMeta();
        meta.setCustomModelData(customModelData);
        helmet.setItemMeta(meta);
        ar.setHelmet(helmet);

        movefigure(ar,loc.clone().add(0.5,-1.9,0.5),3);
    }

    @Override
    public ArmorStand getArmorStand() {
        return ar;
    }

    @Override
    public boolean isWhite() {
        return white;
    }
}
