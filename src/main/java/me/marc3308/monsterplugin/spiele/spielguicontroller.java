package me.marc3308.monsterplugin.spiele;

import me.marc3308.monsterplugin.spiele.chess.objekts.Chessbord;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static me.marc3308.monsterplugin.spiele.chess.chessutilitys.schachliste;

public class spielguicontroller implements Listener {

    @EventHandler
    public void oninvclick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getCurrentItem()==null)return;


        //todo momentan kann man nur schach erstellen
        if(e.getView().getTitle().equalsIgnoreCase("§l§eGames-Menu")){
            e.setCancelled(true);
            switch (e.getCurrentItem().getType()){
                case BARRIER:
                    p.closeInventory();
                    break;
                case ANVIL:
                    openSpieleMenu(p,1,e.getInventory().getItem(47).getItemMeta().getDisplayName().equals("Schach") ? "TicTacToe" : "Schach");
                    break;
                case BOOK:
                    openSpieleMenu(p,1,e.getInventory().getItem(47).getItemMeta().getDisplayName());
                    break;
                case ARROW:
                    openSpieleMenu(p,Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName()),e.getInventory().getItem(47).getItemMeta().getDisplayName());
                    break;
                //schach
                case WHITE_CONCRETE:
                    switch (e.getInventory().getItem(47).getItemMeta().getDisplayName()){
                        case "Schach":
                            schachliste.forEach(bord ->{
                                if(bord.getName().equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName().substring(4))){
                                    opengamemenu(p,"Schach",bord.getName());
                                }
                            });
                            break;
                        case "TicTacToe":
                            break;

                    }
                    break;
                //neues spiel
                case COMMAND_BLOCK:
                    switch (e.getInventory().getItem(47).getItemMeta().getDisplayName()){
                        case "Schach":
                            schachliste.add(new Chessbord(p.getLocation(),"Schach"+(schachliste.size()+1),true,5,true,0));
                            break;
                    }
                    openSpieleMenu(p,Integer.parseInt(e.getInventory().getItem(51).getItemMeta().getDisplayName())-1,e.getInventory().getItem(47).getItemMeta().getDisplayName());
                    break;
            }
        }

        //alle schach varianten
        if(e.getView().getTitle().split(" ")[0].equals("Schach")){
            e.setCancelled(true);
            schachliste.stream().filter(b -> b.getName().equals(e.getView().getTitle().split(" ")[1])).findFirst().ifPresentOrElse(b -> {
                switch (e.getSlot()){
                    //location
                    case 11:
                        b.setFeldLoc(p.getLocation());
                        break;
                        //name
                    case 20:
                        //todo spieler leiste eingeben lassen neuen namen
                        //todo rausfinden wie zur hölle anvils inv gehen
                        break;
                        //hilfslinien
                    case 29:
                        b.setHelp(!b.isHelp());
                        break;
                        //zughistory
                    case 15:
                        b.setHistory(!b.isHistory());
                        break;
                        //zeit
                    case 24:
                        if(b.getTimer()>0)b.setTimer(b.getTimer()-1);
                        break;
                    case 25:
                        b.setTimer(b.getTimer()+1);
                        break;
                        //zugzeit
                    case 33:
                        if(b.getTime()>1)b.setTime(b.getTime()-1);
                        break;
                    case 34:
                        b.setTime(b.getTime()+1);
                        break;
                        //spiel löschen
                    case 53:
                        schachliste.remove(b);
                        //zurück
                    case 45:
                        openSpieleMenu(p,1,"Schach");
                        return;
                }
                opengamemenu(p,"Schach",b.getName());
            }, () -> {
                p.closeInventory();
                p.sendMessage("§cDas Spiel existiert nicht");
            });
        }
    }

    public static void openSpieleMenu(Player p, int Seite, String game) {
        //create inventory
        Inventory inv = Bukkit.createInventory(null,54,"§l§eGames-Menu");

        //feste items
        inv.setItem(45,new ItemStack(Material.BARRIER){{
            ItemMeta meta = getItemMeta();
            meta.setDisplayName("§c§lZurück");
            setItemMeta(meta);
        }});
        inv.setItem(47,new ItemStack(Material.ANVIL){{
            ItemMeta meta = getItemMeta();
            meta.setDisplayName(game);
            meta.setLore(new ArrayList<>(){{
                add("Mögliche Optionen:");
                add("Schach");
                add("TicTacToe");
            }});
            setItemMeta(meta);
        }});
        inv.setItem(49,new ItemStack(Material.BOOK){{
            ItemMeta meta = getItemMeta();
            meta.setDisplayName("§c§lSeite 1");
            meta.setLore(new ArrayList<>(){{
                add("Clicke hier um auf die, erste Seite zu gelangen");
            }});
            setItemMeta(meta);
        }});
        inv.setItem(51,new ItemStack(Material.ARROW){{
            ItemMeta meta = getItemMeta();
            meta.setDisplayName(String.valueOf((Seite+1)));
            setItemMeta(meta);
        }});
        inv.setItem(53,new ItemStack(Material.COMMAND_BLOCK){{
            ItemMeta meta = getItemMeta();
            meta.setDisplayName(ChatColor.GREEN+"Create New Game");
            meta.setLore(new ArrayList<>(){{
                add("Erstelle ein neues Spiel");
            }});
            setItemMeta(meta);
        }});

        //games
        if(game.equalsIgnoreCase("Schach")){
            schachliste.forEach(bord ->{
                if(schachliste.indexOf(bord)>=44*(Seite-1) && schachliste.indexOf(bord)<=44*Seite){
                    inv.setItem(inv.firstEmpty(),new ItemStack(Material.WHITE_CONCRETE){{
                        ItemMeta meta = getItemMeta();
                        meta.setDisplayName("§a§l"+bord.getName());
                        meta.setLore(new ArrayList<>(){{
                            add("-------Einstellungen-------");
                            add("§7ZugHistorye:  "+(bord.isHistory() ? "§aAktiv" : "§cInaktiv"));
                            add("§7Hilfslinien:  "+(bord.isHelp() ? "§aAktiv" : "§cInaktiv"));
                            add("§7FeltStart:§e    X"+bord.getFeltstart().getBlockX()+" Y"+bord.getFeltstart().getBlockY()+" Z"+bord.getFeltstart().getBlockZ());
                            add("§7FeltEnde: §e    X"+bord.getFeltend().getBlockX()+" Y"+bord.getFeltend().getBlockY()+" Z"+bord.getFeltend().getBlockZ());
                            add("§7ZugZeit: §e     "+bord.getTime()+" sec");
                            add("§7Zeit: §e         "+(bord.getTimer()>0 ? bord.getTimer()+" Min" : "Unbegrenzt"));


                        }});
                        setItemMeta(meta);
                    }});
                }
            });
        }
        p.openInventory(inv);
    }

    private void opengamemenu(Player p, String game, String name){
        switch (game){
            case "Schach":
                schachliste.forEach(bord ->{
                    if(bord.getName().equalsIgnoreCase(name)){
                        Inventory inv = Bukkit.createInventory(null,54,"Schach "+bord.getName());

                        //row one
                        inv.setItem(10,new ItemStack(Material.COMPASS){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName("§a§lMomentane Position:");
                            meta.setLore(new ArrayList<>(){{
                                add("§7FeltStart:§e    [X"+bord.getFeltstart().getBlockX()+" Y"+bord.getFeltstart().getBlockY()+" Z"+bord.getFeltstart().getBlockZ()+"]");
                                add("§7FeltEnde: §e    [X"+bord.getFeltend().getBlockX()+" Y"+bord.getFeltend().getBlockY()+" Z"+bord.getFeltend().getBlockZ()+"]");
                            }});
                            setItemMeta(meta);
                        }});
                        inv.setItem(11,new ItemStack(Material.GREEN_CONCRETE){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName(ChatColor.GREEN+"Hier Clicken um neue Position zu setzen");
                            meta.setLore(new ArrayList<>(){{
                                add("§7Die Position wird auf die Jetzige Position");
                                add("§7des Spielers gesetzt");
                            }});
                            setItemMeta(meta);
                        }});
                        inv.setItem(19,new ItemStack(Material.NAME_TAG){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName("§a§lName: "+bord.getName());
                            setItemMeta(meta);
                        }});
                        inv.setItem(20,new ItemStack(Material.GREEN_CONCRETE){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName(ChatColor.GREEN+"Hier Clicken um den Namen zu ändern");
                            setItemMeta(meta);
                        }});
                        inv.setItem(28,new ItemStack(Material.OAK_SIGN){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName("§a§lHilfslinien: "+(bord.isHelp() ? "§aAktiv" : "§cInaktiv"));
                            meta.setLore(new ArrayList<>(){{
                                add("§7Hilfslinien sind Linien die dir helfen");
                                add("§7zu sehen wo du hin ziehen kannst");
                            }});
                            setItemMeta(meta);
                        }});
                        inv.setItem(29,new ItemStack(bord.isHelp() ? Material.GREEN_CONCRETE_POWDER : Material.RED_CONCRETE){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName(bord.isHelp() ? "§a§lHilfslinien aktivieren" : "§c§lHilfslinien deaktivieren");
                            setItemMeta(meta);
                        }});

                        //row two
                        inv.setItem(14,new ItemStack(Material.BOOK){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName("§a§lZugHistory: "+(bord.isHistory() ? "§aAktiv" : "§cInaktiv"));
                            meta.setLore(new ArrayList<>(){{
                                add("§7Die History hilft dir die");
                                add("§7übersicht über alle gespielten Züge zu behalten");
                            }});
                            setItemMeta(meta);
                        }});
                        inv.setItem(15,new ItemStack(bord.isHistory() ? Material.GREEN_CONCRETE_POWDER : Material.RED_CONCRETE){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName(bord.isHistory() ? "§a§lZugHistory aktivieren" : "§c§lZugHistory deaktivieren");
                            setItemMeta(meta);
                        }});
                        inv.setItem(23,new ItemStack(Material.CLOCK){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName("§a§lZeit: "+(bord.getTimer()>0 ? bord.getTimer()+"Min" : "Unbegrenzt"));
                            meta.setLore(new ArrayList<>(){{
                                add("§7Die Zeit ist die Zeit die du hast");
                                add("§7um deine Züge zu machen");
                                add("§7Stellst du die Zeit auf 0 ist das Spiel unbegrenzt");
                            }});
                            setItemMeta(meta);
                        }});
                        inv.setItem(24,new ItemStack(Material.RED_CONCRETE){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName("§c§l-1 Min");
                            setItemMeta(meta);
                        }});
                        inv.setItem(25,new ItemStack(Material.GREEN_CONCRETE){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName("§a§l+1 Min");
                            setItemMeta(meta);
                        }});
                        inv.setItem(32,new ItemStack(Material.ENDER_PEARL){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName("§a§lZugzeit: "+bord.getTime()+"sec");
                            meta.setLore(new ArrayList<>(){{
                                add("§7Die ZugZeit ist die Zeit");
                                add("§7Die Eine figur brauch um ihr Ziel zu erreichen");
                            }});
                            setItemMeta(meta);
                        }});
                        inv.setItem(33,new ItemStack(Material.RED_CONCRETE){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName("§c§l-1 Sec");
                            setItemMeta(meta);
                        }});
                        inv.setItem(34,new ItemStack(Material.GREEN_CONCRETE){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName("§a§l+1 Sec");
                            setItemMeta(meta);
                        }});

                        //under row
                        inv.setItem(45,new ItemStack(Material.BARRIER){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName("§c§lZurück");
                            setItemMeta(meta);
                        }});
                        inv.setItem(53,new ItemStack(Material.COMMAND_BLOCK){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName("§a§lSpielLöschen");
                            meta.setLore(new ArrayList<>(){{
                                add("§cLösche dieses spiel");
                            }});
                            setItemMeta(meta);
                        }});


                        p.openInventory(inv);
                    }
                });
                break;
            case "TicTacToe":
                break;
        }

    }
}
