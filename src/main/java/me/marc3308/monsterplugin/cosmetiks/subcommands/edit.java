package me.marc3308.monsterplugin.cosmetiks.subcommands;

import me.marc3308.monsterplugin.cosmetiks.Cosmetikobjekt;
import me.marc3308.monsterplugin.cosmetiks.subcommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

import static me.marc3308.monsterplugin.Monsterplugin.cosmetikslist;

public class edit extends subcommand {
    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public String getDescription() {
        return "edits a cosmetik";
    }

    @Override
    public String getSyntax() {
        return "/cosmetics edit <Name> <art> <neuerwert>";
    }

    @Override
    public void perform(Player p, String[] args) {

        if(args.length<4){
            p.sendMessage(ChatColor.RED+"Syntax: "+getSyntax());
            return;
        }

        for (Cosmetikobjekt cosmetikobjekt : cosmetikslist) {
            if(cosmetikobjekt.getName().equals(args[1])){
                switch (args[2]){
                    case "Name":
                        cosmetikobjekt.setName(args[3]);
                        break;
                    case "Nummer":
                        cosmetikobjekt.setCustommodeldata(Integer.parseInt(args[3]));
                        break;
                    case "Block":
                        cosmetikobjekt.setMaterial(args[3]);
                        break;
                    case "Körperteil":
                        cosmetikobjekt.setKörperteil(args[3]);
                        break;
                    case "Bedingung":
                        cosmetikobjekt.setBedingung(args[3].equals("remove") ? null : args[3]);
                        break;
                    case "Beschreibung":
                        String newname=args[3];
                        for(int i=4;i<args.length;i++)newname+=" "+args[i];
                        String[] beschreibung=newname.split(";");
                        cosmetikobjekt.setBeschreibung(List.of(beschreibung));
                        break;
                    default:
                        p.sendMessage(ChatColor.RED+"Syntax: "+getSyntax());
                        return;
                }
                p.sendMessage(ChatColor.GREEN+"Cosmetik edited");
                Cosmetikobjekt.savecosmis();
                return;
            }
        }

    }
}
