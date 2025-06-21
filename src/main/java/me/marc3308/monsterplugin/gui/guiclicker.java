package me.marc3308.monsterplugin.gui;

import me.marc3308.kMSCustemModels.extras;
import me.marc3308.monsterplugin.Monsterplugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

public class guiclicker implements Listener {

    @EventHandler
    public void onclick(InventoryClickEvent e){
        Player p= (Player) e.getWhoClicked();
        if(e.getCurrentItem()==null)return;

        if(e.getView().getTitle().equals("Weltenfresser")){
            e.setCancelled(true);
            switch (e.getSlot()){
                case 12:
                    if(e.getAction().equals(InventoryAction.PICKUP_ALL)){
                        Monsterplugin.settings.setBig(Monsterplugin.settings.getBig()-10);
                    } else if (e.getAction().equals(InventoryAction.PICKUP_HALF)) {
                        Monsterplugin.settings.setBig(Monsterplugin.settings.getBig()-100);
                    } else if (e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                        Monsterplugin.settings.setBig(Monsterplugin.settings.getBig()-1000);
                    }
                    break;
                case 14:
                    if(e.getAction().equals(InventoryAction.PICKUP_ALL)){
                        Monsterplugin.settings.setBig(Monsterplugin.settings.getBig()+10);
                    } else if (e.getAction().equals(InventoryAction.PICKUP_HALF)) {
                        Monsterplugin.settings.setBig(Monsterplugin.settings.getBig()+100);
                    } else if (e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                        Monsterplugin.settings.setBig(Monsterplugin.settings.getBig()+1000);
                    }
                    break;
                case 19:
                    Monsterplugin.settings.setDenied(!Monsterplugin.settings.isDenied());
                    break;
                case 20:
                    extras.openMaterialAnvilgui(p, Bukkit.createInventory(p,45, "Weltenfresser"),"Verbotene Blöcke"
                            ,"Material",
                            sti -> {
                                if(e.getAction().equals(InventoryAction.PICKUP_ALL)){
                                    Monsterplugin.settings.getDeniedblocks().remove(sti);
                                } else {
                                    Monsterplugin.settings.getDeniedblocks().add(sti);
                                }
                            });
                    return;
                case 24:
                    extras.openStringAnvilgui(p, Bukkit.createInventory(p,45, "Weltenfresser"),"Chanchen Blöcke"
                            ,"Material;Chanche",
                            sti -> {
                                if(e.getAction().equals(InventoryAction.PICKUP_ALL)){
                                    Monsterplugin.settings.getPersentblocks().remove(Material.valueOf(sti.split(";")[0]));
                                } else {
                                    Monsterplugin.settings.getPersentblocks().put(Material.valueOf(sti.split(";")[0]), Integer.valueOf(sti.split(";")[1]));
                                }
                            });
                    return;
                case 25:
                    Monsterplugin.settings.setPresent(!Monsterplugin.settings.isPresent());
                    break;
                case 30:
                    p.openInventory(Bukkit.createInventory(p,54, "Admin Menu"));
                    return;
                case 32:
                    p.closeInventory();
                    Monsterplugin.settings.start();
                    return;
            }
            p.openInventory(Bukkit.createInventory(p,45, "Weltenfresser"));

        }
    }
}
