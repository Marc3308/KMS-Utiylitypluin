package me.marc3308.monsterplugin.eventlistener;

import me.marc3308.monsterplugin.commands.playerlog;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

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
                    playerlog.openinv(p,null,(Integer.valueOf(e.getCurrentItem().getItemMeta().getDisplayName())+1),e.getInventory().getItem(47).getItemMeta().getDisplayName());
                    break;
                case BOOK:
                    playerlog.openinv(p,null,1,e.getInventory().getItem(47).getItemMeta().getDisplayName());
                    break;
                case ANVIL:
                    String sorter = e.getInventory().getItem(47).getItemMeta().getDisplayName();
                    sorter=sorter.equals("LastOfLog") ? "FirstOnLock"
                            :sorter.equals("FirstOnLock") ? "MostPlayTime"
                            :sorter.equals("MostPlayTime") ? "UUID"
                            :sorter.equals("UUID") ? "PlayerName"
                            :"LastOfLog";
                    playerlog.openinv(p,null
                            ,(Integer.valueOf(e.getInventory().getItem(51).getItemMeta().getDisplayName()))
                            ,sorter);
                    break;
            }


        }


    }
}
