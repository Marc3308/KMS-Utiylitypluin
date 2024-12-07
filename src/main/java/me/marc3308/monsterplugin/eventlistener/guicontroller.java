package me.marc3308.monsterplugin.eventlistener;

import me.marc3308.monsterplugin.commands.playerlog;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

public class guicontroller implements Listener {

    @EventHandler
    public void ongui(InventoryClickEvent e){
        Player p= (Player) e.getWhoClicked();
        //siedlungsinventory
        if(e.getView().getTitle().equalsIgnoreCase("§lSpielerLog")){
            e.setCancelled(true);
            if(e.getCurrentItem()==null)return;
            switch (e.getCurrentItem().getType()){
                case ARROW:
                    playerlog.openinv(p,null,(Integer.valueOf(e.getCurrentItem().getItemMeta().getDisplayName())+1));
                    break;
                case BOOK:
                    Bukkit.dispatchCommand(p,"spielerinfo");
                    break;
            }


        }


    }
}
