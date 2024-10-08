package me.marc3308.monsterplugin.eventlistener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class destorydiamonds implements Listener {

    public static ArrayList<Material> dialist=new ArrayList<>();

    public destorydiamonds() {
        dialist.add(Material.DIAMOND_SWORD);
        dialist.add(Material.DIAMOND_AXE);
        dialist.add(Material.DIAMOND_PICKAXE);
        dialist.add(Material.DIAMOND_HOE);
        dialist.add(Material.DIAMOND_SHOVEL);

        dialist.add(Material.DIAMOND_BLOCK);

        dialist.add(Material.DIAMOND_HELMET);
        dialist.add(Material.DIAMOND_CHESTPLATE);
        dialist.add(Material.DIAMOND_LEGGINGS);
        dialist.add(Material.DIAMOND_BOOTS);

    }

    @EventHandler
    public static void ondia(InventoryClickEvent e){

        Player p= (Player) e.getWhoClicked();
        if(!p.getGameMode().equals(GameMode.SURVIVAL))return;
        if(e.getCurrentItem()==null)return;

        ItemStack stick=new ItemStack(Material.STICK);
        ItemMeta sitcl_meta= stick.getItemMeta();
        ArrayList<String> sticl_lore=new ArrayList<>();
        sticl_lore.add("Ein Opfer für die Barrriere..");
        sitcl_meta.addEnchant(Enchantment.DURABILITY,1,false);
        sitcl_meta.setLore(sticl_lore);
        stick.setItemMeta(sitcl_meta);

        if(dialist.contains(e.getCurrentItem().getType()))e.setCurrentItem(stick);
    }


}
