package me.marc3308.monsterplugin.cosmetiks.subcommands;

import me.marc3308.monsterplugin.cosmetiks.Cosmetikobjekt;
import me.marc3308.monsterplugin.cosmetiks.subcommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static me.marc3308.monsterplugin.Monsterplugin.cosmetikslist;

public class give extends subcommand {

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return "give a cosmetik";
    }

    @Override
    public String getSyntax() {
        return "/cosmetics give <cosmetik>";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(args.length<2){
            p.sendMessage(ChatColor.RED+"Syntax: "+getSyntax());
            return;
        }

        for (Cosmetikobjekt c : cosmetikslist){
            if(c.getName().equalsIgnoreCase(args[1])){
                ItemStack item = new ItemStack(Material.valueOf(c.getMaterial()));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(c.getName());
                meta.setLore(c.getBeschreibung());
                meta.setCustomModelData(c.getCustommodeldata());
                item.setItemMeta(meta);
                p.getWorld().dropItemNaturally(p.getLocation(),item);
                return;
            }
        }

    }
}
