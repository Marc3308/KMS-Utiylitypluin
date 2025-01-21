package me.marc3308.monsterplugin.cosmetiks;

import me.marc3308.monsterplugin.Monsterplugin;
import me.marc3308.monsterplugin.cosmetiks.subcommands.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.marc3308.monsterplugin.Monsterplugin.cosmetikslist;

public class Commandmanager implements CommandExecutor, TabCompleter {

    private ArrayList<subcommand> subcommands =new ArrayList<>();


    public Commandmanager(){
        subcommands.add(new help());
        subcommands.add(new add());
        subcommands.add(new remove());
        subcommands.add(new edit());
        subcommands.add(new info());
        subcommands.add(new give());
        subcommands.add(new spieler());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))return false;
        Player p= (Player) sender;
        if(args.length>0){

            if(!p.hasPermission("technikdetails"))return false;

            switch (args[0]){
                case "load":
                    Cosmetikobjekt.loadlist();
                    p.sendMessage("§aCosmetiks wurden geladen");
                    break;
                case "add":
                    getSubcommands().get(1).perform(p,args);
                    break;
                case "remove":
                    getSubcommands().get(2).perform(p,args);
                    break;
                case "edit":
                    getSubcommands().get(3).perform(p,args);
                    break;
                case "info":
                    getSubcommands().get(4).perform(p,args);
                    break;
                case "give":
                    getSubcommands().get(5).perform(p,args);
                    break;
                case "spieler":
                    getSubcommands().get(6).perform(p,args);
                    break;
                default:
                    getSubcommands().get(0).perform(p,args);
                    break;
            }
        } else {
            getSubcommands().get(0).perform(p,args);
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p= (Player) sender;
        ArrayList<String> list =new ArrayList<>();

        try {
            if(args.length == 0)return list;
            if(args.length == 1){
                for (subcommand sub : subcommands)list.add(sub.getName().toString());
                list.add("load");
            }
            if(args[0].equals("help") || args[0].equals("load"))return list;
            if(args.length == 2){
                if(args[0].equals("add")){
                    list.add("<Name>");
                } else if (args[0].equals("spieler")){
                    Bukkit.getOnlinePlayers().forEach(player -> list.add(player.getName()));
                } else {
                    cosmetikslist.forEach(cosmetikobjekt -> list.add(cosmetikobjekt.getName()));
                }
            }
            if(args.length == 3){
                switch (args[0]){
                    case "edit":
                        if(!p.hasPermission("technikdetails"))return list;
                        list.add("Name");
                        list.add("Beschreibung");
                        list.add("Nummer");
                        list.add("Material");
                        list.add("Körperteil");
                        list.add("Bedingung");
                        break;
                    case "add":
                        list.add("<Nummer>");
                        break;
                    case "spieler":
                        list.add("add");
                        list.add("remove");
                        break;
                    default:
                        return list;
                }
            }
            if(args.length==4){
                switch (args[0]){
                    case "edit":
                        if(!p.hasPermission("technikdetails"))return list;
                        if(args[2].equals("Körperteil")){
                            list.add("Kopf");
                            list.add("Körper");
                            list.add("Beine");
                            list.add("Füße");
                            list.add("Mainhand");
                            list.add("Offhand");
                        } else {
                            if(args[2].equals("Bedingung"))list.add("remove");
                            list.add("<Neu>");
                        }
                        break;
                    case "add":
                        Arrays.stream(Material.values()).forEach(material -> list.add(material.toString()));
                        break;
                    case "spieler":
                        if(args[2].equals("add")){
                            cosmetikslist.stream().filter(c -> c.getBedingung()!=null).forEach(c -> {
                                Player player = Bukkit.getPlayer(args[1]);
                                if(!player.getPersistentDataContainer().has(new NamespacedKey(Monsterplugin.getPlugin(), c.getBedingung()), PersistentDataType.BOOLEAN)){
                                    list.add(c.getName());
                                }
                            });
                        } else {
                            cosmetikslist.stream().filter(c -> c.getBedingung()!=null).forEach(c -> {
                                Player player = Bukkit.getPlayer(args[1]);
                                if(player.getPersistentDataContainer().has(new NamespacedKey(Monsterplugin.getPlugin(), c.getBedingung()), PersistentDataType.BOOLEAN)){
                                    list.add(c.getName());
                                }
                            });
                        }
                        break;
                    default:
                        return list;
                }
            }
            if(args.length==5){
                if(args[0].equals("add")){
                    list.add("Kopf");
                    list.add("Körper");
                    list.add("Beine");
                    list.add("Füße");
                    list.add("Mainhand");
                    list.add("Offhand");
                } else {
                    return list;
                }
            }

            if(list.isEmpty())return list;

            //autocompetion
            ArrayList<String> commpleteList = new ArrayList<>();
            String currentarg = args[args.length-1].toLowerCase();
            for (String s : list){
                if(s==null)return list;
                String s1 =s.toLowerCase();
                if(s1.startsWith(currentarg)){
                    commpleteList.add(s);
                }
            }

            return commpleteList;
        } catch (CommandException e){
            return list;
        }
    }

    public ArrayList<subcommand> getSubcommands() {
        return subcommands;
    }

}
