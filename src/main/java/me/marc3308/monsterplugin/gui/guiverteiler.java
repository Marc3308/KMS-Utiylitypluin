package me.marc3308.monsterplugin.gui;

import me.marc3308.monsterplugin.Monsterplugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class guiverteiler implements Listener {


    @EventHandler
    public void onopen(InventoryOpenEvent e) {

        Inventory inv = e.getInventory();

        if(e.getView().getTitle().equalsIgnoreCase("Weltenfresser")){
            inv.setItem(12,new ItemStack(Material.RED_CONCRETE){{
                ItemMeta meta = getItemMeta();
                meta.setDisplayName(ChatColor.RED+"-10 | -100 | -1000");
                meta.setLore(new ArrayList<>(){{
                    add("Linksklick: -10");
                    add("Rechtsklick: -100");
                    add("Shift + Linksklick: -1000");
                }});
                setItemMeta(meta);
            }});
            inv.setItem(13,new ItemStack(Material.LEAD){{
                ItemMeta meta = getItemMeta();
                meta.setDisplayName(ChatColor.YELLOW+""+Monsterplugin.settings.getBig()+ChatColor.WHITE+"["+Monsterplugin.settings.getBig()*2+"x"+Monsterplugin.settings.getBig()*2+"]");
                meta.setLore(new ArrayList<>(){{
                    add("Momentane Größe des Weltenfressers");
                    add("["+Monsterplugin.settings.getBig()+"/-"+Monsterplugin.settings.getBig()+"]");
                }});
                setItemMeta(meta);
            }});
            inv.setItem(14,new ItemStack(Material.GREEN_CONCRETE){{
                ItemMeta meta = getItemMeta();
                meta.setDisplayName(ChatColor.GREEN+"+10 | +100 | +1000");
                meta.setLore(new ArrayList<>(){{
                    add("Linksklick: +10");
                    add("Rechtsklick: +100");
                    add("Shift + Linksklick: +1000");
                }});
                setItemMeta(meta);
            }});
            inv.setItem(19,new ItemStack(Monsterplugin.settings.isDenied() ? Material.GREEN_CONCRETE : Material.RED_CONCRETE){{
                ItemMeta meta = getItemMeta();
                meta.setDisplayName(ChatColor.YELLOW+"Toggle: "+Monsterplugin.settings.isDenied());
                meta.setLore(new ArrayList<>(){{
                    add("Toggle ob verbotene blöcke berücksichtigt werden");
                }});
                setItemMeta(meta);
            }});
            inv.setItem(20,new ItemStack(Material.BARRIER){{
                ItemMeta meta = getItemMeta();
                meta.setDisplayName("Verbotene Blöcke");
                meta.setLore(new ArrayList<>(){{
                    Monsterplugin.settings.getDeniedblocks().forEach(b -> add((Monsterplugin.settings.getDeniedblocks().indexOf(b)+1)+": "+b.name()));
                    add("");
                    add("Linksklick: Block entfernen");
                    add("Rechtsklick: Block hinzufügen");
                }});
                setItemMeta(meta);
            }});
            inv.setItem(22,new ItemStack(Material.WARDEN_SPAWN_EGG){{
                ItemMeta meta = getItemMeta();
                meta.setDisplayName(ChatColor.RED+"WARNUNG DIES IST SEHR SERVER LASTIG");
                meta.setLore(new ArrayList<>(){{
                    add("Es wird zu lags kommen und sehr viel wird gelöscht");
                }});
                setItemMeta(meta);
            }});
            inv.setItem(24,new ItemStack(Material.WANDERING_TRADER_SPAWN_EGG){{
                ItemMeta meta = getItemMeta();
                meta.setDisplayName("Verbotene Blöcke");
                meta.setLore(new ArrayList<>(){{
                    Monsterplugin.settings.getPersentblocks().forEach((b,j) -> add(b.name()+": "+j));
                    add("");
                    add("Linksklick: Block entfernen");
                    add("Rechtsklick: Block hinzufügen [ ; trennung block und wert ]");
                }});
                setItemMeta(meta);
            }});
            inv.setItem(25,new ItemStack(Monsterplugin.settings.isPresent() ? Material.GREEN_CONCRETE : Material.RED_CONCRETE){{
                ItemMeta meta = getItemMeta();
                meta.setDisplayName(ChatColor.YELLOW+"Toggle: "+Monsterplugin.settings.isPresent());
                meta.setLore(new ArrayList<>(){{
                    add("Toggle ob Chanchen blöcke berücksichtigt werden");
                }});
                setItemMeta(meta);
            }});
            inv.setItem(30,new ItemStack(Material.ARROW){{
                ItemMeta meta = getItemMeta();
                meta.setDisplayName(ChatColor.YELLOW+"Back");
                meta.setLore(new ArrayList<>(){{
                    add("Back");
                }});
                setItemMeta(meta);
            }});
            inv.setItem(32,new ItemStack(Material.CHAIN_COMMAND_BLOCK){{
                ItemMeta meta = getItemMeta();
                meta.setDisplayName(ChatColor.YELLOW+"START");
                meta.setLore(new ArrayList<>(){{
                    add("UFFPASSE DANN WIRD ES WILD");
                }});
                setItemMeta(meta);
            }});



        }
    }
}
