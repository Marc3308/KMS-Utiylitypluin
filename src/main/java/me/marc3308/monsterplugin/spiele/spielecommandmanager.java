package me.marc3308.monsterplugin.spiele;

import me.marc3308.monsterplugin.spiele.chess.objekts.Chessbord;
import me.marc3308.monsterplugin.spiele.tiktaktoe.objekte.tiktakbord;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static me.marc3308.monsterplugin.spiele.chess.chessutilitys.darferdas;
import static me.marc3308.monsterplugin.spiele.chess.chessutilitys.schachliste;
import static me.marc3308.monsterplugin.spiele.tiktaktoe.tiktakutility.tiktakliste;

public class spielecommandmanager implements CommandExecutor, TabCompleter {

    private String error = "§2/spiele §e<game> <command> §4<args>..";
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))return false;
        Player p= (Player) sender;

        if(args.length<3){
            p.sendMessage(error);
            return false;
        }

        switch (args[0]){
            case "chess":
                chessgame(p,args);
                break;
            case "tictactoe":
                break;
            case "menschargernicht":
                break;
            default:
                p.sendMessage(error);
                return false;
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
                list.add("chess");
                list.add("tictactoe");
                list.add("menschargernicht");
            }

            if(args.length == 2){
                list.add("create");
                list.add("edit");
                list.add("info");
                list.add("delete");
            }

            if(args.length == 3){
                if(args[1].equals("create")){
                    list.add("<Name>");
                    return list;
                } else {
                    switch (args[0]){
                        case "chess":
                            schachliste.stream().map(Chessbord::getName).forEach(list::add);
                            break;
                        case "tictactoe":
                            tiktakliste.stream().map(tiktakbord::getName).forEach(list::add);
                            break;
                        case "menschargernicht":
                            break;
                        default:
                            return list;
                    }
                }
            }

            if(args.length == 4 && args[1].equals("edit")){
                list.add("Name");
                list.add("Location");
                switch (args[0]){
                    case "chess":
                        list.add("Help");
                        list.add("Time");
                        break;
                    case "tictactoe":
                        break;
                    case "menschargernicht":
                        list.add("spieleranzahl");
                        list.add("lauflänge");
                        list.add("figuanzahl");
                        list.add("würfelanzahl");
                        list.add("Time");
                        break;
                }
            }
            if(args.length == 5 && args[3].equals("Time"))list.add("<Time>");
            if(args.length == 5 && args[3].equals("Name"))list.add("<Name>");

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

    private void chessgame(Player p, String[] args){
        try {
            switch (args[1]){
                case "create":
                    schachliste.add(new Chessbord(p.getLocation(),args[2],false,3));
                    p.sendMessage(ChatColor.DARK_GREEN+"Schachbrett erstellt: "+ChatColor.GREEN+args[2]);
                    break;
                case "edit":
                    switch (args[3]){
                        case "Name":
                            schachliste.stream().filter(chessbord -> chessbord.getName().equals(args[2])).forEach(chessbord -> chessbord.setName(args[4]));
                            p.sendMessage(ChatColor.DARK_GREEN+"Schachbrett Name geändert: "+ChatColor.GREEN+args[2] + " -> "+args[4]);
                            break;
                        case "Location":
                            schachliste.stream().filter(chessbord -> chessbord.getName().equals(args[2])).findFirst().ifPresent(chessbord -> {
                                chessbord.setGame(null);
                                schachliste.remove(chessbord);
                                schachliste.add(new Chessbord(p.getLocation(),args[2],chessbord.isHelp(),chessbord.getTime()));
                                p.sendMessage(ChatColor.DARK_GREEN+"Schachbrett Location geändert: "+ChatColor.GREEN+args[2]);
                            });
                        case "Help":
                            schachliste.stream().filter(chessbord -> chessbord.getName().equals(args[2])).findFirst().ifPresent(chessbord -> {
                                chessbord.setHelp(!chessbord.isHelp());
                                p.sendMessage(ChatColor.DARK_GREEN+"Schachbrett Help geändert: "+(chessbord.isHelp() ? ChatColor.GREEN : ChatColor.RED)+chessbord.isHelp());
                            });
                            break;
                        case "Time":
                            schachliste.stream().filter(chessbord -> chessbord.getName().equals(args[2])).findFirst().ifPresent(chessbord -> {
                                chessbord.setTime(Integer.parseInt(args[4]));
                                p.sendMessage(ChatColor.DARK_GREEN+"Schachbrett Time geändert: "+ChatColor.GREEN+args[4]);
                            });
                            break;
                    }
                    break;
                case "info":
                    schachliste.stream().filter(chessbord -> chessbord.getName().equals(args[2])).findFirst().ifPresent(chessbord -> {
                        p.sendMessage(ChatColor.DARK_GREEN+"------------Info-----------");
                        p.sendMessage(ChatColor.DARK_GREEN+"Name: "+ChatColor.GREEN+chessbord.getName());
                        TextComponent loc1=new TextComponent(ChatColor.DARK_GREEN+"Eckpunkt 1: "+ChatColor.YELLOW+"["+(int) chessbord.getFeltstart().getX()+"x "+chessbord.getFeltstart().getY()+"y "+(int) chessbord.getFeltstart().getZ()+"z ]");
                        loc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tp "+chessbord.getFeltstart().getX() +" "+(chessbord.getFeltstart().getY()+1)+ " "+ chessbord.getFeltstart().getZ()));
                        p.sendMessage(loc1);
                        TextComponent loc2=new TextComponent(ChatColor.DARK_GREEN+"Eckpunkt 2: "+ChatColor.YELLOW+"["+(int) chessbord.getFeltend().getX()+"x "+chessbord.getFeltend().getY()+"y "+(int) chessbord.getFeltend().getZ()+"z ]");
                        loc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tp "+chessbord.getFeltend().getX() +" "+(chessbord.getFeltend().getY()+1)+ " "+ chessbord.getFeltend().getZ()));
                        p.sendMessage(loc2);
                        if(chessbord.hasGame()){
                            p.sendMessage(ChatColor.DARK_GREEN+(chessbord.hasGame() ? "--------Spielstand---------" : "---------------------------"));
                            for (int i = 7;i >= 0; i--){
                                Component row = Component.text("[" + (i + 1) + "]").color(NamedTextColor.DARK_GREEN); // Row label

                                for (int j =0; j < 8;j++) {

                                    // Determine board cell color
                                    ChatColor squareColor = chessbord.getGame().getTurn().split(":").length > 1
                                            && !chessbord.getGame().getTurn().split(":")[0].equals("promo")
                                            && !chessbord.getGame().getTurn().split(":")[0].equals("netfinished")
                                            && darferdas(chessbord, Integer.valueOf(chessbord.getGame().getTurn().split(":")[1]), Integer.valueOf(chessbord.getGame().getTurn().split(":")[2]), i, j)
                                            ? chessbord.getGame().getBord()[i][j] != null ? ChatColor.RED : ChatColor.GREEN : (i == 0 && j == 0) || (i + j) % 2 == 0 ? ChatColor.DARK_GRAY : ChatColor.WHITE;

                                    // Default empty cell component
                                    String columnLetter = switch (j) {
                                        case 0 -> "A";
                                        case 1 -> "B";
                                        case 2 -> "C";
                                        case 3 -> "D";
                                        case 4 -> "E";
                                        case 5 -> "F";
                                        case 6 -> "G";
                                        default -> "H";
                                    };

                                    String name = "";
                                    String hover = "";
                                    String command = columnLetter + (i + 1);
                                    Component boardCell;

                                    if (chessbord.getGame().getBord()[i][j] != null) {
                                        String pieceName = chessbord.getGame().getBord()[i][j].getArmorStand().getName().substring(2, 3);
                                        ChatColor pieceColor = chessbord.getGame().getTurn().split(":").length > 1
                                                && !chessbord.getGame().getTurn().split(":")[0].equals("promo")
                                                && !chessbord.getGame().getTurn().split(":")[0].equals("netfinished")
                                                && Integer.valueOf(chessbord.getGame().getTurn().split(":")[1]) == i
                                                && Integer.valueOf(chessbord.getGame().getTurn().split(":")[2]) == j ? ChatColor.GOLD : chessbord.getGame().getBord()[i][j].isWhite() ? ChatColor.WHITE : ChatColor.DARK_GRAY;

                                        name = squareColor + "[" + pieceColor + pieceName + squareColor + "]";
                                        hover = command + chessbord.getGame().getBord()[i][j].getArmorStand().getName().substring(0, 2) + " [" + chessbord.getGame().getBord()[i][j].getArmorStand().getName().substring(2) + "]";
                                    } else {
                                        name = squareColor + "[_]";
                                        hover = command;
                                    }
                                    Component normalcell = Component.text(name)
                                            .hoverEvent(HoverEvent.showText(Component.text(hover)));
                                    row = row.append(normalcell);
                                }

                                // Send the row component to the player
                                p.sendMessage(row);
                            }
                            p.sendMessage(Component.text("[X][A][B][C][D][E][F][G][H]")
                                    .color(NamedTextColor.DARK_GREEN));
                            p.sendMessage(ChatColor.DARK_GREEN+"HilfeStellung: "+ChatColor.GREEN+chessbord.isHelp());
                            p.sendMessage(ChatColor.DARK_GREEN+"Spieler1: "+ChatColor.GREEN+chessbord.getGame().getSpieler1().getName());
                            p.sendMessage(ChatColor.DARK_GREEN+"Spieler2: "+ChatColor.GREEN+chessbord.getGame().getSpieler2().getName());
                            p.sendMessage(ChatColor.DARK_GREEN+"Zeit: "+ChatColor.GREEN+chessbord.getTime());
                            p.sendMessage(ChatColor.DARK_GREEN+"Zug: "+ChatColor.GREEN+chessbord.getGame().getTurn());
                            p.sendMessage(ChatColor.DARK_GREEN+"---------------------------");
                        }
                    });
                    break;
                case "delete":
                    schachliste.stream().filter(chessbord -> chessbord.getName().equals(args[2])).findFirst().ifPresent(chessbord -> {
                        chessbord.setGame(null);
                        schachliste.remove(chessbord);
                        p.sendMessage(ChatColor.DARK_GREEN+"Schachbrett gelöscht: "+ChatColor.GREEN+args[2]);
                    });
                    break;
            }
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e){
            p.sendMessage(error);
        }
    }

}
