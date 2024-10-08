package me.marc3308.monsterplugin.eventlistener;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import static me.marc3308.monsterplugin.Monsterplugin.eiss;

public class noenchants implements Listener {

    @EventHandler
    public void menif(InventoryClickEvent e){
        if(e.getCurrentItem()==null)return;
        for(Enchantment en : e.getCurrentItem().getEnchantments().keySet()){
            if(eiss.get("enchantments"+"."+en.getName())!=null && !eiss.getBoolean("enchantments"+"."+en.getName())){
                e.setCancelled(true);
                ItemMeta met=e.getCurrentItem().getItemMeta();
                met.removeEnchant(en);
                e.getCurrentItem().setItemMeta(met);
            }
        }
    }

    @EventHandler
    public void onenchant(PrepareItemEnchantEvent e){
        for (int i = 0; i < e.getExpLevelCostsOffered().length; i++) {
            if (e.getExpLevelCostsOffered()[i] > eiss.getInt("enchantments"+"."+"MaxLv")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onambos(PrepareAnvilEvent e){
        if(eiss.getBoolean("Ambos2slot"))return;
        if(e.getInventory().getSecondItem()==null)return;
        if(!e.getInventory().getSecondItem().getType().equals(Material.AIR)){
            e.setResult(new ItemStack(Material.AIR));
        }
    }
}
