package me.marc3308.monsterplugin.cosmetiks.subcommands;

import me.marc3308.monsterplugin.cosmetiks.Cosmetikobjekt;
import me.marc3308.monsterplugin.cosmetiks.subcommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static me.marc3308.monsterplugin.Monsterplugin.cosmetikslist;

public class add extends subcommand {
    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "add a new cosmetik";
    }

    @Override
    public String getSyntax() {
        return "/cosmetics add <Name> <Nummer> <Block> <Körperteil>";
    }

    @Override
    public void perform(Player p, String[] args) {

        if(args.length<5){
            p.sendMessage(ChatColor.RED+"Syntax: "+getSyntax());
            return;
        }
        cosmetikslist.add(new Cosmetikobjekt(args[1],new ArrayList<>(),Integer.parseInt(args[2]),args[3],args[4],null));
        p.sendMessage(ChatColor.GREEN+"Cosmetik added");
        Cosmetikobjekt.savecosmis();
    }
}
