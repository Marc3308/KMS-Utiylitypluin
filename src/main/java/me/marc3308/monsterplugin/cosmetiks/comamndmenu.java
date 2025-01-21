package me.marc3308.monsterplugin.cosmetiks;

import me.marc3308.monsterplugin.Monsterplugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class comamndmenu implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))return false;
        Player p= (Player) sender;

        Inventory Cosmetikmenu = Bukkit.createInventory(p,54,"§lCosmetik Menu");

        ItemStack glass= new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassmeta = glass.getItemMeta();
        glassmeta.setDisplayName(" ");
        glass.setItemMeta(glassmeta);

        //glass
        for (int i=0;i<9;i++)Cosmetikmenu.setItem(i,glass); //top
        for (int i=1;i<5;i++)Cosmetikmenu.setItem(i*9,glass); //left side
        for (int i=0;i<5;i++)Cosmetikmenu.setItem(8+(i*9),glass); //right side
        for (int i=45;i<54;i++)Cosmetikmenu.setItem(i,glass); //bot


        boolean kopf=p.getPersistentDataContainer().has(new NamespacedKey(Monsterplugin.getPlugin(), "comichead"), PersistentDataType.INTEGER);

        ItemStack head= new ItemStack(kopf ? Material.PAPER : Material.BARRIER);
        ItemMeta headmeta = head.getItemMeta();
        headmeta.setDisplayName("Kopf");
        headmeta.setLore(new ArrayList<>(){{
            add("Deine Kopf Cosmetik");
        }});
        if(kopf)headmeta.setCustomModelData(p.getPersistentDataContainer().get(new NamespacedKey(Monsterplugin.getPlugin(), "comichead"), PersistentDataType.INTEGER));
        head.setItemMeta(headmeta);

        ItemStack placeholder= new ItemStack(Material.BARRIER);
        ItemMeta placeholdermeta = placeholder.getItemMeta();
        placeholdermeta.setDisplayName(" ");
        placeholder.setItemMeta(placeholdermeta);

        Cosmetikmenu.setItem(13,head); //kopf
        Cosmetikmenu.setItem(22,placeholder); //körper
        Cosmetikmenu.setItem(31,placeholder); //beine
        Cosmetikmenu.setItem(40,placeholder); //füße

        Cosmetikmenu.setItem(29,placeholder); //mainhand
        Cosmetikmenu.setItem(33,placeholder); //offhand


        p.openInventory(Cosmetikmenu);
        return false;
    }
}
