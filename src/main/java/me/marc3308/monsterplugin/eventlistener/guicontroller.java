package me.marc3308.monsterplugin.eventlistener;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static me.marc3308.monsterplugin.commands.playerlog.openinv;
import static me.marc3308.monsterplugin.commands.playerlog.openleaderinv;

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
                    openinv(p,null,(Integer.valueOf(e.getCurrentItem().getItemMeta().getDisplayName())+1),e.getInventory().getItem(47).getItemMeta().getDisplayName());
                    break;
                case BOOK:
                    openinv(p,null,1,e.getInventory().getItem(47).getItemMeta().getDisplayName());
                    break;
                case ANVIL:
                    String sorter = e.getInventory().getItem(47).getItemMeta().getDisplayName();
                    sorter=sorter.equals("LastOfLog") ? "FirstOnLock"
                            :sorter.equals("FirstOnLock") ? "MostPlayTime"
                            :sorter.equals("MostPlayTime") ? "UUID"
                            :sorter.equals("UUID") ? "PlayerName"
                            :sorter.equals("PlayerName") ? "RpName"
                            :sorter.equals("RpName") ? "Spezien"
                            :"LastOfLog";
                    openinv(p,null
                            ,(Integer.valueOf(e.getInventory().getItem(51).getItemMeta().getDisplayName()))
                            ,sorter);
                    break;
                case WRITTEN_BOOK:
                    openleaderinv(p,1, 0);
                    break;
            }


        }

        if(e.getView().getTitle().equalsIgnoreCase("§lSpieler Bestenliste")){
            e.setCancelled(true);
            if(e.getCurrentItem()==null)return;
            switch (e.getCurrentItem().getType()){
                case ARROW:
                    openleaderinv(p,(Integer.valueOf(e.getCurrentItem().getItemMeta().getDisplayName())+1),Integer.valueOf(e.getInventory().getItem(47).getItemMeta().getLore().getFirst()));
                    break;
                case BOOK:
                    openleaderinv(p,1,Integer.valueOf(e.getInventory().getItem(47).getItemMeta().getLore().getFirst()));
                    break;
                case ANVIL:
                    openleaderinv(p
                            ,(Integer.valueOf(e.getInventory().getItem(51).getItemMeta().getDisplayName()))
                            ,Statistic.values().length-1==Integer.valueOf(e.getInventory().getItem(47).getItemMeta().getLore().getFirst())
                                    ? 1 : Integer.valueOf(e.getInventory().getItem(47).getItemMeta().getLore().getFirst())+1);
                    break;
                case BARRIER:
                    openinv(p,null,1,"LastOfLog");
                    break;
            }
        }
    }
}
