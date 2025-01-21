package me.marc3308.monsterplugin.cosmetiks.subcommands;

import me.marc3308.monsterplugin.cosmetiks.Cosmetikobjekt;
import me.marc3308.monsterplugin.cosmetiks.subcommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static me.marc3308.monsterplugin.Monsterplugin.cosmetikslist;

public class remove extends subcommand {
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "remove a cosmetik";
    }

    @Override
    public String getSyntax() {
        return "/cosmetiks remove <Name>";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(args.length<2){
            p.sendMessage(ChatColor.RED+"Syntax: "+getSyntax());
            return;
        }
        for (Cosmetikobjekt cosmetikobjekt : cosmetikslist) {
            if(cosmetikobjekt.getName().equals(args[1])){
                cosmetikslist.remove(cosmetikobjekt);
                p.sendMessage(ChatColor.GREEN+"Cosmetik removed");
                Cosmetikobjekt.savecosmis();
                return;
            }
        }
        p.sendMessage(ChatColor.RED+"Cosmetik nicht gefunden");
    }
}
