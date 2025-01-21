package me.marc3308.monsterplugin.cosmetiks.subcommands;

import me.marc3308.monsterplugin.cosmetiks.subcommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static me.marc3308.monsterplugin.Monsterplugin.cosmetikslist;

public class info extends subcommand {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "gives infos of a cosmetik";
    }

    @Override
    public String getSyntax() {
        return "/cosmetik info <cosmetik>";
    }

    @Override
    public void perform(Player p, String[] args) {

        if(args.length<2){
            p.sendMessage("Please try: "+getSyntax());
            return;
        }

        for (int i = 0; i < cosmetikslist.size(); i++) {
            if(cosmetikslist.get(i).getName().equalsIgnoreCase(args[1])){
                p.sendMessage(ChatColor.DARK_GREEN+"------------Info-----------");
                p.sendMessage(ChatColor.DARK_GREEN+"Name: "+ChatColor.GREEN+cosmetikslist.get(i).getName());
                p.sendMessage(ChatColor.DARK_GREEN+"Beschreibung: "+ChatColor.GREEN+ChatColor.GREEN+cosmetikslist.get(i).getBeschreibung());
                p.sendMessage(ChatColor.DARK_GREEN+"Nummer: "+ChatColor.GREEN+cosmetikslist.get(i).getCustommodeldata());
                p.sendMessage(ChatColor.DARK_GREEN+"Körperteil: "+ChatColor.GREEN+cosmetikslist.get(i).getKörperteil());
                p.sendMessage(ChatColor.DARK_GREEN+"Bedingung: "+ChatColor.GREEN+cosmetikslist.get(i).getBedingung());
                p.sendMessage(ChatColor.DARK_GREEN+"---------------------------");
                return;
            }
        }

    }
}
