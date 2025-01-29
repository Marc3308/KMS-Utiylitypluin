package me.marc3308.monsterplugin.chess;

import me.marc3308.monsterplugin.chess.objekts.Chessbord;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static me.marc3308.monsterplugin.chess.utilitys.schachliste;

public class chesscommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))return false;
        Player p= (Player) sender;
        //chess name x y z x y z
        if(args.length<2){
            p.sendMessage("§c/chess <thing> <name>");
            return false;
        }

        try {

            switch (args[0]){
                case "create":
                    schachliste.add(new Chessbord(p.getLocation(),args[1],false));
                    p.sendMessage(ChatColor.DARK_GREEN+"Schachbrett erstellt: "+ChatColor.GREEN+args[1]);
                    break;
                case "edit":
                    switch (args[2]){
                        case "Name":
                            schachliste.stream().filter(chessbord -> chessbord.getName().equals(args[1])).forEach(chessbord -> chessbord.setName(args[3]));
                            p.sendMessage(ChatColor.DARK_GREEN+"Schachbrett Name geändert: "+ChatColor.GREEN+args[1] + " -> "+args[3]);
                            break;
                        case "Location":
                            schachliste.stream().filter(chessbord -> chessbord.getName().equals(args[1])).findFirst().ifPresent(chessbord -> {
                                chessbord.setGame(null);
                                schachliste.remove(chessbord);
                                schachliste.add(new Chessbord(p.getLocation(),args[1],false));
                                p.sendMessage(ChatColor.DARK_GREEN+"Schachbrett Location geändert: "+ChatColor.GREEN+args[1]);
                            });
                            case "Help":
                            schachliste.stream().filter(chessbord -> chessbord.getName().equals(args[1])).findFirst().ifPresent(chessbord -> {
                                chessbord.setHelp(!chessbord.isHelp());
                                p.sendMessage(ChatColor.DARK_GREEN+"Schachbrett Help geändert: "+(chessbord.isHelp() ? ChatColor.GREEN : ChatColor.RED)+chessbord.isHelp());
                            });
                            break;
                    }
                    break;
                case "info":
                    schachliste.stream().filter(chessbord -> chessbord.getName().equals(args[1])).findFirst().ifPresent(chessbord -> {
                        p.sendMessage(ChatColor.DARK_GREEN+"------------Info-----------");
                        p.sendMessage(ChatColor.DARK_GREEN+"Name: "+ChatColor.GREEN+chessbord.getName());
                        TextComponent loc1=new TextComponent(ChatColor.DARK_GREEN+"Eckpunkt 1: "+ChatColor.YELLOW+"["+(int) chessbord.getFeltstart().getX()+"x "+chessbord.getFeltstart().getY()+"y "+(int) chessbord.getFeltstart().getZ()+"z ]");
                        loc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tp "+chessbord.getFeltstart().getX() +" "+(chessbord.getFeltstart().getY()+1)+ " "+ chessbord.getFeltstart().getZ()));
                        p.sendMessage(loc1);
                        TextComponent loc2=new TextComponent(ChatColor.DARK_GREEN+"Eckpunkt 2: "+ChatColor.YELLOW+"["+(int) chessbord.getFeltend().getX()+"x "+chessbord.getFeltend().getY()+"y "+(int) chessbord.getFeltend().getZ()+"z ]");
                        loc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tp "+chessbord.getFeltend().getX() +" "+(chessbord.getFeltend().getY()+1)+ " "+ chessbord.getFeltend().getZ()));
                        p.sendMessage(loc2);
                        p.sendMessage(ChatColor.DARK_GREEN+(chessbord.hasGame() ? "--------Spielstand---------" : "---------------------------"));
                        if(chessbord.hasGame()){
                            for (int i = 7; i >= 0; i--) {
                                String row="";
                                for (int j = 0; j < 8; j++) {
                                    if(chessbord.getGame().getBord()[i][j]!=null){
                                        row+=((i==0 && j==0) || (i+j)%2==0 ? ChatColor.GRAY : ChatColor.WHITE) +"["
                                                +(chessbord.getGame().getBord()[i][j].isWhite() ? ChatColor.WHITE : ChatColor.GRAY)
                                                +(chessbord.getGame().getBord()[i][j].getArmorStand().getName().substring(2,3))+((i==0 && j==0) || (i+j)%2==0 ? ChatColor.GRAY : ChatColor.WHITE)+"]";
                                    } else {
                                        row+=(((i==0 && j==0) || (i+j)%2==0) ? ChatColor.GRAY : ChatColor.WHITE)+"[_]";
                                    }
                                }
                                p.sendMessage(row);
                            }
                            p.sendMessage(ChatColor.DARK_GREEN+"HilfeStellung: "+ChatColor.GREEN+chessbord.isHelp());
                            p.sendMessage(ChatColor.DARK_GREEN+"Spieler1: "+ChatColor.GREEN+chessbord.getGame().getSpieler1().getName());
                            p.sendMessage(ChatColor.DARK_GREEN+"Spieler2: "+ChatColor.GREEN+chessbord.getGame().getSpieler2().getName());
                            p.sendMessage(ChatColor.DARK_GREEN+"Zug: "+ChatColor.GREEN+chessbord.getGame().getTurn());
                            p.sendMessage(ChatColor.DARK_GREEN+"---------------------------");

                        }
                    });
                    break;
                case "delete":
                    schachliste.stream().filter(chessbord -> chessbord.getName().equals(args[1])).findFirst().ifPresent(chessbord -> {
                        chessbord.setGame(null);
                        schachliste.remove(chessbord);
                        p.sendMessage(ChatColor.DARK_GREEN+"Schachbrett gelöscht: "+ChatColor.GREEN+args[1]);
                    });
                    break;
            }


        } catch (NumberFormatException e) {
            p.sendMessage("§c/chess <thing> <name>");
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        ArrayList<String> list =new ArrayList<>();

        try {
            if(args.length == 0)return list;
            if(args.length == 1){
                list.add("create");
                list.add("edit");
                list.add("info");
                list.add("delete");
            }
            if(args.length == 2){
                if(args[0].equals("create")){
                    list.add("<Name>");
                } else {
                    schachliste.stream().map(Chessbord::getName).forEach(list::add);
                }
            }
            if(!args[0].equals("edit"))return list;
            if(args.length == 3){
                list.add("Name");
                list.add("Location");
                list.add("Help");
            }
            if(args.length==4 && args[2].equals("Name"))list.add("<Name>");

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
}
