package me.marc3308.monsterplugin.cosmetiks;

import me.marc3308.monsterplugin.Monsterplugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

import static me.marc3308.monsterplugin.Monsterplugin.cosmetikslist;

public class comamndmenu implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))return false;
        Player p= (Player) sender;

        Inventory Cosmetikmenu = Bukkit.createInventory(p,54,"§lCosmetic-Menü");

        ItemStack glass= new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassmeta = glass.getItemMeta();
        glassmeta.setDisplayName(" ");
        glass.setItemMeta(glassmeta);

        //glass
        for (int i=0;i<9;i++)Cosmetikmenu.setItem(i,glass); //top
        for (int i=1;i<5;i++)Cosmetikmenu.setItem(i*9,glass); //left side
        for (int i=0;i<5;i++)Cosmetikmenu.setItem(8+(i*9),glass); //right side
        for (int i=45;i<54;i++)Cosmetikmenu.setItem(i,glass); //bot

        Cosmetikmenu.setItem(13,itembuilder(p,"Kopf")); //kopf
        Cosmetikmenu.setItem(22,itembuilder(p,"Körper")); //körper
        Cosmetikmenu.setItem(31,itembuilder(p,"Beine")); //beine
        Cosmetikmenu.setItem(40,itembuilder(p,"Füße")); //füße

        Cosmetikmenu.setItem(29,itembuilder(p,"???")); //mainhand
        Cosmetikmenu.setItem(33,itembuilder(p,"Offhand")); //offhand


        p.openInventory(Cosmetikmenu);
        return false;
    }

    private ItemStack itembuilder(Player p, String sorter){
        String custem ="testit";
        switch (sorter){
            case "Kopf":
                custem="comichead";
                break;
            case "Körper":
                custem="comicchest";
                break;
            case "Beine":
                custem="comiclegs";
                break;
            case "Füße":
                custem="comicfeet";
                break;
            case "Mainhand":
                custem="commicmainhand";
                break;
            case "Offhand":
                custem="comicoffhand";
                break;
        }

        if(p.getPersistentDataContainer().has(new NamespacedKey(Monsterplugin.getPlugin(), custem), PersistentDataType.INTEGER)){
            for (Cosmetikobjekt c : cosmetikslist) {
                if (p.getPersistentDataContainer().get(new NamespacedKey(Monsterplugin.getPlugin(), custem), PersistentDataType.INTEGER).equals(c.getCustommodeldata()) && c.getKörperteil().equals(sorter)) {
                    ItemStack head = new ItemStack(Material.valueOf(c.getMaterial()));
                    ItemMeta headmeta = head.getItemMeta();
                    headmeta.setDisplayName(sorter + " [" + c.getName() + "]");
                    headmeta.setLore(new ArrayList<>() {{
                        add("Hier findest du deine " + sorter + "-Cosmetics");
                        add("");
                        add(ChatColor.YELLOW + "Linksklick zum Wechseln");
                    }});
                    headmeta.setCustomModelData(p.getPersistentDataContainer().get(new NamespacedKey(Monsterplugin.getPlugin(), custem), PersistentDataType.INTEGER));
                    head.setItemMeta(headmeta);
                    return head;
                }
            }
        }

        ItemStack placeholder= new ItemStack(Material.BARRIER);
        ItemMeta placeholdermeta = placeholder.getItemMeta();
        placeholdermeta.setDisplayName(sorter);
        placeholdermeta.setLore(new ArrayList<>() {{
            add("Hier findest du deine " + sorter + "-Cosmetics");
            add("");
            add(ChatColor.YELLOW + "Linksklick zum Wechseln");
        }});
        placeholder.setItemMeta(placeholdermeta);
        return placeholder;
    }
}
