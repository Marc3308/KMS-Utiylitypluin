package me.marc3308.monsterplugin.cosmetiks.subcommands;

import me.marc3308.monsterplugin.Monsterplugin;
import me.marc3308.monsterplugin.cosmetiks.Cosmetikobjekt;
import me.marc3308.monsterplugin.cosmetiks.subcommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import static me.marc3308.monsterplugin.Monsterplugin.cosmetikslist;

public class spieler extends subcommand {

    @Override
    public String getName() {
        return "spieler";
    }

    @Override
    public String getDescription() {
        return "spieler a cosmetik";
    }

    @Override
    public String getSyntax() {
        return "/cosmetics spieler <player> <status> <cosmetik>";
    }

    @Override
    public void perform(Player p, String[] args) {

        if (args.length<4){
            p.sendMessage("§cSyntax: "+getSyntax());
            return;
        }
        for (Cosmetikobjekt c : cosmetikslist){
            if(c.getName().equalsIgnoreCase(args[3])){
                for (Player of : Bukkit.getOnlinePlayers()){
                    if(of.getName().equalsIgnoreCase(args[1])){
                        if(args[2].equals("add")){
                            of.getPersistentDataContainer().set(new NamespacedKey(Monsterplugin.getPlugin(), c.getBedingung()),PersistentDataType.BOOLEAN,true);
                            p.sendMessage(ChatColor.DARK_GREEN+of.getName()+" hat nun die Cosmetic: "+ChatColor.GREEN+c.getName());
                            of.sendMessage(ChatColor.DARK_GREEN+"Du hast nun die Cosmetic: "+ChatColor.GREEN+c.getName());
                        } else {
                            of.getPersistentDataContainer().remove(new NamespacedKey(Monsterplugin.getPlugin(), c.getBedingung()));
                            p.sendMessage(ChatColor.DARK_GREEN+of.getName()+" hat nun nicht mehr die Cosmetic: "+ChatColor.GREEN+c.getName());
                            of.sendMessage(ChatColor.DARK_GREEN+"Du hast nun nicht mehr die Cosmetic: "+ChatColor.GREEN+c.getName());
                        }
                        return;
                    }
                }
            }
        }
    }
}
