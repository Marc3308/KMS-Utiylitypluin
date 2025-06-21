package me.marc3308.monsterplugin.spiele.chess.objekts;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static me.marc3308.monsterplugin.spiele.chess.chessutilitys.movefigure;

public abstract class Figuren {

    private boolean white;
    private ArmorStand ar;

    abstract public ArmorStand getArmorStand();
    abstract public boolean isWhite();

    public ArmorStand getpasteArmorStand(Location loc, String name, Material mat, int customModelData, double grose) {
        ArmorStand armor = loc.getWorld().spawn(loc.clone().add(0.5,-(3*grose),0.5),ArmorStand.class);

        armor.setInvulnerable(true);
        armor.setCustomName(name);
        armor.setCustomNameVisible(false);
        armor.setGravity(false);
        armor.setVisible(false);
        armor.setMarker(true);
        armor.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(grose);
        ItemStack helmet = new ItemStack(mat);
        ItemMeta meta = helmet.getItemMeta();
        meta.setCustomModelData(customModelData);
        helmet.setItemMeta(meta);
        armor.setHelmet(helmet);
        return armor;
    }

}

class Bauer extends Figuren{

    private boolean white;
    private ArmorStand ar;
    public Bauer(Location loc,Material mat,int customModelData, boolean white,double grose){
        this.white=white;
        ar = getpasteArmorStand(loc,(white ? ChatColor.WHITE : ChatColor.DARK_GRAY)+"Bauer",mat,customModelData,grose);
        movefigure(ar,loc.clone().add(0.5,-(2*grose)+0.1,0.5),3);
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
    public Turm(Location loc,Material mat,int customModelData, boolean white,double grose){
        this.white=white;
        ar = getpasteArmorStand(loc,(white ? ChatColor.WHITE : ChatColor.DARK_GRAY)+"Turm",mat,customModelData,grose);
        movefigure(ar,loc.clone().add(0.5,-(2*grose)+0.1,0.5),3);
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
    public Springer(Location loc,Material mat,int customModelData, boolean white,double grose){
        this.white=white;
        ar = getpasteArmorStand(loc,(white ? ChatColor.WHITE : ChatColor.DARK_GRAY)+"Springer",mat,customModelData,grose);
        movefigure(ar,loc.clone().add(0.5,-(2*grose)+0.1,0.5),3);
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
    public Laufer(Location loc,Material mat,int customModelData, boolean white,double grose){
        this.white=white;
        ar = getpasteArmorStand(loc,(white ? ChatColor.WHITE : ChatColor.DARK_GRAY)+"Laufer",mat,customModelData,grose);
        movefigure(ar,loc.clone().add(0.5,-(2*grose)+0.1,0.5),3);
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
    public Dame(Location loc, Material mat, int customModelData, boolean white,double grose){
        this.white=white;
        ar = getpasteArmorStand(loc,(white ? ChatColor.WHITE : ChatColor.DARK_GRAY)+"Dame",mat,customModelData,grose);
        movefigure(ar,loc.clone().add(0.5,-(2*grose)+0.1,0.5),3);
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
    public Koenig(Location loc,Material mat,int customModelData, boolean white,double grose){
        this.white=white;
        ar = getpasteArmorStand(loc,(white ? ChatColor.WHITE : ChatColor.DARK_GRAY)+"König",mat,customModelData,grose);
        movefigure(ar,loc.clone().add(0.5,-(2*grose)+0.1,0.5),3);
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


